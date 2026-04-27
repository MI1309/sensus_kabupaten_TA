package com.kabupaten.view;

import com.kabupaten.model.Desa;
import com.kabupaten.model.Kecamatan;
import com.kabupaten.model.RTRW;
import com.kabupaten.dao.DesaDAO;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.dao.RTRWDAO;
import com.kabupaten.listener.DataChangeListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import com.kabupaten.Dialog.DesaFormDialog;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Panel CRUD untuk data Desa dengan Filter Kecamatan
 */
public class CrudDesaPanel extends JPanel {
    private DesaDAO desaDAO = new DesaDAO();
    private KecamatanDAO kecamatanDAO = new KecamatanDAO();
    private RTRWDAO rtrwDAO = new RTRWDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbKecamatanFilter;
    private JComboBox<String> cmbJenisFilter;
    private JButton btnTambah, btnEdit, btnHapus, btnDetail;

    // Listener untuk notifikasi perubahan data
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public CrudDesaPanel() {
        setLayout(new BorderLayout());

        // Panel pencarian dan filter
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchPanel.add(new JLabel("Filter Kecamatan:"));
        cmbKecamatanFilter = new JComboBox<>();
        cmbKecamatanFilter.addItem("-- Semua Kecamatan --");
        loadKecamatanList();
        searchPanel.add(cmbKecamatanFilter);

        searchPanel.add(new JLabel("  Jenis:"));
        cmbJenisFilter = new JComboBox<>();
        cmbJenisFilter.addItem("-- Semua --");
        cmbJenisFilter.addItem("DESA");
        cmbJenisFilter.addItem("KELURAHAN");
        searchPanel.add(cmbJenisFilter);

        searchPanel.add(new JLabel("  Pencarian:"));
        txtSearch = new JTextField(15);
        searchPanel.add(txtSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Tabel data Desa
        tableModel = new DefaultTableModel(
                new Object[] { "No", "Foto", "Kecamatan", "Nama Desa / Kelurahan", "Jenis", "Alamat Kantor",
                        "Nama Kepala", "No HP", "ID_HIDDEN" },
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
        table.setRowHeight(80); // Taller rows for images
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setBackground(Color.black);
        table.getTableHeader().setForeground(Color.white);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setOpaque(true);
        return label;
    }
});

        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Foto
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        // Hide ID_HIDDEN column
        table.getColumnModel().getColumn(8).setMinWidth(0);
        table.getColumnModel().getColumn(8).setMaxWidth(0);
        table.getColumnModel().getColumn(8).setPreferredWidth(0);

        // Custom renderer untuk kolom gambar
        table.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

