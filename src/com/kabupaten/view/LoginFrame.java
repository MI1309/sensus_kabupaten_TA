package com.kabupaten.view;

import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import com.kabupaten.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.imageio.ImageIO;

/**
 * Frame untuk login aplikasi dengan validasi role
 * Guest bisa langsung login tanpa username/password
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnLogin;
    private JButton btnExit;
    private String userRole;
    private String fullName;
    
    // Komponen untuk toggle
    private JPanel usernamePanel;
    private JPanel passwordPanel;
    private JLabel lblUsername;
    private JLabel lblPassword;
    
    // Warna tema dari loading screen
    private static final Color PRIMARY_COLOR = new Color(30, 60, 114);    // Biru tua
    private static final Color SECONDARY_COLOR = new Color(42, 82, 152);  // Biru sedang
    private static final Color ACCENT_COLOR = new Color(79, 172, 254);    // Biru muda/cyan
    private static final Color CREAM_COLOR = new Color(255, 251, 245);    // Cream untuk background
    
    public LoginFrame() {
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        setTitle("Login - Sistem Pendataan Kabupaten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(CREAM_COLOR);
        
        // Panel kiri - Logo dan judul
        JPanel leftPanel = createLeftPanel();
        
        // Panel kanan - Form login
        JPanel rightPanel = createRightPanel();
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    // Class untuk panel kiri dengan gradient
    class GradientPatternPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Gradient background yang halus
            GradientPaint gradient = new GradientPaint(
                0, 0, PRIMARY_COLOR,
                getWidth(), getHeight(), SECONDARY_COLOR
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new GradientPatternPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(340, 600));
        
        // Container utama
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        
        // Panel untuk konten vertikal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Logo
        JPanel logoPanel = createLogoPanel();
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(logoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Title
        JLabel titleLabel = new JLabel("SISTEM");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel subtitleLabel = new JLabel("PENDATAAN KABUPATEN");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitleLabel.setForeground(new Color(220, 230, 255));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(255, 255, 255, 80));
        separator.setMaximumSize(new Dimension(200, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(separator);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Description
        JLabel descLabel = new JLabel("<html><div style='text-align: center; padding: 0 15px;'>"
            + "Aplikasi Pengelolaan Data<br>"
            + "Wilayah Administratif Terpadu"
            + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(220, 230, 255));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(descLabel);
        
        // Spacer untuk push footer ke bawah
        contentPanel.add(Box.createVerticalGlue());
        
        // Footer dengan version
        JLabel versionLabel = new JLabel("v1.0.0 • © 2025");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(180, 200, 255));
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(versionLabel);
        
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        panel.add(mainContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                int size = 130;
                int x = (getWidth() - size) / 2;
                int y = 0;
                
                try {
                    // Load logo
                    BufferedImage img = ImageIO.read(
                        getClass().getResource("/com/kabupaten/img/logo.jpg")
                    );
                    
                    // Shadow effect
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(x + 4, y + 4, size, size, 20, 20);
                    
                    // Main square with accent color
                    g2.setColor(ACCENT_COLOR);
                    g2.fillRoundRect(x, y, size, size, 20, 20);
                    
                    // White border
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.setColor(Color.WHITE);
                    g2.drawRoundRect(x, y, size, size, 20, 20);
                    
                    if (img != null) {
                        // Draw logo with padding
                        int padding = 14;
                        int imgSize = size - (padding * 2);
                        g2.drawImage(img, x + padding, y + padding, imgSize, imgSize, null);
                    } else {
                        // Fallback text
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                        String text = "SK";
                        FontMetrics fm = g2.getFontMetrics();
                        int textX = x + (size - fm.stringWidth(text)) / 2;
                        int textY = y + (size + fm.getAscent()) / 2 - 8;
                        g2.drawString(text, textX, textY);
                    }
                    
                } catch (Exception e) {
                    // Fallback design
                    g2.setColor(ACCENT_COLOR);
                    g2.fillRoundRect(x, y, size, size, 20, 20);
                    
                    g2.setStroke(new BasicStroke(2.5f));
                    g2.setColor(Color.WHITE);
                    g2.drawRoundRect(x, y, size, size, 20, 20);
                    
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                    g2.setColor(Color.WHITE);
                    FontMetrics fm = g2.getFontMetrics();
                    String text = "LOGO";
                    int textX = x + (size - fm.stringWidth(text)) / 2;
                    int textY = y + (size + fm.getAscent()) / 2 - 8;
                    g2.drawString(text, textX, textY);
                }
            }
        };
        
        panel.setPreferredSize(new Dimension(150, 150));
        panel.setMaximumSize(new Dimension(150, 150));
        panel.setOpaque(false);
        
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CREAM_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Container untuk form
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(CREAM_COLOR);
        
        // Panel form dengan padding yang tepat
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        formPanel.setPreferredSize(new Dimension(480, 600));
        
        // Header section
        JLabel welcomeLabel = new JLabel("Selamat Datang");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(welcomeLabel);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel descLabel = new JLabel("Silakan login untuk mengakses sistem");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 120));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descLabel);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Username field
        lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setForeground(PRIMARY_COLOR);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblUsername);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.setBackground(Color.WHITE);
        usernamePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        usernamePanel.setMaximumSize(new Dimension(380, 50));
        usernamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createEmptyBorder());
        txtUsername.setBackground(Color.WHITE);
        usernamePanel.add(txtUsername, BorderLayout.CENTER);
        
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password field
        lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(PRIMARY_COLOR);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblPassword);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        passwordPanel.setMaximumSize(new Dimension(380, 50));
        passwordPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createEmptyBorder());
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setEchoChar('•');
        passwordPanel.add(txtPassword, BorderLayout.CENTER);
        
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Role selection
        JLabel lblRole = new JLabel("Login Sebagai");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRole.setForeground(PRIMARY_COLOR);
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblRole);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel comboPanel = new JPanel(new BorderLayout());
        comboPanel.setBackground(Color.WHITE);
        comboPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        comboPanel.setMaximumSize(new Dimension(380, 48));
        comboPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] roles = {"Admin", "Guest", "Bupati"};
        cmbRole = new JComboBox<>(roles);
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRole.setBackground(Color.WHITE);
        cmbRole.setBorder(BorderFactory.createEmptyBorder());
        cmbRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        comboPanel.add(cmbRole, BorderLayout.CENTER);
        formPanel.add(comboPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnLogin = createStyledButton("MASUK", ACCENT_COLOR);
        btnExit = createStyledButton("KELUAR", new Color(120, 120, 120));
        
        btnLogin.setPreferredSize(new Dimension(150, 48));
        btnExit.setPreferredSize(new Dimension(150, 48));
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        
        formPanel.add(buttonPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Footer
        JLabel footerLabel = new JLabel("Sistem Pendataan Kabupaten v1.0");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footerLabel.setForeground(new Color(150, 150, 150));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(footerLabel);
        
        formContainer.add(formPanel);
        panel.add(formContainer, BorderLayout.CENTER);
        
        setupActionListeners();
        
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker().darker(), 1),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
        });
        
        return button;
    }

    private void setupActionListeners() {
        btnLogin.addActionListener(e -> performLogin());
        btnExit.addActionListener(e -> exit());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());
        txtPassword.addActionListener(e -> performLogin());
        
        cmbRole.addActionListener(e -> toggleCredentialFields());
    }
    
    // Method untuk show/hide username dan password field
    private void toggleCredentialFields() {
        String selectedRole = (String) cmbRole.getSelectedItem();
        boolean isGuest = "Guest".equals(selectedRole);
        
        lblUsername.setVisible(!isGuest);
        usernamePanel.setVisible(!isGuest);
        lblPassword.setVisible(!isGuest);
        passwordPanel.setVisible(!isGuest);
        
        if (isGuest) {
            txtUsername.setText("");
            txtPassword.setText("");
        }
        
        revalidate();
        repaint();
    }

    private void performLogin() {
        String selectedRole = ((String) cmbRole.getSelectedItem()).toLowerCase();

        // Guest langsung login tanpa validasi
        if ("guest".equalsIgnoreCase(selectedRole)) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new guest_dashboard().setVisible(true));
            return;
        }

        // Validasi Admin dan Bupati
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan password tidak boleh kosong!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validateLogin(username, password, selectedRole)) {
            if (userRole != null && userRole.equalsIgnoreCase(selectedRole)) {
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    if (userRole.equalsIgnoreCase("admin")) {
                        new dashboard_admin(fullName).setVisible(true);
                    } else if (userRole.equalsIgnoreCase("bupati")) {
                        new dashboard_bupati(fullName).setVisible(true);
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this,
                    "Role yang dipilih tidak sesuai!",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Username atau password salah!",
                "Login Gagal",
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }

    private boolean validateLogin(String username, String password, String selectedRole) {
        String hashedPassword = hashPassword(password);
        String sql = "SELECT role, nama_lengkap FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userRole = rs.getString("role");
                    fullName = rs.getString("nama_lengkap");
                    
                    if (fullName == null || fullName.trim().isEmpty()) {
                        fullName = username;
                    }
                    
                    return true;
                }
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error validating login: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error koneksi database: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private String hashPassword(String plain) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(plain.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return plain;
        }
    }

    private void exit() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin Keluar?", 
            "Konfirmasi Keluar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void setupFrame() {
        pack();
        setLocationRelativeTo(null);
        setSize(900, 600);
        setMinimumSize(new Dimension(900, 600));
        
        SwingUtilities.invokeLater(() -> {
            txtUsername.requestFocus();
            toggleCredentialFields(); // Initialize visibility
        });
    }
}