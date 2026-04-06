package com.kabupaten.database;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 * Kelas untuk mengelola koneksi database
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/db_kabupaten";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    /**
     * Selalu membuat koneksi baru
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "MySQL Driver tidak ditemukan!\nPastikan mysql-connector-java telah ditambahkan ke classpath.",
                "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("MySQL Driver tidak ditemukan", e);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Gagal koneksi ke database!\nPastikan MySQL server berjalan dan database tersedia.\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Gagal koneksi ke database", e);
        }
    }

    /**
     * Test koneksi database
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
