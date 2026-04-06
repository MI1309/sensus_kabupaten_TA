package com.kabupaten.view;

import com.kabupaten.model.Kecamatan;
import com.kabupaten.model.Desa;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.dao.DesaDAO;
import com.kabupaten.listener.DataChangeListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.kabupaten.Dialog.KecamatanFormDialog;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Panel CRUD untuk data Kecamatan dengan jumlah Desa dan Kelurahan
 */
public class CrudKecamatanPanel extends JPanel {
    private KecamatanDAO kecamatanDAO = new KecamatanDAO();
    private DesaDAO desaDAO = new DesaDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblTotalKecamatan;
    private JLabel lblTotalDesa;
    private JLabel lblTotalKelurahan;
    private JButton btnTambah, btnEdit, btnHapus, btnDetail;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public CrudKecamatanPanel() {
        setLayout(new BorderLayout());

        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pencarian:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Tabel data Kecamatan dengan kolom gambar
        tableModel = new DefaultTableModel(
                new Object[] { "No", "Foto", "Nama Kecamatan", "Alamat Kantor", "Nama Pejabat",
                        "No HP", "Jumlah Desa", "Jumlah Kelurahan", "Total", "ID_HIDDEN" },
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
        table.setRowHeight(80); // Tinggi baris untuk menampilkan gambar
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Kolom Foto
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(120);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);

        // Hide ID_HIDDEN column
        table.getColumnModel().getColumn(9).setMinWidth(0);
        table.getColumnModel().getColumn(9).setMaxWidth(0);
        table.getColumnModel().getColumn(9).setPreferredWidth(0);

        // Custom renderer untuk kolom gambar
        table.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel bawah dengan tombol dan statistik
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Panel tombol CRUD
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnTambah = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnDetail = new JButton("Detail");

        btnTambah.setBackground(new Color(46, 125, 50));
        btnTambah.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(33, 150, 243));
        btnEdit.setForeground(Color.WHITE);
        btnHapus.setBackground(new Color(244, 67, 54));
        btnHapus.setForeground(Color.WHITE);
        btnDetail.setBackground(new Color(0, 128, 128));
        btnDetail.setForeground(Color.WHITE);

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnDetail);

        // Export Button
        JButton btnExport = new JButton("Export Data");
        btnExport.setBackground(new Color(40, 167, 69));
        btnExport.setForeground(Color.WHITE);
        btnExport.addActionListener(e -> com.kabupaten.utils.ExportUtils.exportToCSV(table, "Kecamatan"));
        buttonPanel.add(btnExport);

        // UI Polish - Table
        table.getTableHeader().setBackground(Color.black);
        table.getTableHeader().setForeground(Color.white);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(new Color(79, 172, 254));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setOpaque(true);
        return label;
    }
});

        // Panel statistik
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalKecamatan = new JLabel("Total Kecamatan: 0");
        lblTotalDesa = new JLabel("Total Desa: 0");
        lblTotalKelurahan = new JLabel("Total Kelurahan: 0");

        lblTotalKecamatan.setFont(new Font("Arial", Font.BOLD, 12));
        lblTotalDesa.setFont(new Font("Arial", Font.BOLD, 12));
        lblTotalKelurahan.setFont(new Font("Arial", Font.BOLD, 12));

        statsPanel.add(lblTotalKecamatan);
        statsPanel.add(new JLabel("  |  "));
        statsPanel.add(lblTotalDesa);
        statsPanel.add(new JLabel("  |  "));
        statsPanel.add(lblTotalKelurahan);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statsPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

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

        // Load initial data
        refreshTable();
    }

    public void setReadOnly(boolean readOnly) {
        btnTambah.setVisible(!readOnly);
        btnEdit.setVisible(!readOnly);
        btnHapus.setVisible(!readOnly);
    }

    public void addDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    public void refreshData() {
        refreshTable();
    }

    /**
     * Class untuk renderer gambar di tabel
     */
    private class ImageRenderer extends DefaultTableCellRenderer {
        private final int imageWidth = 70;
        private final int imageHeight = 70;
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            
            // Set background untuk selected/unselected
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            } else {
                label.setBackground(table.getBackground());
                label.setForeground(table.getForeground());
            }
            label.setOpaque(true);
            
            if (value instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) value;
                // Resize gambar agar sesuai dengan ukuran baris
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
                label.setText("");
            } else {
                // Gambar dummy jika tidak ada foto
                label.setIcon(createDummyImage());
                label.setText("");
            }
            
            return label;
        }
        
        /**
         * Membuat gambar dummy (placeholder) jika tidak ada foto
         */
        private ImageIcon createDummyImage() {
            BufferedImage dummy = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = dummy.createGraphics();
            
            // Background gradient
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 60, 114),
                    imageWidth, imageHeight, new Color(79, 172, 254));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, imageWidth, imageHeight);
            
            // Border
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(2, 2, imageWidth - 5, imageHeight - 5);
            
            // Icon kamera
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            FontMetrics fm = g2d.getFontMetrics();
            String cameraIcon = "📷";
            int x = (imageWidth - fm.stringWidth(cameraIcon)) / 2;
            int y = (imageHeight + fm.getAscent() - fm.getDescent()) / 2;
            g2d.drawString(cameraIcon, x, y);
            
            // Text kecil
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 8));
            fm = g2d.getFontMetrics();
            String noImg = "No Image";
            x = (imageWidth - fm.stringWidth(noImg)) / 2;
            g2d.drawString(noImg, x, y + 20);
            
            g2d.dispose();
            
            return new ImageIcon(dummy);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        int totalDesa = 0;
        int totalKelurahan = 0;

        try {
            List<Kecamatan> kecamatanList = kecamatanDAO.getAllKecamatan();
            int no = 1;
            for (Kecamatan kec : kecamatanList) {
                int jmlDesa = kec.getJumlahDesa();
                int jmlKelurahan = kec.getJumlahKelurahan();
                int total = kec.getTotalWilayahAdministratif();

                totalDesa += jmlDesa;
                totalKelurahan += jmlKelurahan;

                // Load gambar
                ImageIcon fotoIcon = loadImage(kec.getFotoUrl());

                tableModel.addRow(new Object[] {
                        no++,
                        fotoIcon,
                        kec.getNamaKecamatan() != null ? kec.getNamaKecamatan() : "-",
                        kec.getAlamatKantor() != null ? kec.getAlamatKantor() : "-",
                        kec.getNamaKepala() != null ? kec.getNamaKepala() : "-",
                        kec.getNoHp() != null ? kec.getNoHp() : "-",
                        jmlDesa,
                        jmlKelurahan,
                        total,
                        kec.getIdKecamatan()
                });
            }

            lblTotalKecamatan.setText("Total Kecamatan: " + kecamatanList.size());
            lblTotalDesa.setText("Total Desa: " + totalDesa);
            lblTotalKelurahan.setText("Total Kelurahan: " + totalKelurahan);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error memuat data: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Method untuk load gambar dari path
     * @param fotoPath Path file gambar
     * @return ImageIcon atau null jika gagal
     */
    private ImageIcon loadImage(String fotoPath) {
        if (fotoPath == null || fotoPath.trim().isEmpty()) {
            return null;
        }
        
        try {
            File imgFile = new File(fotoPath);
            if (!imgFile.exists()) {
                // Coba dari project path
                String projectPath = System.getProperty("user.dir");
                imgFile = new File(projectPath + "/" + fotoPath);
            }
            
            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(imgFile.getPath());
                // Resize awal agar tidak terlalu besar
                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            // Gagal load gambar
            System.err.println("Gagal load gambar: " + fotoPath);
        }
        return null;
    }

    private void searchData() {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }

        tableModel.setRowCount(0);
        int totalDesa = 0;
        int totalKelurahan = 0;

        try {
            List<Kecamatan> kecamatanList = kecamatanDAO.searchKecamatan(keyword);
            int no = 1;
            for (Kecamatan kec : kecamatanList) {
                int jmlDesa = kec.getJumlahDesa();
                int jmlKelurahan = kec.getJumlahKelurahan();
                int total = kec.getTotalWilayahAdministratif();

                totalDesa += jmlDesa;
                totalKelurahan += jmlKelurahan;

                ImageIcon fotoIcon = loadImage(kec.getFotoUrl());

                tableModel.addRow(new Object[] {
                        no++,
                        fotoIcon,
                        kec.getNamaKecamatan() != null ? kec.getNamaKecamatan() : "-",
                        kec.getAlamatKantor() != null ? kec.getAlamatKantor() : "-",
                        kec.getNamaKepala() != null ? kec.getNamaKepala() : "-",
                        kec.getNoHp() != null ? kec.getNoHp() : "-",
                        jmlDesa,
                        jmlKelurahan,
                        total,
                        kec.getIdKecamatan()
                });
            }

            lblTotalKecamatan.setText("Total Kecamatan: " + kecamatanList.size());
            lblTotalDesa.setText("Total Desa: " + totalDesa);
            lblTotalKelurahan.setText("Total Kelurahan: " + totalKelurahan);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error pencarian: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahData() {
        KecamatanFormDialog dialog = new KecamatanFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Tambah Data Kecamatan",
                true);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Kecamatan kecamatan = dialog.getKecamatan();

            try {
                boolean success = kecamatanDAO.addKecamatan(kecamatan);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data kecamatan berhasil ditambahkan!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    txtSearch.setText("");
                    notifyDataChanged();
                    
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menambahkan data!\nNama kecamatan mungkin sudah ada.",
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

        int idKecamatan = (int) table.getValueAt(row, 9);
        Kecamatan kecamatan = kecamatanDAO.getKecamatanById(idKecamatan);

        if (kecamatan == null) {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simpan path foto lama untuk backup jika user tidak memilih foto baru
        final String oldFotoPath = kecamatan.getFotoUrl();

        KecamatanFormDialog dialog = new KecamatanFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Edit Data Kecamatan",
                true,
                kecamatan);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Kecamatan updatedKecamatan = dialog.getKecamatan();
            
            // Jika user tidak memilih foto baru, gunakan foto lama
            if (updatedKecamatan.getFotoUrl() == null || updatedKecamatan.getFotoUrl().trim().isEmpty()) {
                updatedKecamatan.setFotoUrl(oldFotoPath);
            }

            try {
                boolean success = kecamatanDAO.updateKecamatan(updatedKecamatan);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data kecamatan berhasil diupdate!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    notifyDataChanged();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal mengupdate data!\nNama kecamatan mungkin sudah ada.",
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

        int idKecamatan = (int) table.getValueAt(row, 9);
        String namaKecamatan = table.getValueAt(row, 2).toString();
        int jumlahDesa = (int) table.getValueAt(row, 6);
        int jumlahKelurahan = (int) table.getValueAt(row, 7);
        int total = jumlahDesa + jumlahKelurahan;

        if (total > 0) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat menghapus kecamatan!\n" +
                            "Kecamatan " + namaKecamatan + " masih memiliki:\n" +
                            "- " + jumlahDesa + " Desa\n" +
                            "- " + jumlahKelurahan + " Kelurahan\n\n" +
                            "Hapus semua desa/kelurahan terlebih dahulu.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus kecamatan:\n" + namaKecamatan + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            try {
                // Hapus file gambar jika ada
                Kecamatan kec = kecamatanDAO.getKecamatanById(idKecamatan);
                if (kec != null && kec.getFotoUrl() != null && !kec.getFotoUrl().trim().isEmpty()) {
                    File imgFile = new File(kec.getFotoUrl());
                    if (!imgFile.exists()) {
                        String projectPath = System.getProperty("user.dir");
                        imgFile = new File(projectPath + "/" + kec.getFotoUrl());
                    }
                    if (imgFile.exists()) {
                        imgFile.delete(); // Hapus file gambar
                    }
                }
                
                boolean success = kecamatanDAO.deleteKecamatan(idKecamatan);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data kecamatan berhasil dihapus!",
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

        int idKecamatan = (int) table.getValueAt(row, 9);
        Kecamatan kec = kecamatanDAO.getKecamatanById(idKecamatan);

        if (kec != null) {
            // Ambil daftar desa/kelurahan
            List<Desa> desaList = desaDAO.getDesaByKecamatan(idKecamatan);
            StringBuilder listDesa = new StringBuilder("\n=== DAFTAR DESA & KELURAHAN ===\n");

            if (desaList.isEmpty()) {
                listDesa.append("(Belum ada data)\n");
            } else {
                for (int i = 0; i < desaList.size(); i++) {
                    Desa d = desaList.get(i);
                    listDesa.append(String.format("%d. [%s] %s\n",
                            (i + 1), d.getJenis(), d.getNamaDesa()));
                }
            }

            // Load gambar untuk ditampilkan di detail
            String imageInfo = "";
            if (kec.getFotoUrl() != null && !kec.getFotoUrl().trim().isEmpty()) {
                File imgFile = new File(kec.getFotoUrl());
                if (!imgFile.exists()) {
                    String projectPath = System.getProperty("user.dir");
                    imgFile = new File(projectPath + "/" + kec.getFotoUrl());
                }
                if (imgFile.exists()) {
                    imageInfo = "\n📸 Foto Kecamatan    : " + kec.getFotoUrl() + " (Ada)\n";
                } else {
                    imageInfo = "\n📸 Foto Kecamatan    : File tidak ditemukan\n";
                }
            } else {
                imageInfo = "\n📸 Foto Kecamatan    : Belum ada foto\n";
            }

            String detail = String.format(
                    "========== DETAIL DATA KECAMATAN ==========\n\n" +
                            "ID Kecamatan         : %d\n" +
                            "Nama Kecamatan       : %s\n" +
                            "Alamat Kantor        : %s\n" +
                            "Nama Pejabat         : %s\n" +
                            "No HP                : %s\n" +
                            "%s" +
                            "\n=== STATISTIK WILAYAH ===\n" +
                            "Jumlah Desa          : %d\n" +
                            "Jumlah Kelurahan     : %d\n" +
                            "Total Wilayah        : %d\n\n" +
                            "Dibuat pada          : %s\n" +
                            "Terakhir diupdate    : %s\n" +
                            "%s",
                    kec.getIdKecamatan(),
                    kec.getNamaKecamatan() != null ? kec.getNamaKecamatan() : "-",
                    kec.getAlamatKantor() != null ? kec.getAlamatKantor() : "-",
                    kec.getNamaKepala() != null ? kec.getNamaKepala() : "-",
                    kec.getNoHp() != null ? kec.getNoHp() : "-",
                    imageInfo,
                    kec.getJumlahDesa(),
                    kec.getJumlahKelurahan(),
                    kec.getTotalWilayahAdministratif(),
                    kec.getCreatedAt() != null ? kec.getCreatedAt() : "-",
                    kec.getUpdatedAt() != null ? kec.getUpdatedAt() : "-",
                    listDesa.toString());

            // Buat panel dengan scroll pane dan preview gambar jika ada
            JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
            
            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
            
            JScrollPane textScrollPane = new JScrollPane(textArea);
            textScrollPane.setPreferredSize(new Dimension(600, 450));
            detailPanel.add(textScrollPane, BorderLayout.CENTER);
            
            // Jika ada foto, tampilkan preview
            if (kec.getFotoUrl() != null && !kec.getFotoUrl().trim().isEmpty()) {
                File imgFile = new File(kec.getFotoUrl());
                if (!imgFile.exists()) {
                    String projectPath = System.getProperty("user.dir");
                    imgFile = new File(projectPath + "/" + kec.getFotoUrl());
                }
                if (imgFile.exists()) {
                    try {
                        ImageIcon icon = new ImageIcon(imgFile.getPath());
                        int imgW = icon.getIconWidth();
                        int imgH = icon.getIconHeight();
                        int maxW = 200, maxH = 150;
                        double scale = Math.min((double) maxW / imgW, (double) maxH / imgH);
                        int newW = (int) (imgW * scale);
                        int newH = (int) (imgH * scale);
                        Image scaled = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                        JLabel lblFotoPreview = new JLabel(new ImageIcon(scaled));
                        lblFotoPreview.setHorizontalAlignment(JLabel.CENTER);
                        lblFotoPreview.setBorder(BorderFactory.createTitledBorder("Preview Kecamatan"));
                        
                        JPanel fotoPanel = new JPanel(new BorderLayout());
                        fotoPanel.add(lblFotoPreview, BorderLayout.CENTER);
                        fotoPanel.setPreferredSize(new Dimension(220, 180));
                        detailPanel.add(fotoPanel, BorderLayout.EAST);
                    } catch (Exception e) {
                        // Gagal load preview
                    }
                }
            }

            JScrollPane scrollPane = new JScrollPane(detailPanel);
            scrollPane.setPreferredSize(new Dimension(850, 550));

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Detail Data Kecamatan - " + kec.getNamaKecamatan(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}