        add(new JScrollPane(table), BorderLayout.CENTER);

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
        btnDetail.setBackground(new Color(0, 128, 128)); // Teal color
        btnDetail.setForeground(Color.WHITE);

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnDetail);

        // Export Button
        JButton btnExport = new JButton("Export Data");
        btnExport.setBackground(new Color(40, 167, 69)); // Green Excel-like
        btnExport.setForeground(Color.WHITE);
        btnExport.addActionListener(e -> com.kabupaten.utils.ExportUtils.exportToCSV(table, "Desa"));
        buttonPanel.add(btnExport);

        // UI Polish - Table
        table.getTableHeader().setBackground(Color.BLACK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(new Color(79, 172, 254));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));

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
        cmbKecamatanFilter.addActionListener(e -> searchData());
        cmbJenisFilter.addActionListener(e -> searchData());

        // Load initial data
        refreshTable();
    }

    public void setReadOnly(boolean readOnly) {
        btnTambah.setVisible(!readOnly);
        btnEdit.setVisible(!readOnly);
        btnHapus.setVisible(!readOnly);
    }

    // ========== LISTENER MANAGEMENT ==========
    /**
     * Menambahkan listener untuk perubahan data
     */
    public void addDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    /**
     * Memberitahu semua listener bahwa data telah berubah
     */
    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    // ========== PUBLIC METHODS ==========
    /**
     * Method public untuk refresh dari luar (dipanggil oleh panel lain)
     */
    public void refreshData() {
        refreshKecamatanComboBox();
        refreshTable();
    }

    /**
     * Method untuk refresh combo box kecamatan
     */
    public void refreshKecamatanComboBox() {
        String currentSelection = (String) cmbKecamatanFilter.getSelectedItem();

        // Matikan listener sementara untuk menghindari trigger berulang
        cmbKecamatanFilter.removeAllItems();
        cmbKecamatanFilter.addItem("-- Semua Kecamatan --");

        List<String> kecamatanList = kecamatanDAO.getAllKecamatanNames();
        for (String kecamatan : kecamatanList) {
            cmbKecamatanFilter.addItem(kecamatan);
        }

        // Coba pertahankan selection sebelumnya
        if (currentSelection != null && !currentSelection.equals("-- Semua Kecamatan --")) {
            boolean found = false;
            for (String kec : kecamatanList) {
                if (kec.equals(currentSelection)) {
                    cmbKecamatanFilter.setSelectedItem(currentSelection);
                    found = true;
                    break;
                }
            }
            if (!found) {
                cmbKecamatanFilter.setSelectedIndex(0);
            }
        } else {
            cmbKecamatanFilter.setSelectedIndex(0);
        }
    }

    // ========== PRIVATE METHODS ==========
    private void loadKecamatanList() {
        List<String> kecamatanList = kecamatanDAO.getAllKecamatanNames();
        for (String kecamatan : kecamatanList) {
            cmbKecamatanFilter.addItem(kecamatan);
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        try {
            int idKecamatan = -1;
            if (cmbKecamatanFilter.getSelectedIndex() > 0) {
                String namaKec = cmbKecamatanFilter.getSelectedItem().toString();
                Kecamatan kec = kecamatanDAO.getKecamatanByName(namaKec);
                if (kec != null)
                    idKecamatan = kec.getIdKecamatan();
            }

            List<Desa> list;
            if (idKecamatan != -1) {
                list = desaDAO.getDesaByKecamatan(idKecamatan);
            } else {
                list = desaDAO.getAllDesa();
            }

            int no = 1;
            for (Desa d : list) {
                // Filter jenis jika dipilih
                if (cmbJenisFilter.getSelectedIndex() > 0) {
                    String jenisSelected = cmbJenisFilter.getSelectedItem().toString();
                    if (!d.getJenis().equals(jenisSelected))
                        continue;
                }

                ImageIcon fotoIcon = loadImage(d.getFotoUrl());

                tableModel.addRow(new Object[] {
                        no++,
                        fotoIcon,
                        d.getNamaKecamatan(),
                        d.getNamaDesa(),
                        d.getJenis(),
                        d.getAlamatKantor(),
                        d.getNamaKepala(),
                        d.getNoHp(),
                        d.getIdDesa()
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

    private ImageIcon loadImage(String fotoPath) {
        if (fotoPath == null || fotoPath.trim().isEmpty()) {
            return null;
        }

        try {
            File imgFile = new File(fotoPath);
            if (!imgFile.exists()) {
                String projectPath = System.getProperty("user.dir");
                imgFile = new File(projectPath + "/" + fotoPath);
            }

            if (imgFile.exists()) {
                ImageIcon icon = new ImageIcon(imgFile.getPath());
                Image img = icon.getImage();
                Image scaled = img.getScaledInstance(75, 100, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.err.println("Gagal load gambar: " + fotoPath);
        }
        return null;
    }

    private class ImageRenderer extends DefaultTableCellRenderer {
        private final int imageWidth = 75;
        private final int imageHeight = 100;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);

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
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
                label.setText("");
            } else {
                label.setIcon(createDummyImage());
                label.setText("");
            }

            return label;
        }

        private ImageIcon createDummyImage() {
            BufferedImage dummy = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = dummy.createGraphics();
            GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 60, 114), imageWidth, imageHeight,
                    new Color(79, 172, 254));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, imageWidth, imageHeight);
            g2d.setColor(new Color(255, 255, 255, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(2, 2, imageWidth - 5, imageHeight - 5);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            FontMetrics fm = g2d.getFontMetrics();
            String cameraIcon = "📷";
            int x = (imageWidth - fm.stringWidth(cameraIcon)) / 2;
            int y = (imageHeight + fm.getAscent() - fm.getDescent()) / 2;
            g2d.drawString(cameraIcon, x, y);
            g2d.dispose();
            return new ImageIcon(dummy);
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        String selectedKecamatan = (String) cmbKecamatanFilter.getSelectedItem();
        String selectedJenis = (String) cmbJenisFilter.getSelectedItem();

        // Validasi null
        if (selectedKecamatan == null) {
            selectedKecamatan = "-- Semua Kecamatan --";
        }
        if (selectedJenis == null) {
            selectedJenis = "-- Semua --";
        }

        boolean filterAllKecamatan = selectedKecamatan.equals("-- Semua Kecamatan --");
        boolean filterAllJenis = selectedJenis.equals("-- Semua --");

        tableModel.setRowCount(0);
        try {
            List<Desa> desaList = desaDAO.getAllDesa();

            for (Desa desa : desaList) {
                String namaKecamatanDesa = desa.getNamaKecamatan();
                String jenisDesa = desa.getJenis();

                // Filter berdasarkan kecamatan
                if (!filterAllKecamatan) {
                    if (namaKecamatanDesa == null || !selectedKecamatan.equals(namaKecamatanDesa)) {
                        continue;
                    }
                }

                // Filter berdasarkan jenis
                if (!filterAllJenis) {
                    if (jenisDesa == null || !selectedJenis.equals(jenisDesa)) {
                        continue;
                    }
                }

                // Filter berdasarkan keyword
                if (!keyword.isEmpty()) {
                    String namaDesa = desa.getNamaDesa() != null ? desa.getNamaDesa().toLowerCase() : "";
                    String alamat = desa.getAlamatKantor() != null ? desa.getAlamatKantor().toLowerCase() : "";
                    String namaKepala = desa.getNamaKepala() != null ? desa.getNamaKepala().toLowerCase() : "";

                    if (!namaDesa.contains(keyword) && !alamat.contains(keyword) &&
                            !namaKepala.contains(keyword)
                            && (namaKecamatanDesa == null || !namaKecamatanDesa.toLowerCase().contains(keyword))) {
                        continue;
                    }
                }

                tableModel.addRow(new Object[] {
                        (tableModel.getRowCount() + 1),
                        loadImage(desa.getFotoUrl()),
                        namaKecamatanDesa != null ? namaKecamatanDesa : "-",
                        desa.getNamaDesa() != null ? desa.getNamaDesa() : "-",
                        jenisDesa != null ? jenisDesa : "-",
                        desa.getAlamatKantor() != null ? desa.getAlamatKantor() : "-",
                        desa.getNamaKepala() != null ? desa.getNamaKepala() : "-",
                        desa.getNoHp() != null ? desa.getNoHp() : "-",
                        desa.getIdDesa()
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
        DesaFormDialog dialog = new DesaFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Tambah Data Desa",
                true);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Desa desa = dialog.getDesa();

            try {
                boolean success = desaDAO.addDesa(desa);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data desa berhasil ditambahkan!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    txtSearch.setText("");

                    // PENTING: Notifikasi panel lain untuk refresh
                    notifyDataChanged();
                    refreshKecamatanComboBox();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menambahkan data!\nNama desa mungkin sudah ada di kecamatan yang sama.",
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

        int idDesa = (int) table.getValueAt(row, 8);
        Desa desa = desaDAO.getDesaById(idDesa);

        if (desa == null) {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String oldFotoPath = desa.getFotoUrl();

        DesaFormDialog dialog = new DesaFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Edit Data Desa/Kelurahan",
                true,
                desa);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Desa updatedDesa = dialog.getDesa();
            if (updatedDesa.getFotoUrl() == null || updatedDesa.getFotoUrl().trim().isEmpty()) {
                updatedDesa.setFotoUrl(oldFotoPath);
            }

            try {
                boolean success = desaDAO.updateDesa(updatedDesa);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data desa berhasil diupdate!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();

                    // PENTING: Notifikasi panel lain untuk refresh
                    notifyDataChanged();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal mengupdate data!\nNama desa mungkin sudah ada di kecamatan yang sama.",
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

        int idDesa = (int) table.getValueAt(row, 8);
        String namaDesa = table.getValueAt(row, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus desa:\n" + namaDesa + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Hapus file gambar jika ada
                Desa d = desaDAO.getDesaById(idDesa);
                if (d != null && d.getFotoUrl() != null && !d.getFotoUrl().trim().isEmpty()) {
                    File imgFile = new File(d.getFotoUrl());
                    if (!imgFile.exists()) {
                        String projectPath = System.getProperty("user.dir");
                        imgFile = new File(projectPath + "/" + d.getFotoUrl());
                    }
                    if (imgFile.exists()) {
                        imgFile.delete();
                    }
                }

                boolean success = desaDAO.deleteDesa(idDesa);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data desa berhasil dihapus!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Refresh data lokal
                    refreshKecamatanComboBox();
                    refreshTable();

                    // PENTING: Notifikasi panel lain untuk refresh
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

        int idDesa = (int) table.getValueAt(row, 8);
        Desa d = desaDAO.getDesaById(idDesa);

        if (d != null) {
            // Ambil daftar RT/RW
            List<RTRW> rtrwList = rtrwDAO.getRTRWByDesaName(d.getNamaDesa());
            StringBuilder listRtrw = new StringBuilder("\n=== DAFTAR RT/RW ===\n");

            if (rtrwList.isEmpty()) {
                listRtrw.append("(Belum ada data)\n");
            } else {
                for (int i = 0; i < rtrwList.size(); i++) {
                    RTRW r = rtrwList.get(i);
                    listRtrw.append(String.format("%d. RT %s / RW %s (Ketua: %s)\n",
                            (i + 1), r.getRt(), r.getRw(),
                            r.getNamaKetua() != null ? r.getNamaKetua() : "-"));
                }
            }

            // Load gambar untuk ditampilkan di detail
            String imageInfo = "";
            if (d.getFotoUrl() != null && !d.getFotoUrl().trim().isEmpty()) {
                File imgFile = new File(d.getFotoUrl());
                if (!imgFile.exists()) {
                    String projectPath = System.getProperty("user.dir");
                    imgFile = new File(projectPath + "/" + d.getFotoUrl());
                }
                if (imgFile.exists()) {
                    imageInfo = "\n📸 Foto " + d.getJenis() + "    : " + d.getFotoUrl() + " (Ada)\n";
                } else {
                    imageInfo = "\n📸 Foto " + d.getJenis() + "    : File tidak ditemukan\n";
                }
            } else {
                imageInfo = "\n📸 Foto " + d.getJenis() + "    : Belum ada foto\n";
            }

            // Tentukan label berdasarkan jenis
            boolean isKelurahan = "KELURAHAN".equals(d.getJenis());
            String labelKepala = isKelurahan ? "Nama Lurah        " : "Nama Kepala Desa  ";

            String detail = String.format(
                    "========== DETAIL DATA %s ==========\n\n" +
                            "ID Desa              : %d\n" +
                            "Kecamatan            : %s\n" +
                            "Nama Desa/Kelurahan  : %s\n" +
                            "Jenis                : %s\n" +
                            "Alamat Kantor        : %s\n" +
                            "%s : %s\n" +
                            "No HP                : %s\n" +
                            "%s" +
                            "\n" +
                            "Dibuat pada          : %s\n" +
                            "Terakhir diupdate    : %s\n" +
                            "%s",
                    d.getJenis(),
                    d.getIdDesa(),
                    d.getNamaKecamatan() != null ? d.getNamaKecamatan() : "-",
                    d.getNamaDesa() != null ? d.getNamaDesa() : "-",
                    d.getJenis() != null ? d.getJenis() : "-",
                    d.getAlamatKantor() != null ? d.getAlamatKantor() : "-",
                    labelKepala, d.getNamaKepala() != null ? d.getNamaKepala() : "-",
                    d.getNoHp() != null ? d.getNoHp() : "-",
                    imageInfo,
                    d.getCreatedAt() != null ? d.getCreatedAt() : "-",
                    d.getUpdatedAt() != null ? d.getUpdatedAt() : "-",
                    listRtrw.toString());

            // Buat panel dengan scroll pane dan preview gambar jika ada
            JPanel detailPanel = new JPanel(new BorderLayout(10, 10));

            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));

            JScrollPane textScrollPane = new JScrollPane(textArea);
            textScrollPane.setPreferredSize(new Dimension(600, 450));
            detailPanel.add(textScrollPane, BorderLayout.CENTER);

            // Jika ada foto, tampilkan preview
            if (d.getFotoUrl() != null && !d.getFotoUrl().trim().isEmpty()) {
                File imgFile = new File(d.getFotoUrl());
                if (!imgFile.exists()) {
                    String projectPath = System.getProperty("user.dir");
                    imgFile = new File(projectPath + "/" + d.getFotoUrl());
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
                        lblFotoPreview.setBorder(BorderFactory.createTitledBorder("Preview " + d.getJenis()));

                        JPanel fotoPanel = new JPanel(new BorderLayout());
                        fotoPanel.add(lblFotoPreview, BorderLayout.CENTER);
                        fotoPanel.setPreferredSize(new Dimension(220, 180));
                        detailPanel.add(fotoPanel, BorderLayout.EAST);
                    } catch (Exception e) {
                        // Gagal load preview
                    }
                }
            }

            JScrollPane mainScroll = new JScrollPane(detailPanel);
            mainScroll.setPreferredSize(new Dimension(850, 550));

            JOptionPane.showMessageDialog(this,
                    mainScroll,
                    "Detail " + d.getJenis() + " - " + d.getNamaDesa(),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}