package com.kabupaten.view;

import com.kabupaten.dao.KabupatenDAO;
import com.kabupaten.model.kabupaten;
import com.kabupaten.model.kabupaten;
import com.kabupaten.view.*;
import com.kabupaten.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

/**
 * Frame utama aplikasi dengan fitur CRUD kabupaten
 */
public class dashboard_guest extends JFrame {
    private String currentUser;
    private KabupatenDAO kabupatenDAO;
    
    // Components
    private JTable tableKabupaten;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnLogout;
    private JLabel lblStatus, lblUser;
    
    public dashboard_guest(String username) {
        this.currentUser = username;
        this.kabupatenDAO = new KabupatenDAO();
        
        initComponents();
        setupFrame();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Sistem Pendataan Kabupaten - Selamat Datang, " + currentUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Toolbar panel
        JPanel toolbarPanel = createToolbarPanel();
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        
        // Status bar
        JPanel statusPanel = createStatusPanel();
        
        // Add to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(toolbarPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
        
        setupEventListeners();
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(39, 82, 139));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
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
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        panel.setLayout(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel lblSearch = new JLabel("Cari:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        searchPanel.add(lblSearch);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(txtSearch);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        btnAdd = createActionButton("Tambah", new Color(46, 125, 50), "add.png");
        btnEdit = createActionButton("Edit", new Color(255, 152, 0), "edit.png");
        btnDelete = createActionButton("Hapus", new Color(198, 40, 40), "delete.png");
        btnRefresh = createActionButton("Refresh", new Color(33, 150, 243), "refresh.png");
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(btnEdit);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(btnDelete);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(btnRefresh);
        
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JButton createActionButton(String text, Color bgColor, String iconName) {
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
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Table model
        String[] columns = {
            "ID", "Provinsi", "Kode", "Nama Kabupaten", "Ibukota", 
            "Luas (km²)", "Penduduk", "Kecamatan", "Desa", "Dibuat"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak dapat diedit langsung
            }
        };
        
        tableKabupaten = new JTable(tableModel);
        tableKabupaten.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tableKabupaten.setRowHeight(25);
        tableKabupaten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableKabupaten.getTableHeader().setReorderingAllowed(false);
        
        // Style table
        tableKabupaten.setShowGrid(true);
        tableKabupaten.setGridColor(new Color(230, 230, 230));
        tableKabupaten.getTableHeader().setBackground(new Color(39, 82, 139));
        tableKabupaten.getTableHeader().setForeground(Color.WHITE);
        tableKabupaten.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Set column widths
        tableKabupaten.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tableKabupaten.getColumnModel().getColumn(1).setPreferredWidth(120); // Provinsi
        tableKabupaten.getColumnModel().getColumn(2).setPreferredWidth(80);  // Kode
        tableKabupaten.getColumnModel().getColumn(3).setPreferredWidth(180); // Nama
        tableKabupaten.getColumnModel().getColumn(4).setPreferredWidth(120); // Ibukota
        tableKabupaten.getColumnModel().getColumn(5).setPreferredWidth(100); // Luas
        tableKabupaten.getColumnModel().getColumn(6).setPreferredWidth(100); // Penduduk
        tableKabupaten.getColumnModel().getColumn(7).setPreferredWidth(80);  // Kecamatan
        tableKabupaten.getColumnModel().getColumn(8).setPreferredWidth(80);  // Desa
        tableKabupaten.getColumnModel().getColumn(9).setPreferredWidth(120); // Dibuat
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tableKabupaten);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.setPreferredSize(new Dimension(0, 400));
        
        // Table title
        JLabel tableTitle = new JLabel("Data Kabupaten");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        panel.add(tableTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        lblStatus = new JLabel("Siap");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(100, 100, 100));
        
        panel.add(lblStatus, BorderLayout.WEST);
        
        return panel;
    }
    
    private void setupEventListeners() {
        // Search functionality
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                performSearch();
            }
        });
        
        // Button actions
        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelectedKabupaten());
        btnRefresh.addActionListener(e -> loadData());
        btnLogout.addActionListener(e -> logout());
        
        // Double click to edit
        tableKabupaten.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    openEditDialog();
                }
            }
        });
    }
    
    private void loadData() {
        SwingUtilities.invokeLater(() -> {
            lblStatus.setText("Memuat data...");
        });
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                tableModel.setRowCount(0);
                List<kabupaten> kabupatenList = kabupatenDAO.getAllKabupaten();
                
                for (kabupaten k : kabupatenList) {
                    Object[] row = {
                        k.getIdKabupaten(),
                        k.getNamaProvinsi(),
                        k.getKodeKabupaten(),
                        k.getNamaKabupaten(),
                        k.getIbukota(),
                        String.format("%.2f", k.getLuasWilayah()),
                        String.format("%,d", k.getJumlahPenduduk()),
                        k.getJumlahKecamatan(),
                        k.getJumlahDesa(),
                        k.getCreatedAt() != null ? k.getCreatedAt().toString().substring(0, 19) : ""
                    };
                    tableModel.addRow(row);
                }
                return null;
            }
            
            @Override
            protected void done() {
                lblStatus.setText("Data berhasil dimuat. Total: " + tableModel.getRowCount() + " kabupaten");
            }
        };
        
        worker.execute();
    }
    
    private void performSearch() {
        String keyword = txtSearch.getText().trim();
        
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            lblStatus.setText("Mencari...");
        });
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                tableModel.setRowCount(0);
                List<kabupaten> kabupatenList = kabupatenDAO.searchKabupaten(keyword);
                
                for (kabupaten k : kabupatenList) {
                    Object[] row = {
                        k.getIdKabupaten(),
                        k.getNamaProvinsi(),
                        k.getKodeKabupaten(),
                        k.getNamaKabupaten(),
                        k.getIbukota(),
                        String.format("%.2f", k.getLuasWilayah()),
                        String.format("%,d", k.getJumlahPenduduk()),
                        k.getJumlahKecamatan(),
                        k.getJumlahDesa(),
                        k.getCreatedAt() != null ? k.getCreatedAt().toString().substring(0, 19) : ""
                    };
                    tableModel.addRow(row);
                }
                return null;
            }
            
            @Override
            protected void done() {
                lblStatus.setText("Pencarian selesai. Ditemukan: " + tableModel.getRowCount() + " kabupaten");
            }
        };
        
        worker.execute();
    }
    
    private void openAddDialog() {
        guest_dashboard dialog = new guest_dashboard(this, "Tambah Kabupaten", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            loadData();
        }
    }
    
    private void openEditDialog() {
        int selectedRow = tableKabupaten.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data kabupaten yang akan diedit!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idKabupaten = (Integer) tableModel.getValueAt(selectedRow, 0);
        kabupaten kabupaten = kabupatenDAO.getKabupatenById(idKabupaten);
        
        if (kabupaten != null) {
            guest_dashboard dialog = new guest_dashboard(this, "Edit Kabupaten", kabupaten);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                loadData();
            }
        }
    }
    
    private void deleteSelectedKabupaten() {
        int selectedRow = tableKabupaten.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data kabupaten yang akan dihapus!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String namaKabupaten = (String) tableModel.getValueAt(selectedRow, 3);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data kabupaten:\n" + namaKabupaten + "?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int idKabupaten = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            if (kabupatenDAO.deleteKabupaten(idKabupaten)) {
                JOptionPane.showMessageDialog(this, 
                    "Data kabupaten berhasil dihapus!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menghapus data kabupaten!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin logout?", 
            "Konfirmasi Logout", 
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
        setMinimumSize(new Dimension(1200, 700));
        setLocationRelativeTo(null);
    }
}