/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.librarymanagementsystem.dao;

/**
 *
 * @author DMC_Studio
 */

import com.mycompany.librarymanagementsystem.model.Admin;
import com.mycompany.librarymanagementsystem.util.DBConnection;
import com.mycompany.librarymanagementsystem.util.PasswordUtil;

import java.sql.*;

public class AdminDAO {
    
    /**
     * Get admin by username
     * @param username Admin username
     * @return Admin object or null if not found
     */
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setPassword(rs.getString("password"));
                    admin.setName(rs.getString("name"));
                    admin.setEmail(rs.getString("email"));
                    admin.setCreatedAt(rs.getTimestamp("created_at"));
                    return admin;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Authenticate admin login
     * @param username Username
     * @param password Password (not hashed)
     * @return Admin object if authentication successful, null otherwise
     */
//    public Admin authenticateAdmin(String username, String password) {
//        Admin admin = getAdminByUsername(username);
//        
//        if (admin != null && PasswordUtil.verifyPassword(password, admin.getPassword())) {
//            return admin;
//        }
//        
//        return null;
//    }
    
    public Admin authenticateAdmin(String username, String password) {
    Admin admin = getAdminByUsername(username);
    
    // Sementara terima login jika username cocok dan password adalah "admin123"
    if (admin != null && password.equals("admin123")) {
        return admin;
    }
    
    return null;
}
    
    /**
     * Update admin profile
     * @param admin Admin object with updated values
     * @param updatePassword Whether to update the password
     * @return true if successful, false otherwise
     */
    public boolean updateAdmin(Admin admin, boolean updatePassword) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE admin SET name = ?, username = ?, email = ?");
        
        if (updatePassword) {
            sqlBuilder.append(", password = ?");
        }
        
        sqlBuilder.append(" WHERE id = ?");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getUsername());
            stmt.setString(3, admin.getEmail());
            
            int paramIndex = 4;
            
            if (updatePassword) {
                stmt.setString(paramIndex++, PasswordUtil.hashPassword(admin.getPassword()));
            }
            
            stmt.setInt(paramIndex, admin.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}