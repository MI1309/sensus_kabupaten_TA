package com.kabupaten.dao;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk operasi CRUD User
 */
public class UserDAO {
    private Connection connection;
    
    public UserDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Mengambil semua data user
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY nama_lengkap";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return userList;
    }
    
    /**
     * Mencari user berdasarkan username, nama, atau email
     */
    public List<User> searchUsers(String keyword) {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users " +
                    "WHERE username LIKE ? OR nama_lengkap LIKE ? OR email LIKE ? " +
                    "ORDER BY nama_lengkap";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = mapResultSetToUser(rs);
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return userList;
    }
    
    /**
     * Cek apakah username sudah ada
     */
    public boolean isUsernameExists(String username, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        if (excludeId != null) {
            sql += " AND id_user != ?";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            if (excludeId != null) {
                stmt.setInt(2, excludeId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cek username: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cek apakah email sudah ada
     */
    public boolean isEmailExists(String email, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        if (excludeId != null) {
            sql += " AND id_user != ?";
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            if (excludeId != null) {
                stmt.setInt(2, excludeId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cek email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Menambah user baru
     */
    public boolean addUser(User user) {
        // Cek duplikat username
        if (isUsernameExists(user.getUsername(), null)) {
            System.err.println("Username sudah digunakan: " + user.getUsername());
            return false;
        }
        
        // Cek duplikat email
        if (isEmailExists(user.getEmail(), null)) {
            System.err.println("Email sudah digunakan: " + user.getEmail());
            return false;
        }
        
        String sql = "INSERT INTO users (username, password, nama_lengkap, email, role, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Dalam praktik nyata, hash password dulu
            stmt.setString(3, user.getNamaLengkap());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getStatus());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setIdUser(generatedKeys.getInt(1));
                    }
                }
                System.out.println("User berhasil ditambahkan dengan ID: " + user.getIdUser());
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error menambah user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Mengupdate data user
     */
    public boolean updateUser(User user) {
        // Cek duplikat username
        if (isUsernameExists(user.getUsername(), user.getIdUser())) {
            System.err.println("Username sudah digunakan: " + user.getUsername());
            return false;
        }
        
        // Cek duplikat email
        if (isEmailExists(user.getEmail(), user.getIdUser())) {
            System.err.println("Email sudah digunakan: " + user.getEmail());
            return false;
        }
        
        String sql = "UPDATE users SET username=?, nama_lengkap=?, email=?, role=?, status=? " +
                    "WHERE id_user=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getNamaLengkap());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setString(5, user.getStatus());
            stmt.setInt(6, user.getIdUser());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("User berhasil diupdate. ID: " + user.getIdUser());
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Reset password user
     */
    public boolean resetPassword(int idUser, String newPassword) {
        String sql = "UPDATE users SET password=? WHERE id_user=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword); // Hash password dulu di praktik nyata
            stmt.setInt(2, idUser);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Password berhasil direset untuk user ID: " + idUser);
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error reset password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Menghapus user
     */
    public boolean deleteUser(int idUser) {
        String sql = "DELETE FROM users WHERE id_user=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("User berhasil dihapus. ID: " + idUser);
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error menghapus user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Mengambil user berdasarkan ID
     */
    public User getUserById(int idUser) {
        String sql = "SELECT * FROM users WHERE id_user = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Helper method untuk mapping ResultSet ke object User
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setIdUser(rs.getInt("id_user"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setNamaLengkap(rs.getString("nama_lengkap"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        user.setFotoProfile(rs.getString("foto_profile"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return user;
    }
    
    /**
     * Mendapatkan total jumlah user
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as total FROM users";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Mendapatkan jumlah user berdasarkan role
     */
    public int getCountByRole(String role) {
        String sql = "SELECT COUNT(*) as total FROM users WHERE role = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung user per role: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
}