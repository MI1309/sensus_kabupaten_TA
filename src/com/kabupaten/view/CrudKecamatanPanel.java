package com.kabupaten.view;

import com.kabupaten.model.Kecamatan;
import com.kabupaten.model.Desa;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.dao.DesaDAO;
import com.kabupaten.listener.DataChangeListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    // PERBAIKAN: Hanya satu deklarasi field
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    public CrudKecamatanPanel() {
        setLayout(new BorderLayout());

        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pencarian:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Tabel data Kecamatan
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Nama Kecamatan", "Alamat Kantor", "Nama Kepala",
                        "No HP", "Jumlah Desa", "Jumlah Kelurahan", "Total" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35); // Taller rows
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);

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
        btnExport.addActionListener(e -> com.kabupaten.utils.ExportUtils.exportToCSV(table, "Kecamatan"));
        buttonPanel.add(btnExport);

        // UI Polish - Table
        table.getTableHeader().setBackground(new Color(30, 60, 114)); // Header Background
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(new Color(79, 172, 254)); // Selection Color
        table.setGridColor(new Color(230, 230, 230));

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
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                tambahData();
            }
        });
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                editData();
            }
        });
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                hapusData();
            }
        });
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showDetail();
            }
        });

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

    // ========== LISTENER METHODS (PENTING!) ==========
    public void addDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    private void notifyDataChanged() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    // PENTING: Method ini HARUS ada
    public void refreshData() {
        refreshTable();
    }

    // ========== PRIVATE METHODS ==========
    private void refreshTable() {
        tableModel.setRowCount(0);
        int totalDesa = 0;
        int totalKelurahan = 0;

        try {
            List<Kecamatan> kecamatanList = kecamatanDAO.getAllKecamatan();
            for (Kecamatan kec : kecamatanList) {
                int jmlDesa = kec.getJumlahDesa();
                int jmlKelurahan = kec.getJumlahKelurahan();
                int total = kec.getTotalWilayahAdministratif();

                totalDesa += jmlDesa;
                totalKelurahan += jmlKelurahan;

                tableModel.addRow(new Object[] {
                        kec.getIdKecamatan(),
                        kec.getNamaKecamatan() != null ? kec.getNamaKecamatan() : "-",
                        kec.getAlamatKantor() != null ? kec.getAlamatKantor() : "-",
                        kec.getNamaKepala() != null ? kec.getNamaKepala() : "-",
                        kec.getNoHp() != null ? kec.getNoHp() : "-",
                        jmlDesa,
                        jmlKelurahan,
                        total
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
            for (Kecamatan kec : kecamatanList) {
                int jmlDesa = kec.getJumlahDesa();
                int jmlKelurahan = kec.getJumlahKelurahan();
                int total = kec.getTotalWilayahAdministratif();

                totalDesa += jmlDesa;
                totalKelurahan += jmlKelurahan;

                tableModel.addRow(new Object[] {
                        kec.getIdKecamatan(),
                        kec.getNamaKecamatan() != null ? kec.getNamaKecamatan() : "-",
                        kec.getAlamatKantor() != null ? kec.getAlamatKantor() : "-",
                        kec.getNamaKepala() != null ? kec.getNamaKepala() : "-",
                        kec.getNoHp() != null ? kec.getNoHp() : "-",
                        jmlDesa,
                        jmlKelurahan,
                        total
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
                    notifyDataChanged(); // Notify listeners
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

        int idKecamatan = (int) table.getValueAt(row, 0);
        Kecamatan kecamatan = kecamatanDAO.getKecamatanById(idKecamatan);

        if (kecamatan == null) {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        KecamatanFormDialog dialog = new KecamatanFormDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Edit Data Kecamatan",
                true,
                kecamatan);

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Kecamatan updatedKecamatan = dialog.getKecamatan();

            try {
                boolean success = kecamatanDAO.updateKecamatan(updatedKecamatan);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data kecamatan berhasil diupdate!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    notifyDataChanged(); // Notify listeners
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

        int idKecamatan = (int) table.getValueAt(row, 0);
        String namaKecamatan = table.getValueAt(row, 1).toString();
        int jumlahDesa = (int) table.getValueAt(row, 5);
        int jumlahKelurahan = (int) table.getValueAt(row, 6);
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
                boolean success = kecamatanDAO.deleteKecamatan(idKecamatan);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data kecamatan berhasil dihapus!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    notifyDataChanged(); // Notify listeners
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

        int idKecamatan = (int) table.getValueAt(row, 0);
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

            String detail = String.format(
                    "========== DETAIL DATA KECAMATAN ==========\n\n" +
                            "ID Kecamatan         : %d\n" +
                            "Nama Kecamatan       : %s\n" +
                            "Alamat Kantor        : %s\n" +
                            "Nama Kepala          : %s\n" +
                            "Alamat Rumah Kepala  : %s\n" +
                            "No HP                : %s\n\n" +
                            "=== STATISTIK WILAYAH ===\n" +
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
                    kec.getAlamatRumahKepala() != null ? kec.getAlamatRumahKepala() : "-",
                    kec.getNoHp() != null ? kec.getNoHp() : "-",
                    kec.getJumlahDesa(),
                    kec.getJumlahKelurahan(),
                    kec.getTotalWilayahAdministratif(),
                    kec.getCreatedAt() != null ? kec.getCreatedAt() : "-",
                    kec.getUpdatedAt() != null ? kec.getUpdatedAt() : "-",
                    listDesa.toString());

            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(550, 450));

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