// ============================================
// 2. DAO: UserDAO.java
// ============================================


// ============================================
// 3. SERVICE: UserService.java
// ============================================


// ============================================
// 4. VIEW: CrudUserPanel.java
// ============================================
package com.kabupaten.view;

import com.kabupaten.model.User;
import com.kabupaten.services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CrudUserPanel extends JPanel {
    private UserService service = new UserService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public CrudUserPanel() {
        setLayout(new BorderLayout());

        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pencarian:"));
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Cari");
        JButton btnRefresh = new JButton("Refresh");
        
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        add(searchPanel, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Username", "Nama Lengkap", "Email", "Role", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel tombol CRUD
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnDetail = new JButton("Detail");
        JButton btnResetPassword = new JButton("Reset Password");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnDetail);
        buttonPanel.add(btnResetPassword);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnDetail.addActionListener(e -> showDetail());
        btnResetPassword.addActionListener(e -> resetPassword());
        btnSearch.addActionListener(e -> searchData());
        btnRefresh.addActionListener(e -> refreshTable());
        
        // Enter key untuk search
        txtSearch.addActionListener(e -> searchData());
        
        // Load initial data
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        
        try {
            for (User user : service.getAllUsers()) {
                tableModel.addRow(new Object[]{
                    user.getIdUser(),
                    user.getUsername(),
                    user.getNamaLengkap() != null ? user.getNamaLengkap() : "-",
                    user.getEmail() != null ? user.getEmail() : "-",
                    user.getRole() != null ? user.getRole() : "-",
                    user.getStatus() != null ? user.getStatus() : "aktif"
                });
            }
            
            // Clear search text
            txtSearch.setText("");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error memuat data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void searchData() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }
        
        tableModel.setRowCount(0);
        
        try {
            for (User user : service.searchUsers(keyword)) {
                tableModel.addRow(new Object[]{
                    user.getIdUser(),
                    user.getUsername(),
                    user.getNamaLengkap() != null ? user.getNamaLengkap() : "-",
                    user.getEmail() != null ? user.getEmail() : "-",
                    user.getRole() != null ? user.getRole() : "-",
                    user.getStatus() != null ? user.getStatus() : "aktif"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error pencarian: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void tambahData() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField txtUsername = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();
        JTextField txtNamaLengkap = new JTextField();
        JTextField txtEmail = new JTextField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"admin", "operator", "guest"});
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"aktif", "nonaktif"});

        panel.add(new JLabel("Username:*"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password:*"));
        panel.add(txtPassword);
        panel.add(new JLabel("Konfirmasi Password:*"));
        panel.add(txtConfirmPassword);
        panel.add(new JLabel("Nama Lengkap:*"));
        panel.add(txtNamaLengkap);
        panel.add(new JLabel("Email:*"));
        panel.add(txtEmail);
        panel.add(new JLabel("Role:*"));
        panel.add(cmbRole);
        panel.add(new JLabel("Status:*"));
        panel.add(cmbStatus);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Tambah User",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                String namaLengkap = txtNamaLengkap.getText().trim();
                String email = txtEmail.getText().trim();
                String role = (String) cmbRole.getSelectedItem();
                String status = (String) cmbStatus.getSelectedItem();

                // Validasi
                if (username.isEmpty() || password.isEmpty() || namaLengkap.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Semua field wajib diisi!", 
                        "Peringatan", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, 
                        "Password dan Konfirmasi Password tidak sama!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(this, 
                        "Password minimal 6 karakter!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validasi email format
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(this, 
                        "Format email tidak valid!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = service.addUser(username, password, namaLengkap, email, role, status);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "User berhasil ditambahkan!");
                    refreshTable();
                    txtSearch.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menambahkan user! Username atau email sudah digunakan.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void hapusData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih user yang akan dihapus!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus user ini?\nData yang sudah dihapus tidak dapat dikembalikan!",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (Integer) table.getValueAt(selectedRow, 0);
            try {
                boolean success = service.deleteUser(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
                    refreshTable();
                    txtSearch.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menghapus user!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void resetPassword() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih user untuk reset password!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        String username = (String) table.getValueAt(selectedRow, 1);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JPasswordField txtNewPassword = new JPasswordField();
        JPasswordField txtConfirmPassword = new JPasswordField();
        
        panel.add(new JLabel("Password Baru:*"));
        panel.add(txtNewPassword);
        panel.add(new JLabel("Konfirmasi Password:*"));
        panel.add(txtConfirmPassword);
        
        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Reset Password untuk: " + username,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String newPassword = new String(txtNewPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                
                if (newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Password tidak boleh kosong!", 
                        "Peringatan", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, 
                        "Password dan Konfirmasi Password tidak sama!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (newPassword.length() < 6) {
                    JOptionPane.showMessageDialog(this, 
                        "Password minimal 6 karakter!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean success = service.resetPassword(id, newPassword);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Password berhasil direset!");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal mereset password!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void showDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih user untuk melihat detail!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        User user = service.getUserById(id);
        
        if (user != null) {
            String detail = String.format(
                "=== DETAIL USER ===\n\n" +
                "ID: %d\n" +
                "Username: %s\n" +
                "Nama Lengkap: %s\n" +
                "Email: %s\n" +
                "Role: %s\n" +
                "Status: %s\n" +
                "Last Login: %s\n" +
                "Dibuat: %s\n" +
                "Diupdate: %s",
                user.getIdUser(),
                user.getUsername(),
                user.getNamaLengkap() != null ? user.getNamaLengkap() : "-",
                user.getEmail() != null ? user.getEmail() : "-",
                user.getRole() != null ? user.getRole() : "-",
                user.getStatus() != null ? user.getStatus() : "-",
                user.getLastLogin() != null ? user.getLastLogin() : "Belum pernah login",
                user.getCreatedAt(),
                user.getUpdatedAt()
            );
            
            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Detail User", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "User tidak ditemukan!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }



    private void editData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih user yang akan diedit!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        User user = service.getUserById(id);
        
        if (user == null) {
            JOptionPane.showMessageDialog(this, 
                "User tidak ditemukan!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField txtUsername = new JTextField(user.getUsername());
        JTextField txtNamaLengkap = new JTextField(user.getNamaLengkap() != null ? user.getNamaLengkap() : "");
        JTextField txtEmail = new JTextField(user.getEmail() != null ? user.getEmail() : "");
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"admin", "operator", "guest"});
        cmbRole.setSelectedItem(user.getRole());
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"aktif", "nonaktif"});
        cmbStatus.setSelectedItem(user.getStatus());

        panel.add(new JLabel("Username:*"));
        panel.add(txtUsername);
        panel.add(new JLabel("Nama Lengkap:*"));
        panel.add(txtNamaLengkap);
        panel.add(new JLabel("Email:*"));
        panel.add(txtEmail);
        panel.add(new JLabel("Role:*"));
        panel.add(cmbRole);
        panel.add(new JLabel("Status:*"));
        panel.add(cmbStatus);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Edit User",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String username = txtUsername.getText().trim();
                String namaLengkap = txtNamaLengkap.getText().trim();
                String email = txtEmail.getText().trim();
                String role = (String) cmbRole.getSelectedItem();
                String status = (String) cmbStatus.getSelectedItem();

                // Validasi
                if (username.isEmpty() || namaLengkap.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Semua field wajib diisi!", 
                        "Peringatan", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validasi email format
                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(this, 
                        "Format email tidak valid!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = service.updateUser(id, username, namaLengkap, email, role, status);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "User berhasil diupdate!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal mengupdate user! Username atau email sudah digunakan.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}