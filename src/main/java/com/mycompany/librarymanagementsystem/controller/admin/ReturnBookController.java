package com.mycompany.librarymanagementsystem.controller.admin;

import com.mycompany.librarymanagementsystem.dao.BookDAO;
import com.mycompany.librarymanagementsystem.dao.IssuedBookDAO;
import com.mycompany.librarymanagementsystem.model.Admin;
import com.mycompany.librarymanagementsystem.model.IssuedBook;
import com.mycompany.librarymanagementsystem.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/admin/return-books")
public class ReturnBookController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IssuedBookDAO issuedBookDAO;
    private BookDAO bookDAO;
    
    @Override
    public void init() throws ServletException {
        issuedBookDAO = new IssuedBookDAO();
        bookDAO = new BookDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if admin is logged in
        Admin admin = SessionUtil.getAdminFromSession(request);
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/admin-login");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        if (action == null) {
            // Default action: show return book form
            showReturnBookForm(request, response);
        } else {
            switch (action) {
                case "search":
                    searchIssuedBook(request, response);
                    break;
                default:
                    showReturnBookForm(request, response);
            }
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if admin is logged in
        Admin admin = SessionUtil.getAdminFromSession(request);
        if (admin == null) {
            response.sendRedirect(request.getContextPath() + "/admin-login");
            return;
        }
        
        // Get action parameter
        String action = request.getParameter("action");
        
        if (action == null) {
            // Default action: show return book form
            showReturnBookForm(request, response);
        } else {
            switch (action) {
                case "return":
                    returnBook(request, response);
                    break;
                default:
                    showReturnBookForm(request, response);
            }
        }
    }
    
    private void showReturnBookForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get not returned books
        List<IssuedBook> notReturnedBooks = issuedBookDAO.getNotReturnedBooks();
        
        // Set attributes
        request.setAttribute("notReturnedBooks", notReturnedBooks);
        request.setAttribute("now", new Timestamp(System.currentTimeMillis()));
        
        // Forward to return book form
        request.getRequestDispatcher("/admin/return-books.jsp").forward(request, response);
    }
    
    private void searchIssuedBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get search parameter
        String bookId = request.getParameter("bookId");
        String memberId = request.getParameter("memberId");
        
        if ((bookId == null || bookId.isEmpty()) && (memberId == null || memberId.isEmpty())) {
            request.setAttribute("errorMessage", "Book ID or Member ID is required for search");
            showReturnBookForm(request, response);
            return;
        }
        
        try {
            List<IssuedBook> searchResults;
            
            if (bookId != null && !bookId.isEmpty()) {
                // Search by book ID
                int bookIdInt = Integer.parseInt(bookId);
                searchResults = issuedBookDAO.getNotReturnedBooksByBookId(bookIdInt);
            } else {
                // Search by member ID
                int memberIdInt = Integer.parseInt(memberId);
                searchResults = issuedBookDAO.getNotReturnedBooksByMemberId(memberIdInt);
            }
            
            // Set attributes
            request.setAttribute("notReturnedBooks", searchResults);
            request.setAttribute("now", new Timestamp(System.currentTimeMillis()));
            request.setAttribute("searchPerformed", true);
            
            if (bookId != null && !bookId.isEmpty()) {
                request.setAttribute("searchBookId", bookId);
            }
            
            if (memberId != null && !memberId.isEmpty()) {
                request.setAttribute("searchMemberId", memberId);
            }
            
            // Forward to return book form
            request.getRequestDispatcher("/admin/return-books.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid Book ID or Member ID");
            showReturnBookForm(request, response);
        }
    }
    
    private void returnBook(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // Get issued book ID
    String issuedBookId = request.getParameter("issuedBookId");
    
    System.out.println("Attempting to return issued book with ID: " + issuedBookId);
    
    if (issuedBookId == null || issuedBookId.isEmpty()) {
        request.setAttribute("errorMessage", "Issued Book ID is required");
        showReturnBookForm(request, response);
        return;
    }
    
    try {
        int issuedBookIdInt = Integer.parseInt(issuedBookId);
        
        // Get issued book details for verification
        IssuedBook issuedBook = issuedBookDAO.getIssuedBookById(issuedBookIdInt);
        if (issuedBook == null) {
            request.setAttribute("errorMessage", "Issued book record not found");
            showReturnBookForm(request, response);
            return;
        }
        
        if ("RETURNED".equals(issuedBook.getReturnStatus())) {
            request.setAttribute("errorMessage", "This book has already been returned");
            showReturnBookForm(request, response);
            return;
        }
        
        // Return book
        boolean success = issuedBookDAO.returnBook(issuedBookIdInt);
        
        if (success) {
            request.setAttribute("successMessage", "Book returned successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to return book. Please check server logs for details.");
        }
        
        showReturnBookForm(request, response);
        
    } catch (NumberFormatException e) {
        System.out.println("Invalid Issued Book ID: " + e.getMessage());
        request.setAttribute("errorMessage", "Invalid Issued Book ID");
        showReturnBookForm(request, response);
    } catch (Exception e) {
        System.out.println("Unexpected error during book return: " + e.getMessage());
        e.printStackTrace();
        request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        showReturnBookForm(request, response);
    }
}
}