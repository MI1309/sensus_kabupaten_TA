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
    private static Connection connection;
    
    /**
     * Mendapatkan koneksi database
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Koneksi database berhasil!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver tidak ditemukan: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "MySQL Driver tidak ditemukan!\nPastikan mysql-connector-java telah ditambahkan ke classpath.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.err.println("Gagal koneksi ke database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Gagal koneksi ke database!\nPastikan MySQL server berjalan dan database tersedia.\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }
    
    /**
     * Menutup koneksi database
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("Error menutup koneksi: " + e.getMessage());
        }
    }
    
    /**
     * Test koneksi database
     */
    public static boolean testConnection() {
        Connection conn = getConnection();
        return conn != null;
    }
}