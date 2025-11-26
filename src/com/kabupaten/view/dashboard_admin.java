package com.kabupaten.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class dashboard_admin extends JFrame {
    private String currentFullName;  // Full Name untuk ditampilkan
    private JButton btnLogout;
    private JLabel lblUser;
    private JLabel lblWelcome;

    // PERBAIKAN: Constructor menerima 2 parameter (fullName dan username)
    public dashboard_admin(String fullName) {
        this.currentFullName = fullName;

        initComponents();
        setupFrame();
    }

    private void initComponents() {
        // PERBAIKAN: Tampilkan fullName di title
        setTitle("Dashboard Admin - " + currentFullName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color bgColor = new Color(245, 245, 245);
        Color primaryColor = new Color(39, 82, 139);
        Color accentColor = new Color(74, 144, 226);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);

        // Header panel (REDESIGNED)
        JPanel headerPanel = createModernHeaderPanel();

        // Tabbed pane (IMPROVED)
        JTabbedPane tabbedPane = createModernTabbedPane();

        // Footer panel (ENHANCED)
        JPanel footerPanel = createModernFooterPanel();

        // Rakit ke main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    // ============================================
    // MODERN HEADER PANEL (REDESIGNED)
    // ============================================
    private JPanel createModernHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(39, 82, 139));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(74, 144, 226)),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        

        // LEFT SECTION: Logo + Title
        JPanel leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftSection.setOpaque(false);

        // Logo (Circular)
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        BufferedImage circleImage = new BufferedImage(55, 55, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 55, 55);
        g2.setClip(circle);
        g2.drawImage(scaledImage, 0, 0, null);
        
        // Add border to logo
        g2.setClip(null);
        g2.setColor(new Color(255, 255, 255, 150));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Ellipse2D.Double(0, 0, 55, 55));
        g2.dispose();
        ImageIcon roundedIcon = new ImageIcon(circleImage);

        JLabel logoLabel = new JLabel(roundedIcon);
        leftSection.add(logoLabel);

        // Title + Subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("SISTEM PENDATAAN KABUPATEN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Dashboard Administrator");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 220, 250));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(subtitleLabel);

        leftSection.add(titlePanel);

        // RIGHT SECTION: User Info + Logout
        JPanel rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightSection.setOpaque(false);

        // User Card (NEW DESIGN)
        JPanel userCard = new JPanel();
        userCard.setLayout(new BoxLayout(userCard, BoxLayout.Y_AXIS));
        userCard.setBackground(new Color(255, 255, 255, 25));
        userCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // PERBAIKAN: Tampilkan Full Name
        lblWelcome = new JLabel("Selamat Datang,");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblWelcome.setForeground(new Color(200, 220, 250));
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblUser = new JLabel(currentFullName);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(Color.WHITE);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblRole = new JLabel("● Administrator");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRole.setForeground(new Color(144, 238, 144));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        userCard.add(lblWelcome);
        userCard.add(Box.createVerticalStrut(2));
        userCard.add(lblUser);
        userCard.add(Box.createVerticalStrut(2));
        userCard.add(lblRole);

        rightSection.add(userCard);

        // Modern Logout Button
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(200, 35, 51));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(220, 53, 69));
            }
        });
        
        btnLogout.addActionListener(e -> logout());
        rightSection.add(btnLogout);

        headerPanel.add(leftSection, BorderLayout.WEST);
        headerPanel.add(rightSection, BorderLayout.EAST);

        return headerPanel;
    }

    // ============================================
    // MODERN TABBED PANE (IMPROVED)
    // ============================================
    private JTabbedPane createModernTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        
        // Modern look
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.setBackground(new Color(245, 245, 245));
        tabbedPane.setForeground(new Color(33, 37, 41));
        
        // Custom UI
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement,
                                            int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    // Selected tab - gradient blue
                    GradientPaint gp = new GradientPaint(x, y, new Color(39, 82, 139), x, y + h, new Color(74, 144, 226));
                    g2.setPaint(gp);
                } else {
                    // Unselected tab - light gray
                    g2.setColor(new Color(230, 235, 240));
                }
                g2.fillRoundRect(x + 2, y + 2, w - 4, h - 4, 8, 8);
            }

            @Override
            protected void paintText(Graphics g, int tabPlacement, Font font,
                                    FontMetrics metrics, int tabIndex,
                                    String title, Rectangle textRect,
                                    boolean isSelected) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                if (isSelected) {
                    g2.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
                    g2.setColor(new Color(73, 80, 87));
                }

                // Add icon before text
                String icon = getTabIcon(tabIndex);
                String fullText = icon + " " + title;
                
                g2.drawString(fullText, textRect.x - 5, textRect.y + metrics.getAscent());
            }
            
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                return 180; // Fixed width for consistency
            }

            @Override
            protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
                return 50; // Taller tabs
            }
        });

        // Add tabs with icons
        tabbedPane.addTab("Kecamatan", new CrudKecamatanPanel());
        tabbedPane.addTab("Desa", new CrudDesaPanel());
        // tabbedPane.addTab("RT/RW", new CrudRTRWPanel());
        // tabbedPane.addTab("Ketua RT/RW", new CrudKetuaRTRWPanel());
        tabbedPane.addTab("Warga", new CrudWargaPanel());

        return tabbedPane;
    }

    // Helper method for tab icons
    private String getTabIcon(int index) {
        switch (index) {
            case 0: return "🏙️";  // Kecamatan
            case 1: return "🏡";  // Desa
            // case 2: return "🏘️";  // RT/RW
            // case 2: return "👤";  // Ketua
            case 2: return "👥";  // Warga
            default: return "📋";
        }
    }

    // ============================================
    // MODERN FOOTER PANEL (ENHANCED)
    // ============================================
    private JPanel createModernFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(230, 235, 240)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Left side - Copyright
        JLabel lblCopyright = new JLabel("© 2025 Kabupaten Management System");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(new Color(108, 117, 125));

        // Right side - Developer info
        JLabel lblDeveloper = new JLabel("Developed by Imron");
        lblDeveloper.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDeveloper.setForeground(new Color(108, 117, 125));

        // Center - Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        statusPanel.setOpaque(false);
        
        JLabel lblStatus = new JLabel("● System Active");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(40, 167, 69));
        
        JLabel lblVersion = new JLabel("v1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(108, 117, 125));
        
        statusPanel.add(lblStatus);
        statusPanel.add(new JLabel("|"));
        statusPanel.add(lblVersion);

        footerPanel.add(lblCopyright, BorderLayout.WEST);
        footerPanel.add(statusPanel, BorderLayout.CENTER);
        footerPanel.add(lblDeveloper, BorderLayout.EAST);

        return footerPanel;
    }

    // ============================================
    // LOGOUT FUNCTION
    // ============================================
    private void logout() {
        // Custom confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar dari sistem?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Add fade out effect (optional)
            this.dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    // ============================================
    // SETUP FRAME
    // ============================================
    private void setupFrame() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 600));
        setLocationRelativeTo(null);
        
        // Set window icon (optional)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Could not load window icon: " + e.getMessage());
        }
    }

    public String getCurrentFullName() {
        return currentFullName;
    }
}