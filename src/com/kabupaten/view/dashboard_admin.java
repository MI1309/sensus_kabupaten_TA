package com.kabupaten.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class dashboard_admin extends JFrame {
    private String currentUser;
    private JButton btnRtrw, btnKetuaRtrw, btnWarga, btnKecamatan, btnDesa, btnLogout;
    private JLabel lblUser;

    public dashboard_admin(String username) {
        this.currentUser = username;

        initComponents();
        setupFrame();
        setupActionListeners();
    }

    private void initComponents() {
        setTitle("Dashboard Admin - Selamat Datang, " + currentUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header panel
        JPanel headerPanel = createHeaderPanel();

        // Toolbar panel
        JPanel toolbarPanel = createToolbarPanel();

        // Dummy content panel (isi nanti bisa tabel atau card)
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(new JLabel("Copy right © 2025 Kabupaten Management System by Imron"));

        // Tambahkan ke main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(toolbarPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(39, 82, 139));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // logo
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        ImageIcon originalIcon = new JLabel(new ImageIcon("src/com/kabupaten/assets/logo.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        
        BufferedImage circleImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 50, 50);
        g2.setClip(circle);
        g2.drawImage(scaledImage, 0, 0, null);
        g2.dispose();
        ImageIcon roundedIcon = new ImageIcon(circleImage);
        JPanel logoLabel = new JPanel(roundedIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel titleLabel = new JLabel("SISTEM PENDATAAN KABUPATEN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        lblUser = new JLabel("User: " + currentUser);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUser.setForeground(Color.WHITE);

        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnLogout.setBackground(new Color(198, 40, 40));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel rightPanel = new JPanel(new FlowLayout());
        rightPanel.setBackground(new Color(39, 82, 139));
        rightPanel.add(lblUser);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(btnLogout);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createToolbarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        btnRtrw = createActionButton("Kelola RT RW", new Color(46, 125, 50));
        btnKetuaRtrw = createActionButton("Kelola ketua RT & RW", new Color(255, 152, 0));
        btnWarga = createActionButton("Kelola Warga", new Color(198, 40, 40));
        btnKecamatan = createActionButton("Kelola Kecamatan", new Color(33, 150, 243));
        btnDesa = createActionButton("Kelola Kelurahan / Desa", new Color(33, 150, 243));

        panel.add(btnRtrw);
        panel.add(btnKetuaRtrw);
        panel.add(btnWarga);
        panel.add(btnKecamatan);
        panel.add(btnDesa);

        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void setupActionListeners() {
        btnLogout.addActionListener(e -> logout());
        

        // nanti bisa tambah action button lain di sini
        btnRtrw.addActionListener(e -> JOptionPane.showMessageDialog(this, "Tambah data diklik"));
        btnKetuaRtrw.addActionListener(e -> JOptionPane.showMessageDialog(this, "Edit data diklik"));
        btnWarga.addActionListener(e -> JOptionPane.showMessageDialog(this, "Hapus data diklik"));
        btnDesa.addActionListener(e -> JOptionPane.showMessageDialog(this, "Refresh data diklik"));
        btnKecamatan.addActionListener(e -> JOptionPane.showMessageDialog(this, "btnKecamatan data diklik"));
    }


    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }

    private void setupFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
    }
}
