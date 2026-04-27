package com.kabupaten.view;

import com.kabupaten.model.Fasilitas;
import com.kabupaten.dao.FasilitasDAO;
import com.kabupaten.listener.DataChangeListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import com.kabupaten.Dialog.FasilitasFormDialog;

/**
 * Panel CRUD untuk data Fasilitas
 */
public class CrudFasilitas extends JPanel {
    private FasilitasDAO fasilitasDAO = new FasilitasDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbJenisFilter;
    private JButton btnTambah, btnEdit, btnHapus, btnDetail;

    // Listener untuk notifikasi perubahan data
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public CrudFasilitas() {
        setLayout(new BorderLayout());

        // Panel pencarian dan filter
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 247, 250));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchPanel.add(new JLabel("Jenis Fasilitas:"));
        cmbJenisFilter = new JComboBox<>();
        cmbJenisFilter.addItem("-- Semua --");
        cmbJenisFilter.addItem("🏥 Kesehatan");
        cmbJenisFilter.addItem("📚 Pendidikan");
        cmbJenisFilter.addItem("🕌 Ibadah");
        cmbJenisFilter.addItem("🏟️ Olahraga");
        cmbJenisFilter.addItem("🏢 Pemerintahan");
        cmbJenisFilter.addItem("🛒 Perekonomian");
        cmbJenisFilter.addItem("🚌 Transportasi");
        cmbJenisFilter.addItem("🌿 Lingkungan");
        cmbJenisFilter.addItem("📡 Telekomunikasi");
        cmbJenisFilter.addItem("⚡ Energi");
        cmbJenisFilter.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        searchPanel.add(cmbJenisFilter);

        searchPanel.add(new JLabel("  Pencarian:"));
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 13));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 228), 1, true),
                BorderFactory.createEmptyBorder(7, 11, 7, 11)));
        searchPanel.add(txtSearch);

        // Tombol Cari
        JButton btnCari = new JButton("🔍 Cari");
        btnCari.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnCari.setBackground(new Color(30, 60, 114));
        btnCari.setForeground(Color.WHITE);
        btnCari.setBorder(BorderFactory.createEmptyBorder(7, 15, 7, 15));
        btnCari.setFocusPainted(false);
        btnCari.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCari.addActionListener(e -> searchData());
        searchPanel.add(btnCari);

        add(searchPanel, BorderLayout.NORTH);

        // Tabel data Fasilitas
        tableModel = new DefaultTableModel(
                new Object[] { "No", "Foto", "Nama Fasilitas", "Jenis", "Dinas Terkait", "Alamat", "Keterangan", "ID_HIDDEN" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(85); // Tinggi baris untuk menampilkan gambar
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 240, 250));
                table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        label.setOpaque(true);
        return label;
    }
});

        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Kolom Foto
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);
        table.getColumnModel().getColumn(6).setPreferredWidth(180);

        // Hide ID_HIDDEN column
        table.getColumnModel().getColumn(7).setMinWidth(0);
        table.getColumnModel().getColumn(7).setMaxWidth(0);
        table.getColumnModel().getColumn(7).setPreferredWidth(0);

        // Custom renderer untuk kolom gambar
        table.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        // Center alignment untuk No
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        add(scrollPane, BorderLayout.CENTER);

        // Panel tombol CRUD
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnTambah = createStyledButton("➕ Tambah", new Color(46, 125, 50));
        btnEdit = createStyledButton("✏️ Edit", new Color(33, 150, 243));
        btnHapus = createStyledButton("🗑️ Hapus", new Color(244, 67, 54));
        btnDetail = createStyledButton("📋 Detail", new Color(0, 128, 128));

        btnTambah.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnEdit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnHapus.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnDetail.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        
        // Export Button
        JButton btnExport = createStyledButton("📊 Export Data", new Color(40, 167, 69));
        btnExport.addActionListener(e -> exportToCSV());

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnDetail);
        buttonPanel.add(btnExport);

        // UI Polish - Table Header
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setSelectionBackground(new Color(79, 172, 254, 100));
        table.setSelectionForeground(new Color(30, 60, 114));
        table.setShowGrid(true);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnDetail.addActionListener(e -> showDetail());

        // Live search
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchData();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchData();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchData();
            }
        });

        // Filter listeners
        cmbJenisFilter.addActionListener(e -> searchData());

        // Load initial data
        refreshTable();
    }

    // ============================================================
    // HELPER: Create Styled Button
    // ============================================================
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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

    // ============================================================
    // LISTENER MANAGEMENT
    // ============================================================
    public void addDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    /**
     * Set panel to read-only mode (hide CRUD buttons)
     */
    public void setReadOnly(boolean readOnly) {
        btnTambah.setVisible(!readOnly);
        btnEdit.setVisible(!readOnly);
        btnHapus.setVisible(!readOnly);
        // btnDetail remains visible
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    // ============================================================
    // PUBLIC METHODS
    // ============================================================
    public void refreshData() {
        refreshTable();
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Fasilitas> fasilitasList = fasilitasDAO.getAllFasilitas();
            int no = 1;
            for (Fasilitas fasilitas : fasilitasList) {
                ImageIcon thumbnail = null;
                if (fasilitas.getFotoUrl() != null && !fasilitas.getFotoUrl().isEmpty()) {
                    File imgFile = new File(fasilitas.getFotoUrl());
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imgFile.getPath());
                        Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                        thumbnail = new ImageIcon(img);
                    }
                }

                tableModel.addRow(new Object[] {
                        no++,
                        thumbnail,
                        fasilitas.getNamaFasilitas() != null ? fasilitas.getNamaFasilitas() : "-",
                        fasilitas.getJenis() != null ? getJenisIcon(fasilitas.getJenis()) + " " + fasilitas.getJenis() : "-",
                        fasilitas.getDinasTerkait() != null ? fasilitas.getDinasTerkait() : "-",
                        fasilitas.getAlamat() != null ? fasilitas.getAlamat() : "-",
                        fasilitas.getKeterangan() != null ? fasilitas.getKeterangan() : "-",
                        fasilitas.getIdFasilitas()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error memuat data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String getJenisIcon(String jenis) {
        if (jenis == null) return "📋";
        if (jenis.contains("Kesehatan")) return "🏥";
        if (jenis.contains("Pendidikan")) return "📚";
        if (jenis.contains("Ibadah")) return "🕌";
        if (jenis.contains("Olahraga")) return "🏟️";
        if (jenis.contains("Pemerintahan")) return "🏢";
        if (jenis.contains("Perekonomian")) return "🛒";
        if (jenis.contains("Transportasi")) return "🚌";
        if (jenis.contains("Lingkungan")) return "🌿";
        if (jenis.contains("Telekomunikasi")) return "📡";
        if (jenis.contains("Energi")) return "⚡";
        return "📋";
    }

    private void searchData() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String selectedJenis = (String) cmbJenisFilter.getSelectedItem();

        if (selectedJenis == null) {
            selectedJenis = "-- Semua --";
        }

        boolean filterAllJenis = selectedJenis.equals("-- Semua --");

        tableModel.setRowCount(0);
        try {
            List<Fasilitas> fasilitasList = fasilitasDAO.getAllFasilitas();

            for (Fasilitas fasilitas : fasilitasList) {
                String jenisFasilitas = fasilitas.getJenis();

                // Filter berdasarkan jenis
                // selectedJenis bisa mengandung emoji (misal "🏟️ Olahraga"), sementara
                // jenisFasilitas dari DB hanya teks biasa (misal "Olahraga")
                if (!filterAllJenis) {
                    if (jenisFasilitas == null || !selectedJenis.contains(jenisFasilitas)) {
                        continue;
                    }
                }

                // Filter berdasarkan keyword
                if (!keyword.isEmpty()) {
                    String namaFasilitas = fasilitas.getNamaFasilitas() != null ? fasilitas.getNamaFasilitas().toLowerCase() : "";
                    String dinasTerkait = fasilitas.getDinasTerkait() != null ? fasilitas.getDinasTerkait().toLowerCase() : "";
                    String alamat = fasilitas.getAlamat() != null ? fasilitas.getAlamat().toLowerCase() : "";
                    String keterangan = fasilitas.getKeterangan() != null ? fasilitas.getKeterangan().toLowerCase() : "";

                    if (!namaFasilitas.contains(keyword) && !dinasTerkait.contains(keyword) &&
                            !alamat.contains(keyword) && !keterangan.contains(keyword)) {
                        continue;
                    }
                }

                // Gunakan nomor urut 1, 2, 3...
                int no = tableModel.getRowCount() + 1;
                
                ImageIcon thumbnail = null;
                if (fasilitas.getFotoUrl() != null && !fasilitas.getFotoUrl().isEmpty()) {
                    File imgFile = new File(fasilitas.getFotoUrl());
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imgFile.getPath());
                        Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
                        thumbnail = new ImageIcon(img);
                    }
                }

                tableModel.addRow(new Object[] {
                        no,
                        thumbnail,
                        fasilitas.getNamaFasilitas() != null ? fasilitas.getNamaFasilitas() : "-",
                        fasilitas.getJenis() != null ? getJenisIcon(fasilitas.getJenis()) + " " + fasilitas.getJenis() : "-",
                        fasilitas.getDinasTerkait() != null ? fasilitas.getDinasTerkait() : "-",
                        fasilitas.getAlamat() != null ? fasilitas.getAlamat() : "-",
                        fasilitas.getKeterangan() != null ? fasilitas.getKeterangan() : "-",
                        fasilitas.getIdFasilitas()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error pencarian: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahData() {
        FasilitasFormDialog dialog = new FasilitasFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Tambah Data Fasilitas",
                true);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Fasilitas fasilitas = dialog.getFasilitas();

            try {
                boolean success = fasilitasDAO.addFasilitas(fasilitas);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data fasilitas berhasil ditambahkan!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    txtSearch.setText("");

                    notifyDataChanged();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menambahkan data!",
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

    private void editData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih data yang akan diedit!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil ID asli dari kolom tersembunyi
        int idFasilitas = (int) tableModel.getValueAt(row, 7);
        Fasilitas fasilitas = fasilitasDAO.getFasilitasById(idFasilitas);

        if (fasilitas == null) {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        FasilitasFormDialog dialog = new FasilitasFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Edit Data Fasilitas",
                true,
                fasilitas);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Fasilitas updatedFasilitas = dialog.getFasilitas();

            try {
                boolean success = fasilitasDAO.updateFasilitas(updatedFasilitas);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data fasilitas berhasil diupdate!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();

                    notifyDataChanged();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal mengupdate data!",
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
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih data yang akan dihapus!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String namaFasilitas = table.getValueAt(row, 2).toString(); // Nama di kolom 2
        int idFasilitas = (int) tableModel.getValueAt(row, 7); // ID di hidden kolom 7

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus fasilitas:\n" + namaFasilitas + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = fasilitasDAO.deleteFasilitas(idFasilitas);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data fasilitas berhasil dihapus!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);

                    refreshTable();

                    notifyDataChanged();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menghapus data!",
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
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih data untuk melihat detail!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idFasilitas = (int) tableModel.getValueAt(row, 7);
        Fasilitas fasilitas = fasilitasDAO.getFasilitasById(idFasilitas);

        if (fasilitas != null) {
            JPanel detailPanel = new JPanel(new BorderLayout(20, 20));
            detailPanel.setBackground(Color.WHITE);
            detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Info Panel (Left)
            JPanel infoPanel = new JPanel(new GridBagLayout());
            infoPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(8, 5, 8, 5);
            gbc.anchor = GridBagConstraints.NORTHWEST;

            int rowIdx = 0;
            addDetailRow(infoPanel, "ID Fasilitas", String.valueOf(fasilitas.getIdFasilitas()), gbc, rowIdx++);
            addDetailRow(infoPanel, "Nama Fasilitas", fasilitas.getNamaFasilitas(), gbc, rowIdx++);
            addDetailRow(infoPanel, "Jenis", fasilitas.getJenis(), gbc, rowIdx++);
            addDetailRow(infoPanel, "Dinas Terkait", fasilitas.getDinasTerkait(), gbc, rowIdx++);
            addDetailRow(infoPanel, "Alamat", fasilitas.getAlamat(), gbc, rowIdx++);
            addDetailRow(infoPanel, "Keterangan", fasilitas.getKeterangan(), gbc, rowIdx++);
            addDetailRow(infoPanel, "Dibuat", fasilitas.getCreatedAt() != null ? fasilitas.getCreatedAt().toString() : "-", gbc, rowIdx++);
            addDetailRow(infoPanel, "Terakhir Update", fasilitas.getUpdatedAt() != null ? fasilitas.getUpdatedAt().toString() : "-", gbc, rowIdx++);

            // Glue at bottom
            gbc.weighty = 1.0;
            infoPanel.add(Box.createVerticalGlue(), gbc);

            detailPanel.add(infoPanel, BorderLayout.CENTER);

            // Image Panel (Right)
            if (fasilitas.getFotoUrl() != null && !fasilitas.getFotoUrl().isEmpty()) {
                try {
                    File imgFile = new File(fasilitas.getFotoUrl());
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imgFile.getPath());
                        Image img = icon.getImage();
                        int maxW = 300, maxH = 400;
                        int imgW = icon.getIconWidth(), imgH = icon.getIconHeight();
                        double scale = Math.min((double) maxW / imgW, (double) maxH / imgH);
                        int finalW = (int) (imgW * scale), finalH = (int) (imgH * scale);
                        
                        Image scaled = img.getScaledInstance(finalW, finalH, Image.SCALE_SMOOTH);
                        JLabel lblFoto = new JLabel(new ImageIcon(scaled));
                        lblFoto.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                        ));
                        
                        JPanel photoContainer = new JPanel(new BorderLayout());
                        photoContainer.setBackground(Color.WHITE);
                        photoContainer.add(lblFoto, BorderLayout.NORTH);
                        photoContainer.setBorder(BorderFactory.createTitledBorder("Foto Fasilitas"));
                        detailPanel.add(photoContainer, BorderLayout.EAST);
                    }
                } catch (Exception e) {
                    System.err.println("Gagal memuat foto detail: " + e.getMessage());
                }
            }

            JScrollPane scrollPane = new JScrollPane(detailPanel);
            scrollPane.setPreferredSize(new Dimension(850, 480));
            scrollPane.setBorder(null);

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Detail Fasilitas - " + fasilitas.getNamaFasilitas(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDetailRow(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel lblName = new JLabel(label + " :");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblName, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JLabel lblValue = new JLabel("<html><body style='width: 300px'>" + (value != null ? value : "-") + "</body></html>");
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lblValue, gbc);
    }

    private void exportToCSV() {
        try {
            // Simpan ke file CSV
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("fasilitas_export.csv"));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.FileWriter writer = new java.io.FileWriter(file);
                
                // Header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.append(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) writer.append(",");
                }
                writer.append("\n");
                
                // Data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        writer.append(value != null ? value.toString() : "");
                        if (j < tableModel.getColumnCount() - 1) writer.append(",");
                    }
                    writer.append("\n");
                }
                
                writer.close();
                JOptionPane.showMessageDialog(this,
                        "Data berhasil diekspor ke:\n" + file.getAbsolutePath(),
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error export data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // INNER CLASS: Image Renderer
    // ============================================================
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
                if (isSelected) {
                    label.setOpaque(true);
                    label.setBackground(table.getSelectionBackground());
                }
                return label;
            }
            
            JLabel label = new JLabel("Beban...");
            if (value == null) label.setText("No Image");
            label.setHorizontalAlignment(JLabel.CENTER);
            return label;
        }
    }
}