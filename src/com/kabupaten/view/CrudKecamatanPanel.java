package com.kabupaten.view;

import com.kabupaten.model.RTRWMODEL;
import com.kabupaten.services.RTRWSERVICES;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel CRUD untuk data Warga
 * 
 * PERBAIKAN:
 * 1. Mengganti r.getKecamatan() menjadi r.getDesa() (sesuai model)
 * 2. Memperbaiki parameter addRTRW() menjadi 7 parameter
 * 3. Menambahkan validasi input
 * 4. Menambahkan error handling yang proper
 * 5. Menambahkan fitur search
 */
public class CrudKecamatanPanel extends JPanel {
    private RTRWSERVICES service = new RTRWSERVICES();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public CrudKecamatanPanel() {
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
            new Object[]{"ID", "Desa", "RW", "RT", "Alamat", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol CRUD
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnDetail = new JButton("Detail");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnDetail);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnDetail.addActionListener(e -> showDetail());
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
            for (RTRWMODEL r : service.getAllRTRWModel()) {
                // PERBAIKAN: Ganti getKecamatan() dengan getDesa()
                tableModel.addRow(new Object[]{
                    r.getId(), 
                    r.getDesa() != null ? r.getDesa() : "-",  // FIX: getDesa() bukan getKecamatan()
                    r.getRw() != null ? r.getRw() : "-", 
                    r.getRt() != null ? r.getRt() : "-", 
                    r.getAlamat() != null ? r.getAlamat() : "-", 
                    r.getStatus() != null ? r.getStatus() : "Aktif"
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
    
    private void searchData() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }
        
        tableModel.setRowCount(0);
        try {
            for (RTRWMODEL r : service.getAllRTRWModel()) {
                String desa = r.getDesa() != null ? r.getDesa().toLowerCase() : "";
                String alamat = r.getAlamat() != null ? r.getAlamat().toLowerCase() : "";
                String namaKetua = r.getNamaKetua() != null ? r.getNamaKetua().toLowerCase() : "";
                String keywordLower = keyword.toLowerCase();
                
                if (desa.contains(keywordLower) || 
                    alamat.contains(keywordLower) || 
                    namaKetua.contains(keywordLower)) {
                    tableModel.addRow(new Object[]{
                        r.getId(), 
                        r.getDesa(), 
                        r.getRw(), 
                        r.getRt(), 
                        r.getAlamat(), 
                        r.getStatus()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error pencarian: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
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
            "Tambah Data Warga",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String desa = txtDesa.getText().trim();
                String rwStr = txtRW.getText().trim();
                String rtStr = txtRT.getText().trim();
                String namaKetua = txtNamaKetua.getText().trim();
                String kontak = txtKontak.getText().trim();
                String alamat = txtAlamat.getText().trim();

                // Validasi input wajib
                if (desa.isEmpty() || rwStr.isEmpty() || rtStr.isEmpty() || alamat.isEmpty()) {
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

                // PERBAIKAN: Tambahkan parameter namaKetua dan kontak (total 7 parameter)
                boolean success = service.addRTRW(desa, rw, rt, namaKetua, kontak, alamat, "Aktif");
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                    refreshTable();
                    txtSearch.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menambahkan data!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "RW dan RT harus berupa angka yang valid!", 
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
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih data yang akan diedit!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) table.getValueAt(row, 0);
        
        // Ambil data dari service
        RTRWMODEL rtrw = service.getAllRTRWModel().stream()
            .filter(r -> r.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (rtrw == null) {
            JOptionPane.showMessageDialog(this, 
                "Data tidak ditemukan!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField txtDesa = new JTextField(rtrw.getDesa() != null ? rtrw.getDesa() : "");
        JTextField txtRW = new JTextField(rtrw.getRw() != null ? rtrw.getRw() : "");
        JTextField txtRT = new JTextField(rtrw.getRt() != null ? rtrw.getRt() : "");
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
            "Edit Data Warga",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String desa = txtDesa.getText().trim();
                String rwStr = txtRW.getText().trim();
                String rtStr = txtRT.getText().trim();
                String alamat = txtAlamat.getText().trim();

                // Validasi
                if (desa.isEmpty() || rwStr.isEmpty() || rtStr.isEmpty() || alamat.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Desa, RW, RT, dan Alamat wajib diisi!", 
                        "Peringatan", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!rwStr.matches("\\d+") || !rtStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, 
                        "RW dan RT harus berupa angka!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int rw = Integer.parseInt(rwStr);
                int rt = Integer.parseInt(rtStr);

                boolean success = service.updateRTRW(id, desa, rw, rt, alamat, "Aktif");
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal mengupdate data!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "RW dan RT harus berupa angka yang valid!", 
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
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih data yang akan dihapus!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) table.getValueAt(row, 0);
            try {
                boolean success = service.deleteRTRW(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    refreshTable();
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

        int id = (int) table.getValueAt(row, 0);
        
        RTRWMODEL rtrw = service.getAllRTRWModel().stream()
            .filter(r -> r.getId() == id)
            .findFirst()
            .orElse(null);
        
        if (rtrw != null) {
            String detail = String.format(
                "=== DETAIL DATA WARGA ===\n\n" +
                "ID: %d\n" +
                "Desa: %s\n" +
                "RT: %s\n" +
                "RW: %s\n" +
                "Nama Ketua: %s\n" +
                "Kontak: %s\n" +
                "Alamat: %s\n" +
                "Status: %s\n" +
                "Dibuat: %s\n" +
                "Diupdate: %s",
                rtrw.getId(),
                rtrw.getDesa() != null ? rtrw.getDesa() : "-",
                rtrw.getRt() != null ? rtrw.getRt() : "-",
                rtrw.getRw() != null ? rtrw.getRw() : "-",
                rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
                rtrw.getKontak() != null ? rtrw.getKontak() : "-",
                rtrw.getAlamat() != null ? rtrw.getAlamat() : "-",
                rtrw.getStatus() != null ? rtrw.getStatus() : "Aktif",
                rtrw.getCreatedAt() != null ? rtrw.getCreatedAt() : "-",
                rtrw.getUpdatedAt() != null ? rtrw.getUpdatedAt() : "-"
            );
            
            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Detail Data Warga", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Data tidak ditemukan!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}