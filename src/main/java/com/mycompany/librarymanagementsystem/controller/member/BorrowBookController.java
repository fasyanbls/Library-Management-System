package com.mycompany.librarymanagementsystem.controller.member;

import com.mycompany.librarymanagementsystem.dao.BookDAO;
import com.mycompany.librarymanagementsystem.dao.IssuedBookDAO;
import com.mycompany.librarymanagementsystem.model.Book;
import com.mycompany.librarymanagementsystem.model.Member;
import com.mycompany.librarymanagementsystem.util.DateUtil;
import com.mycompany.librarymanagementsystem.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/member/borrow-book")
public class BorrowBookController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;
    private IssuedBookDAO issuedBookDAO;
    
    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
        issuedBookDAO = new IssuedBookDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/member/books");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Mendapatkan member dari session
        Member member = SessionUtil.getMemberFromSession(request);
        if (member == null) {
            response.sendRedirect(request.getContextPath() + "/member-login");
            return;
        }
        
        // Mendapatkan ID buku dari parameter
        String bookIdParam = request.getParameter("bookId");
        if (bookIdParam == null || bookIdParam.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "ID Buku tidak valid");
            response.sendRedirect(request.getContextPath() + "/member/books");
            return;
        }
        
        try {
            int bookId = Integer.parseInt(bookIdParam);
            
            // Mendapatkan data buku
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                request.getSession().setAttribute("errorMessage", "Buku tidak ditemukan");
                response.sendRedirect(request.getContextPath() + "/member/books");
                return;
            }
            
            // Memeriksa ketersediaan buku
            if (book.getAvailableCopies() <= 0 || !"AVAILABLE".equals(book.getStatus())) {
                request.getSession().setAttribute("errorMessage", "Buku tidak tersedia untuk dipinjam");
                response.sendRedirect(request.getContextPath() + "/member/books");
                return;
            }
            
            // Menghitung tanggal pengembalian (14 hari dari sekarang)
            Timestamp dueDate = DateUtil.calculateDueDate();
            
            // Menyimpan data peminjaman ke dalam database
            boolean success = issuedBookDAO.issueBook(bookId, member.getId(), dueDate);
            
            if (success) {
                // Mengurangi jumlah buku yang tersedia
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                if (book.getAvailableCopies() == 0) {
                    book.setStatus("BORROWED");
                }
                bookDAO.updateBook(book);
                
                request.getSession().setAttribute("successMessage", "Buku berhasil dipinjam. Silakan kembalikan sebelum " + 
                    DateUtil.formatDate(dueDate));
            } else {
                request.getSession().setAttribute("errorMessage", "Gagal meminjam buku. Silakan coba lagi.");
            }
            
            response.sendRedirect(request.getContextPath() + "/member/dashboard");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "ID Buku tidak valid");
            response.sendRedirect(request.getContextPath() + "/member/books");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Terjadi kesalahan: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/member/books");
        }
    }
}