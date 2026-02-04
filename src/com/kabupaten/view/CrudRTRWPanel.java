package com.kabupaten.view;

import com.kabupaten.model.RTRW;
import com.kabupaten.dao.RTRWDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CrudRTRWPanel extends JPanel {
    private RTRWDAO rtrwDAO = new RTRWDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnTambah, btnEdit, btnHapus, btnDetail;

    public CrudRTRWPanel() {
        setLayout(new BorderLayout());

        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pencarian:"));
        txtSearch = new JTextField(20);

        searchPanel.add(txtSearch);
        add(searchPanel, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(
                new Object[] { "ID", "Desa", "RW", "RT", "Nama Ketua", "Kontak", "Alamat" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35); // Taller rows
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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
        btnExport.addActionListener(e -> com.kabupaten.utils.ExportUtils.exportToCSV(table, "RTRW"));
        buttonPanel.add(btnExport);

        // UI Polish - Table
        table.getTableHeader().setBackground(new Color(30, 60, 114));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(new Color(79, 172, 254));
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

        // Enter key tetap ada sebagai backup
        txtSearch.addActionListener(e -> searchData());

        // Load initial data
        refreshTable();

        // Debug: print semua data saat panel dibuat
        debugPrintAllData();

        // Cek apakah ada data duplikat existing
        checkExistingDuplicates();
    }

    public void setReadOnly(boolean readOnly) {
        btnTambah.setVisible(!readOnly);
        btnEdit.setVisible(!readOnly);
        btnHapus.setVisible(!readOnly);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        Set<Integer> addedIds = new HashSet<>(); // Mencegah duplikat

        try {
            for (RTRW rtrw : rtrwDAO.getAllRTRW()) {
                // Cek apakah ID sudah ditambahkan
                if (!addedIds.contains(rtrw.getIdRtrw())) {
                    tableModel.addRow(new Object[] {
                            rtrw.getIdRtrw(),
                            rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
                            rtrw.getRw(),
                            rtrw.getRt(),
                            rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
                            rtrw.getKontak() != null ? rtrw.getKontak() : "-",
                            rtrw.getAlamat() != null ? rtrw.getAlamat() : "-"
                    });
                    addedIds.add(rtrw.getIdRtrw());
                }
            }
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
        Set<Integer> addedIds = new HashSet<>(); // Mencegah duplikat

        try {
            for (RTRW rtrw : rtrwDAO.searchRTRW(keyword)) {
                // Cek apakah ID sudah ditambahkan
                if (!addedIds.contains(rtrw.getIdRtrw())) {
                    tableModel.addRow(new Object[] {
                            rtrw.getIdRtrw(),
                            rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
                            rtrw.getRw(),
                            rtrw.getRt(),
                            rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
                            rtrw.getKontak() != null ? rtrw.getKontak() : "-",
                            rtrw.getAlamat() != null ? rtrw.getAlamat() : "-"
                    });
                    addedIds.add(rtrw.getIdRtrw());
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error pencarian: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Validasi apakah kombinasi Desa-RT-RW sudah ada
     */
    private boolean isDuplicateRTRW(String namaDesa, String rt, String rw, Integer excludeId) {
        return rtrwDAO.isDuplicateRTRW(namaDesa, rt, rw, excludeId);
    }

    /**
     * Menampilkan semua data RTRW untuk debugging
     */
    private void debugPrintAllData() {
        try {
            List<RTRW> allData = rtrwDAO.getAllRTRW();
            System.out.println("\n=== SEMUA DATA RTRW ===");
            System.out.println("Total: " + allData.size() + " records");
            System.out.println("ID\tDesa\t\tRT\tRW");
            System.out.println("-------------------------------------------");

            for (RTRW rtrw : allData) {
                System.out.println(
                        rtrw.getIdRtrw() + "\t" +
                                (rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "NULL") + "\t\t" +
                                (rtrw.getRt() != null ? rtrw.getRt() : "NULL") + "\t" +
                                (rtrw.getRw() != null ? rtrw.getRw() : "NULL"));
            }
            System.out.println("===========================================\n");

        } catch (Exception e) {
            System.err.println("Error saat debug print: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Cek apakah ada data duplikat di database dan tampilkan
     */
    private void checkExistingDuplicates() {
        try {
            List<RTRW> allData = rtrwDAO.getAllRTRW();
            Set<String> uniqueKeys = new HashSet<>();
            List<String> duplicatesRTRW = new ArrayList<>();

            // Cek duplikat Desa-RT-RW
            for (RTRW rtrw : allData) {
                String desa = rtrw.getNamaDesa() != null ? rtrw.getNamaDesa().toLowerCase().trim() : "";
                String rt = rtrw.getRt() != null ? rtrw.getRt().toLowerCase().trim() : "";
                String rw = rtrw.getRw() != null ? rtrw.getRw().toLowerCase().trim() : "";
                //
                String key = desa + "|" + rt + "|" + rw;

                if (uniqueKeys.contains(key)) {
                    duplicatesRTRW.add("ID " + rtrw.getIdRtrw() + ": " +
                            rtrw.getNamaDesa() + " RT " + rtrw.getRt() + " RW " + rtrw.getRw());
                } else {
                    uniqueKeys.add(key);
                }
            }

            // Tampilkan hasil
            if (!duplicatesRTRW.isEmpty()) {
                System.out.println("\n!!! PERINGATAN: Ditemukan duplikat Desa-RT-RW !!!");
                for (String dup : duplicatesRTRW) {
                    System.out.println("- " + dup);
                }
            }

        } catch (Exception e) {
            System.err.println("Error saat cek duplikat existing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void tambahData() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField txtDesa = new JTextField();
        JTextField txtRW = new JTextField();
        JTextField txtRT = new JTextField();
        JTextField txtNamaKetua = new JTextField();
        JTextField txtKontak = new JTextField();
        JTextArea txtAlamat = new JTextArea(3, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);

        panel.add(new JLabel("Desa:*"));
        panel.add(txtDesa);
        panel.add(new JLabel("RW:*"));
        panel.add(txtRW);
        panel.add(new JLabel("RT:*"));
        panel.add(txtRT);
        panel.add(new JLabel("Nama Ketua:"));
        panel.add(txtNamaKetua);
        panel.add(new JLabel("Kontak:"));
        panel.add(txtKontak);
        panel.add(new JLabel("Alamat:*"));
        panel.add(scrollAlamat);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Tambah Data RTRW",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String namaDesa = txtDesa.getText().trim();
                String rwStr = txtRW.getText().trim();
                String rtStr = txtRT.getText().trim();
                String namaKetua = txtNamaKetua.getText().trim();
                String kontak = txtKontak.getText().trim();
                String alamat = txtAlamat.getText().trim();

                // Validasi field wajib
                if (namaDesa.isEmpty() || rwStr.isEmpty() || rtStr.isEmpty() || alamat.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Desa, RW, RT, dan Alamat wajib diisi!",
                            "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validasi format angka untuk RW dan RT
                if (!rwStr.matches("\\d+") || !rtStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this,
                            "RW dan RT harus berupa angka!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int rw = Integer.parseInt(rwStr);
                int rt = Integer.parseInt(rtStr);

                // Validasi rentang nilai
                if (rw <= 0 || rt <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "RW dan RT harus lebih dari 0!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Format RT dan RW dengan padding 0
                String rtFormatted = String.format("%03d", rt);
                String rwFormatted = String.format("%03d", rw);

                // Cek duplikat
                if (isDuplicateRTRW(namaDesa, rtFormatted, rwFormatted, null)) {
                    JOptionPane.showMessageDialog(this,
                            "Data RT/RW untuk desa ini sudah ada!\nDesa: " + namaDesa + ", RT: " + rtFormatted
                                    + ", RW: " + rwFormatted,
                            "Data Duplikat",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Membuat objek RTRW
                RTRW rtrw = new RTRW();
                rtrw.setNamaDesa(namaDesa);
                rtrw.setRt(rtFormatted);
                rtrw.setRw(rwFormatted);
                rtrw.setNamaKetua(namaKetua);
                rtrw.setKontak(kontak);
                rtrw.setAlamat(alamat);

                boolean success = rtrwDAO.addRTRW(rtrw);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data berhasil ditambahkan!\nDesa: " + namaDesa + "\nRT: " + rtFormatted + "\nRW: "
                                    + rwFormatted);
                    refreshTable();
                    txtSearch.setText(""); // Clear search
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal menambahkan data!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "RT dan RW harus berupa angka yang valid!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
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
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih data yang akan diedit!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        RTRW rtrw = rtrwDAO.getRTRWById(id);

        if (rtrw == null) {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create panel with form fields
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField txtDesa = new JTextField(rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "");
        JTextField txtRW = new JTextField(rtrw.getRw());
        JTextField txtRT = new JTextField(rtrw.getRt());
        JTextField txtNamaKetua = new JTextField(rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "");
        JTextField txtKontak = new JTextField(rtrw.getKontak() != null ? rtrw.getKontak() : "");
        JTextArea txtAlamat = new JTextArea(rtrw.getAlamat() != null ? rtrw.getAlamat() : "", 3, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);

        panel.add(new JLabel("Desa:*"));
        panel.add(txtDesa);
        panel.add(new JLabel("RW:*"));
        panel.add(txtRW);
        panel.add(new JLabel("RT:*"));
        panel.add(txtRT);
        panel.add(new JLabel("Nama Ketua:"));
        panel.add(txtNamaKetua);
        panel.add(new JLabel("Kontak:"));
        panel.add(txtKontak);
        panel.add(new JLabel("Alamat:*"));
        panel.add(scrollAlamat);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Edit Data RTRW",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String namaDesa = txtDesa.getText().trim();
                String rwStr = txtRW.getText().trim();
                String rtStr = txtRT.getText().trim();
                String namaKetua = txtNamaKetua.getText().trim();
                String kontak = txtKontak.getText().trim();
                String alamat = txtAlamat.getText().trim();

                // Validasi field wajib
                if (namaDesa.isEmpty() || rwStr.isEmpty() || rtStr.isEmpty() || alamat.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Desa, RW, RT, dan Alamat wajib diisi!",
                            "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validasi format angka
                if (!rwStr.matches("\\d+") || !rtStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this,
                            "RW dan RT harus berupa angka!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int rw = Integer.parseInt(rwStr);
                int rt = Integer.parseInt(rtStr);

                // Validasi rentang nilai
                if (rw <= 0 || rt <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "RW dan RT harus lebih dari 0!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Format RT dan RW
                String rtFormatted = String.format("%03d", rt);
                String rwFormatted = String.format("%03d", rw);

                // Cek duplikat (exclude data yang sedang diedit)
                if (isDuplicateRTRW(namaDesa, rtFormatted, rwFormatted, id)) {
                    JOptionPane.showMessageDialog(this,
                            "Data RT/RW untuk desa ini sudah ada!\nDesa: " + namaDesa + ", RT: " + rtFormatted
                                    + ", RW: " + rwFormatted,
                            "Data Duplikat",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Update object
                rtrw.setNamaDesa(namaDesa);
                rtrw.setRt(rtFormatted);
                rtrw.setRw(rwFormatted);
                rtrw.setNamaKetua(namaKetua);
                rtrw.setKontak(kontak);
                rtrw.setAlamat(alamat);

                boolean success = rtrwDAO.updateRTRW(rtrw);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Data berhasil diupdate!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal mengupdate data!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "RT dan RW harus berupa angka yang valid!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
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
                    "Silakan pilih data yang akan dihapus!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menghapus data ini?\nData yang sudah dihapus tidak dapat dikembalikan!",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            int id = (Integer) table.getValueAt(selectedRow, 0);
            try {
                boolean success = rtrwDAO.deleteRTRW(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    refreshTable();
                    txtSearch.setText(""); // Clear search
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
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih data untuk melihat detail!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        RTRW rtrw = rtrwDAO.getRTRWById(id);

        if (rtrw != null) {
            String detail = String.format(
                    "=== DETAIL RTRW ===\n\n" +
                            "ID: %d\n" +
                            "Desa: %s\n" +
                            "RT: %s\n" +
                            "RW: %s\n" +
                            "Nama Ketua: %s\n" +
                            "Kontak: %s\n" +
                            "Alamat: %s\n" +
                            "Dibuat: %s\n" +
                            "Diupdate: %s",
                    rtrw.getIdRtrw(),
                    rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
                    rtrw.getRt(),
                    rtrw.getRw(),
                    rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
                    rtrw.getKontak() != null ? rtrw.getKontak() : "-",
                    rtrw.getAlamat() != null ? rtrw.getAlamat() : "-",
                    rtrw.getCreatedAt(),
                    rtrw.getUpdatedAt());

            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Detail RTRW",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Data tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}