package com.kabupaten.view;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class dashboard_admin extends JFrame {
    private String currentUser;
    private JButton btnLogout;
    private JLabel lblUser;

    public dashboard_admin(String currentUser) {
        this.currentUser = "Admin"; // Ganti dengan mekanisme autentikasi yang sesuai

        initComponents();
        setupFrame();
    }

    private void initComponents() {
        setTitle("Dashboard Admin - Selamat Datang, " + currentUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color bgColor = new Color(245, 245, 245);

        // atur UIManager supaya tabPane ikut warna bg
        UIManager.put("TabbedPane.contentAreaColor", bgColor);
        UIManager.put("TabbedPane.background", bgColor);
        UIManager.put("TabbedPane.selected", Color.WHITE);
        UIManager.put("TabbedPane.unselectedBackground", bgColor);
        UIManager.put("TabbedPane.focus", bgColor);
        UIManager.put("TabbedPane.borderHightlightColor", bgColor);
        UIManager.put("TabbedPane.darkShadow", bgColor);
        UIManager.put("TabbedPane.light", bgColor);
        UIManager.put("TabbedPane.highlight", bgColor);
        UIManager.put("TabbedPane.contentAreaColor", bgColor);
        UIManager.put("TabbedPane.shadow", bgColor);
        UIManager.put("TabbedPane.darkShadow", bgColor);
        UIManager.put("TabbedPane.light", bgColor);
        UIManager.put("TabbedPane.highlight", bgColor);


        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // Header panel
        JPanel headerPanel = createHeaderPanel();

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.RIGHT) {
        @Override
        public void updateUI() {
            super.updateUI();
            setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
                @Override
                protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                    // kasih minimal lebar tab biar teks tidak overflow
                    int defaultWidth = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
                    return Math.max(150, defaultWidth); // minimal 150px
                }

                @Override
                protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                    // kasih tinggi tab sedikit lebih besar biar lega
                    return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) + 8;
                }

                @Override
                protected void paintTabBackground(Graphics g, int tabPlacement,
                                                int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                    if (isSelected) {
                        g.setColor(new Color(39, 82, 139)); // biru tab aktif
                    } else {
                        g.setColor(new Color(200, 220, 250)); // tab nonaktif
                    }
                    g.fillRect(x, y, w, h);
                }

                @Override
                protected void paintText(Graphics g, int tabPlacement, Font font,
                                        FontMetrics metrics, int tabIndex,
                                        String title, Rectangle textRect,
                                        boolean isSelected) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                    if (isSelected) {
                        g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                        g2.setColor(Color.WHITE);
                    } else {
                        g2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                        g2.setColor(Color.DARK_GRAY);
                    }

                    g2.drawString(title, textRect.x, textRect.y + metrics.getAscent());
                    g2.dispose();
                }
            });
        }
    };



        tabbedPane.setFont(new Font("Roboto", Font.BOLD, 13));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder()); // jaga-jaga


        // matiin supaya tab dummy nggak bisa diklik
        
        // Tambah tab untuk tiap menu
        tabbedPane.addTab("Kelola RT/RW", new CrudRTRWPanel());
        // akhir spacer
        // tabbedPane.addTab("Kelola Ketua RT/RW",  new CrudKetuaRTRWPanel());
        // tabbedPane.addTab("Kelola Warga", new CrudWargaPanel());
        // tabbedPane.addTab("Kelola Kecamatan", new CrudKecamatanPanel());
        // tabbedPane.addTab("Kelola Desa", new CrudDesaPanel());

        // Footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.add(new JLabel("Copyright © 2025 Kabupaten Management System by Imron"));

        // Rakit ke main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(39, 82, 139));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // logo panel
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        BufferedImage circleImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 50, 50);
        g2.setClip(circle);
        g2.drawImage(scaledImage, 0, 0, null);
        g2.dispose();

        ImageIcon roundedIcon = new ImageIcon(circleImage);

        JLabel logoLabel = new JLabel(roundedIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        logoPanel.add(logoLabel, BorderLayout.WEST);

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
        btnLogout.addActionListener(e -> logout());

        JPanel rightPanel = new JPanel(new FlowLayout());
        rightPanel.setBackground(new Color(39, 82, 139));
        rightPanel.add(lblUser);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(btnLogout);

        panel.add(logoPanel, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createDummyPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private void setupFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
    }
}
