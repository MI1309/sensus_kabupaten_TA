package com.kabupaten.main;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.sistem.LoadingScreen;
import com.kabupaten.view.LoginFrame;
import com.kabupaten.view.dashboard_admin;
import com.kabupaten.view.dashboard_guest;

import javax.swing.*;

/**
 * Kelas utama aplikasi Pendataan Kabupaten
 */
public class MainApplication {
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            // Menggunakan Nimbus Look and Feel untuk tampilan yang lebih modern
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Jika Nimbus tidak tersedia, gunakan default
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Kustomisasi UI
        setupUIDefaults();
        
        // Test koneksi database
        if (!DatabaseConnection.testConnection()) {
            JOptionPane.showMessageDialog(null, 
                "Gagal terhubung ke database!\n" +
                "Pastikan:\n" +
                "1. MySQL server sudah berjalan\n" +
                "2. Database 'db_kabupaten' sudah dibuat\n" +
                "3. Username dan password sesuai di DatabaseConnection.java",
                "Error Database", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Jalankan aplikasi di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    
    /**
     * Setup default UI properties untuk tampilan yang lebih menarik
     */
    private static void setupUIDefaults() {
        // Warna tema
        UIManager.put("nimbusBase", new java.awt.Color(51, 98, 140));
        UIManager.put("nimbusBlueGrey", new java.awt.Color(169, 176, 190));
        UIManager.put("control", new java.awt.Color(240, 240, 240));
        
        // Button styling
        UIManager.put("Button.font", new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        UIManager.put("Button[Default].backgroundPainter", null);
        
        // Table styling
        UIManager.put("Table.alternateRowColor", new java.awt.Color(245, 245, 245));
        UIManager.put("Table.gridColor", new java.awt.Color(200, 200, 200));
        
        // Panel styling
        UIManager.put("Panel.background", new java.awt.Color(250, 250, 250));
        
        // Font defaults
        UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        UIManager.put("Label.font", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        UIManager.put("TextField.font", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        UIManager.put("ComboBox.font", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        UIManager.put("Table.font", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        UIManager.put("TableHeader.font", new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    }
}