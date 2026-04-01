package com.kabupaten.view;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.utils.SecurityUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Frame untuk reset password pengguna
 */
public class ForgotPasswordFrame extends JFrame {
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnReset;
    private JButton btnBack;
    private JButton btnSave;

    private static final Color PRIMARY_COLOR = new Color(30, 60, 114);
    private static final Color ACCENT_COLOR = new Color(79, 172, 254);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color CREAM_COLOR = new Color(255, 251, 245);

    public ForgotPasswordFrame() {
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        setTitle("Reset Password - Sistem Pendataan Kabupaten");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(CREAM_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Lupa Password?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel descLabel = new JLabel("Masukkan data Anda untuk mereset password");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(100, 100, 120));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(descLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Input Fields
        txtUsername = createInputField(formPanel, "Username *");
        txtEmail = createInputField(formPanel, "Email *");
        txtFullName = createInputField(formPanel, "Nama Lengkap *");
        txtNewPassword = createPasswordField(formPanel, "Password Baru *");
        txtConfirmPassword = createPasswordField(formPanel, "Konfirmasi Password *");

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        btnBack = new JButton("KEMBALI");
        styleButton(btnBack, new Color(120, 120, 120), 120);

        btnSave = new JButton("SIMPAN PASSWORD");
        styleButton(btnSave, SUCCESS_COLOR, 180);

        btnReset = new JButton("RESET PASSWORD");
        styleButton(btnReset, ACCENT_COLOR, 180);

        buttonPanel.add(btnBack);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnReset);

        formPanel.add(buttonPanel);
        mainPanel.add(formPanel);
        add(mainPanel);

        setupListeners();
    }

    private void styleButton(JButton btn, Color color, int width) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(width, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JTextField createInputField(JPanel parent, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(PRIMARY_COLOR);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(300, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(field);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
        return field;
    }

    private JPasswordField createPasswordField(JPanel parent, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(PRIMARY_COLOR);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));

        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(300, 35));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(field);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
        return field;
    }

    private void setupListeners() {
        btnBack.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });

        btnSave.addActionListener(e -> handleSavePassword());
        btnReset.addActionListener(e -> handleResetPassword());
    }

    // ✅ VALIDASI & PREVIEW PASSWORD
private void handleSavePassword() {
    String username = txtUsername.getText().trim();
    String email = txtEmail.getText().trim();
    String fullName = txtFullName.getText().trim();
    String newPass = new String(txtNewPassword.getPassword());
    String confirmPass = new String(txtConfirmPassword.getPassword());

    // Validasi
    if (username.isEmpty() || email.isEmpty() || fullName.isEmpty() || newPass.isEmpty()) {
        showWarning("Semua field wajib diisi!");
        return;
    }

    if (newPass.length() < 6) {
        showError("Password minimal 6 karakter!");
        return;
    }

    if (!newPass.equals(confirmPass)) {
        showError("Konfirmasi password tidak cocok!");
        return;
    }

    // 🔥 SIMPAN KE DATABASE
    String result = resetPasswordInDatabase(username, email, fullName, newPass);

    if (result.equals("SUCCESS")) {
        JOptionPane.showMessageDialog(this,
                "Password berhasil disimpan!",
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
    } else {
        showError(result);
    }
}

    // ✅ RESET + UPDATE DATABASE
    private void handleResetPassword() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String fullName = txtFullName.getText().trim();
        String newPass = new String(txtNewPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if (username.isEmpty() || email.isEmpty() || fullName.isEmpty() || newPass.isEmpty()) {
            showWarning("Semua field wajib diisi!");
            return;
        }

        if (newPass.length() < 6) {
            showError("Password minimal 6 karakter!");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showError("Password tidak cocok!");
            return;
        }

        String result = resetPasswordInDatabase(username, email, fullName, newPass);

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this,
                    "Password berhasil diubah!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new LoginFrame().setVisible(true);
        } else {
            showError(result);
        }
    }

    private String resetPasswordInDatabase(String username, String email, String fullName, String newPass) {
        String checkSql = "SELECT id_user FROM users WHERE username=? AND nama_lengkap=? AND email=?";
        String updateSql = "UPDATE users SET password=? WHERE id_user=?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setString(1, username);
            check.setString(2, fullName);
            check.setString(3, email);

            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                return "Data tidak cocok!";
            }

            int userId = rs.getInt("id_user");
            String hashedPassword = SecurityUtils.hashPassword(newPass);

            PreparedStatement update = conn.prepareStatement(updateSql);
            update.setString(1, hashedPassword);
            update.setInt(2, userId);

            int rows = update.executeUpdate();
            return rows > 0 ? "SUCCESS" : "Gagal update password";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void setupFrame() {
        pack();
        setSize(500, 550);
        setLocationRelativeTo(null);
    }
}