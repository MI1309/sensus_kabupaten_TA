package com.kabupaten.view;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import com.kabupaten.database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Frame untuk login aplikasi dengan validasi role
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole; // TAMBAHAN: ComboBox untuk memilih role
    private JButton btnLogin;
    private JButton btnExit;
    private String userRole; // Menyimpan role user dari database
    private String fullName; // Menyimpan nama lengkap user

    public LoginFrame() {
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        setTitle("Login - Aplikasi Pendataan Kabupaten Sidoarjo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main panel dengan gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226),
                    0, getHeight(), new Color(39, 82, 139)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Login panel
        JPanel loginPanel = createLoginPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(40, 40, 40, 40);

        mainPanel.add(loginPanel, gbc);
        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel shadowPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 30, 30);
                g2.dispose();
            }
        };
        shadowPanel.setOpaque(false);
        shadowPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Logo dan Title di satu baris, center
        JPanel logoTitlePanel = new JPanel(new BorderLayout());
        logoTitlePanel.setOpaque(false);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/com/kabupaten/img/logo.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        BufferedImage circleImage = new BufferedImage(70, 70, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Buat masking lingkaran
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 70, 70);
        g2.setClip(circle);

        // Gambar image ke dalam lingkaran
        g2.drawImage(scaledImage, 0, 0, null);
        g2.dispose();

        // Buat ImageIcon dari hasil cropping
        ImageIcon roundedIcon = new ImageIcon(circleImage);

        JLabel logoLabel = new JLabel(roundedIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("SISTEM PENDATAAN KABUPATEN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(39, 82, 139));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        logoTitlePanel.add(logoLabel, BorderLayout.NORTH);
        logoTitlePanel.add(titleLabel, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(-10, 0, 25, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(logoTitlePanel, gbc);

        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUsername.setForeground(new Color(39, 82, 139));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblUsername, gbc);

        JPanel usernamePanel = new JPanel(new BorderLayout());
        usernamePanel.setBackground(Color.WHITE);
        usernamePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        usernamePanel.add(txtUsername, BorderLayout.CENTER);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(usernamePanel, gbc);

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblPassword.setForeground(new Color(39, 82, 139));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(lblPassword, gbc);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        passwordPanel.add(txtPassword, BorderLayout.CENTER);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(passwordPanel, gbc);

        // TAMBAHAN: Role ComboBox
        JLabel lblRole = new JLabel("Login Sebagai");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRole.setForeground(new Color(39, 82, 139));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(lblRole, gbc);

        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBackground(Color.WHITE);
        rolePanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        
        // ComboBox dengan opsi role
        String[] roles = {"Admin", "Guest","operator"};
        cmbRole = new JComboBox<>(roles);
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbRole.setBackground(Color.WHITE);
        cmbRole.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        cmbRole.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Styling ComboBox
        cmbRole.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                         int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    setBackground(new Color(74, 144, 226));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });
        
        rolePanel.add(cmbRole, BorderLayout.CENTER);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(rolePanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(Color.WHITE);

        btnLogin = createStyledButton("LOGIN", new Color(46, 125, 50));
        btnExit = createStyledButton("KELUAR", new Color(198, 40, 40));

        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnExit.setPreferredSize(new Dimension(120, 40));

        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 15));

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(buttonPanel, gbc);

        shadowPanel.add(panel, BorderLayout.CENTER);

        setupActionListeners();

        return shadowPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

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
        btnLogin.addActionListener(e -> performLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());
        btnExit.addActionListener(e -> exit());
        txtPassword.addActionListener(e -> performLogin());
    }

    // Route page berdasarkan role dengan validasi ganda
    private void performLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String selectedRole = ((String) cmbRole.getSelectedItem()).toLowerCase(); // TAMBAHAN: Ambil role dari combobox

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan password tidak boleh kosong!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // PERBAIKAN: Validasi login dengan role yang dipilih
        if (validateLogin(username, password, selectedRole)) {
            // Cek apakah role dari database sesuai dengan role yang dipilih
            if (userRole != null && userRole.equalsIgnoreCase(selectedRole)) {
                // Role cocok, lanjutkan login
                if (userRole.equalsIgnoreCase("admin")) {
                    this.dispose();
                    SwingUtilities.invokeLater(() -> {
                        new dashboard_admin(fullName).setVisible(true);
                    });
                } else if (userRole.equalsIgnoreCase("guest")) {
                    this.dispose();
                    SwingUtilities.invokeLater(() -> {
                        new dashboard_guest(fullName).setVisible(true);
                    });
                }
            } else {
                // Role tidak cocok
                JOptionPane.showMessageDialog(this,
                    "Role yang dipilih tidak sesuai!\n" +
                    "Anda memilih: " + selectedRole.toUpperCase() + "\n" +
                    "Role sebenarnya: " + (userRole != null ? userRole.toUpperCase() : "Tidak diketahui"),
                    "Login Gagal - Role Tidak Cocok",
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
            txtUsername.setText("");
            txtUsername.requestFocus();
        }
    }

    // PERBAIKAN: Validasi login dengan parameter role
    private boolean validateLogin(String username, String password, String selectedRole) {
        // Query untuk validasi username, password, dan role
        String sql = "SELECT role, nama_lengkap FROM users WHERE username = ? AND password = ? AND is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Ambil role dan fullName dari database
                    userRole = rs.getString("role");
                    fullName = rs.getString("nama_lengkap");
                    
                    // Jika nama_lengkap null atau kosong, gunakan username sebagai fallback
                    if (fullName == null || fullName.trim().isEmpty()) {
                        fullName = username;
                    }
                    
                    // VALIDASI GANDA: Cek apakah role dari database sama dengan role yang dipilih
                    boolean roleMatch = userRole != null && userRole.equalsIgnoreCase(selectedRole);
                    
                    System.out.println("=== LOGIN VALIDATION ===");
                    System.out.println("Username: " + username);
                    System.out.println("Full Name: " + fullName);
                    System.out.println("Role dari Database: " + userRole);
                    System.out.println("Role yang Dipilih: " + selectedRole);
                    System.out.println("Role Match: " + roleMatch);
                    System.out.println("========================");
                    
                    return true; // Username dan password benar, validasi role dilakukan di performLogin()
                }
                return false; // Username atau password salah
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

    private void exit() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin Keluar?", 
            "Konfirmasi Keluar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                System.exit(0);
            });
        }
    }

    private void setupFrame() {
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(getSize());
        SwingUtilities.invokeLater(() -> txtUsername.requestFocus());
    }
}