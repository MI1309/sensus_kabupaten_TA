package com.kabupaten.view;

import com.kabupaten.dao.DesaDAO;
import com.kabupaten.dao.WargaDAO;
import com.kabupaten.model.Desa;
import com.kabupaten.model.Warga;
import com.kabupaten.util.ValidationUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class CrudWargaPanel extends JPanel {
    private WargaDAO wargaDAO = new WargaDAO();
    private DesaDAO desaDAO = new DesaDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnTambah, btnEdit, btnHapus, btnDetail;

    public CrudWargaPanel() {
        setLayout(new BorderLayout());

        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pencarian:"));
        txtSearch = new JTextField(20);

        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "NIK", "Nama Lengkap", "L/P", "Desa", "Alamat" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35); // Taller rows
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
        btnExport.addActionListener(e -> com.kabupaten.utils.ExportUtils.exportToCSV(table, "Warga"));
        buttonPanel.add(btnExport);

        // UI Polish - Table
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(new Color(79, 172, 254));
        table.setGridColor(new Color(230, 230, 230));

        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        btnTambah.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0);
                showForm(wargaDAO.getWargaById(id));
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan diedit!");
            }
        });
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

        refreshTable();
    }

    public void setReadOnly(boolean readOnly) {
        btnTambah.setVisible(!readOnly);
        btnEdit.setVisible(!readOnly);
        btnHapus.setVisible(!readOnly);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Warga> list = wargaDAO.getAllWarga();
        for (Warga w : list) {
            tableModel.addRow(new Object[] {
                    w.getIdWarga(), w.getNik(), w.getNamaLengkap(), w.getJenisKelamin(), w.getNamaDesa(), w.getAlamat()
            });
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Warga> list = wargaDAO.searchWarga(keyword);
        for (Warga w : list) {
            tableModel.addRow(new Object[] {
                    w.getIdWarga(), w.getNik(), w.getNamaLengkap(), w.getJenisKelamin(), w.getNamaDesa(), w.getAlamat()
            });
        }
    }

    private void showForm(Warga existing) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField txtNik = new JTextField(existing != null ? existing.getNik() : "");
        JTextField txtNama = new JTextField(existing != null ? existing.getNamaLengkap() : "");
        JComboBox<String> cmbJK = new JComboBox<>(new String[] { "L", "P" });
        if (existing != null)
            cmbJK.setSelectedItem(existing.getJenisKelamin());

        JTextField txtTempat = new JTextField(existing != null ? existing.getTempatLahir() : "");
        JTextField txtTgl = new JTextField(existing != null ? existing.getTanggalLahir().toString() : "YYYY-MM-DD");
        JTextField txtAlamat = new JTextField(existing != null ? existing.getAlamat() : "");

        List<Desa> desas = desaDAO.getAllDesa();
        JComboBox<DesaItem> cmbDesa = new JComboBox<>();
        for (Desa d : desas) {
            DesaItem item = new DesaItem(d.getIdDesa(), d.getNamaDesa());
            cmbDesa.addItem(item);
            if (existing != null && existing.getIdDesa() == d.getIdDesa()) {
                cmbDesa.setSelectedItem(item);
            }
        }

        panel.add(new JLabel("NIK:"));
        panel.add(txtNik);
        panel.add(new JLabel("Nama Lengkap:"));
        panel.add(txtNama);
        panel.add(new JLabel("Jenis Kelamin:"));
        panel.add(cmbJK);
        panel.add(new JLabel("Tempat Lahir:"));
        panel.add(txtTempat);
        panel.add(new JLabel("Tgl Lahir (YYYY-MM-DD):"));
        panel.add(txtTgl);
        panel.add(new JLabel("Alamat:"));
        panel.add(txtAlamat);
        panel.add(new JLabel("Desa:"));
        panel.add(cmbDesa);

        // Real-time input filtering
        ValidationUtils.applyNumericFilter(txtNik, 16);
        ValidationUtils.applyStringOnlyFilter(txtNama);
        ValidationUtils.applyStringOnlyFilter(txtTempat);

        // Focus Traversal
        txtNik.addActionListener(e -> txtNama.requestFocus());
        txtNama.addActionListener(e -> txtTempat.requestFocus());
        txtTempat.addActionListener(e -> txtTgl.requestFocus());
        txtTgl.addActionListener(e -> txtAlamat.requestFocus());
        txtAlamat.addActionListener(e -> cmbDesa.requestFocus());

        int result = JOptionPane.showConfirmDialog(this, panel, existing == null ? "Tambah Warga" : "Edit Warga",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nik = txtNik.getText().trim();
                String nama = txtNama.getText().trim();
                String tglLahir = txtTgl.getText().trim();

                // Validasi NIK
                if (nik.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "NIK tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!nik.matches("\\d{16}")) {
                    JOptionPane.showMessageDialog(this, "NIK harus berupa 16 digit angka!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validasi Nama
                if (nama.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!nama.matches("[a-zA-Z\\s]*")) {
                    JOptionPane.showMessageDialog(this, "Nama hanya boleh berisi huruf dan spasi!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validasi Tanggal
                try {
                    Date.valueOf(tglLahir);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Format tanggal lahir tidak valid (YYYY-MM-DD)!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Warga w = existing == null ? new Warga() : existing;
                w.setNik(nik);
                w.setNamaLengkap(nama);
                w.setJenisKelamin((String) cmbJK.getSelectedItem());
                w.setTempatLahir(txtTempat.getText().trim());
                w.setTanggalLahir(Date.valueOf(tglLahir));
                w.setAlamat(txtAlamat.getText().trim());
                w.setIdDesa(((DesaItem) cmbDesa.getSelectedItem()).id);

                boolean success = (existing == null) ? wargaDAO.addWarga(w) : wargaDAO.updateWarga(w);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Berhasil simpan data!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal simpan data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) table.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (wargaDAO.deleteWarga(id)) {
                    refreshTable();
                }
            }
        }
    }

    private void showDetail() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) table.getValueAt(row, 0);
            Warga w = wargaDAO.getWargaById(id);
            if (w != null) {
                String detail = String.format(
                        "========== DETAIL DATA WARGA ==========\n\n" +
                                "NIK              : %s\n" +
                                "Nama Lengkap     : %s\n" +
                                "Jenis Kelamin    : %s\n" +
                                "Tempat Lahir     : %s\n" +
                                "Tanggal Lahir    : %s\n" +
                                "Alamat           : %s\n" +
                                "Desa/Kelurahan   : %s\n" +
                                "Kecamatan        : %s\n\n" +
                                "Dibuat pada      : %s\n" +
                                "Terakhir diupdate: %s",
                        w.getNik() != null ? w.getNik() : "-",
                        w.getNamaLengkap() != null ? w.getNamaLengkap() : "-",
                        w.getJenisKelamin() != null ? (w.getJenisKelamin().equals("L") ? "Laki-laki" : "Perempuan")
                                : "-",
                        w.getTempatLahir() != null ? w.getTempatLahir() : "-",
                        w.getTanggalLahir() != null ? w.getTanggalLahir() : "-",
                        w.getAlamat() != null ? w.getAlamat() : "-",
                        w.getNamaDesa() != null ? w.getNamaDesa() : "-",
                        w.getNamaKecamatan() != null ? w.getNamaKecamatan() : "-",
                        w.getCreatedAt() != null ? w.getCreatedAt() : "-",
                        w.getUpdatedAt() != null ? w.getUpdatedAt() : "-");

                JTextArea textArea = new JTextArea(detail);
                textArea.setEditable(false);
                textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 350));

                JOptionPane.showMessageDialog(this,
                        scrollPane,
                        "Detail Warga - " + w.getNamaLengkap(),
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Silakan pilih data untuk melihat detail!", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private class DesaItem {
        int id;
        String nama;

        DesaItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        @Override
        public String toString() {
            return nama;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DesaItem)
                return ((DesaItem) o).id == id;
            return false;
        }
    }
}