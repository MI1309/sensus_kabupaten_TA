package com.kabupaten.view;

import com.kabupaten.model.RTRW;
import com.kabupaten.services.RTRWSERVICES;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CrudDesaPanel extends JPanel {
    private RTRWSERVICES service = new RTRWSERVICES();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public CrudDesaPanel() {
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
            new Object[]{"ID", "Desa/Kecamatan", "RW", "RT", "Nama Ketua", "Kontak", "Alamat"}, 0
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
            for (RTRW rtrw : service.getAllRTRW()) {
                tableModel.addRow(new Object[]{
                    rtrw.getIdRtrw(),
                    rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
                    rtrw.getRw(),
                    rtrw.getRt(),
                    rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
                    rtrw.getKontak() != null ? rtrw.getKontak() : "-",
                    rtrw.getAlamat() != null ? rtrw.getAlamat() : "-"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error memuat data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
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
            for (RTRW rtrw : service.searchRTRW(keyword)) {
                tableModel.addRow(new Object[]{
                    rtrw.getIdRtrw(),
                    rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
                    rtrw.getRw(),
                    rtrw.getRt(),
                    rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
                    rtrw.getKontak() != null ? rtrw.getKontak() : "-",
                    rtrw.getAlamat() != null ? rtrw.getAlamat() : "-"
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
        String kecamatan = JOptionPane.showInputDialog(this, "Kecamatan/Desa:");
        if (kecamatan == null || kecamatan.trim().isEmpty()) return;
        
        String rwStr = JOptionPane.showInputDialog(this, "RW:");
        if (rwStr == null || rwStr.trim().isEmpty()) return;
        
        String rtStr = JOptionPane.showInputDialog(this, "RT:");
        if (rtStr == null || rtStr.trim().isEmpty()) return;
        
        String namaKetua = JOptionPane.showInputDialog(this, "Nama Ketua (opsional):");
        String kontak = JOptionPane.showInputDialog(this, "Kontak (opsional):");
        String alamat = JOptionPane.showInputDialog(this, "Alamat:");
        
        try {
            int rw = Integer.parseInt(rwStr);
            int rt = Integer.parseInt(rtStr);
            
            boolean success = service.addRTRW(kecamatan, rw, rt, alamat, "Aktif");
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "RT dan RW harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang akan diedit!");
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        RTRW rtrw = service.getRTRWById(id);
        
        if (rtrw == null) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String kecamatan = JOptionPane.showInputDialog(this, "Kecamatan/Desa:", 
            rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "");
        if (kecamatan == null) return;
        
        String rwStr = JOptionPane.showInputDialog(this, "RW:", rtrw.getRw());
        if (rwStr == null) return;
        
        String rtStr = JOptionPane.showInputDialog(this, "RT:", rtrw.getRt());
        if (rtStr == null) return;
        
        String namaKetua = JOptionPane.showInputDialog(this, "Nama Ketua:", 
            rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "");
        
        String kontak = JOptionPane.showInputDialog(this, "Kontak:", 
            rtrw.getKontak() != null ? rtrw.getKontak() : "");
        
        String alamat = JOptionPane.showInputDialog(this, "Alamat:", 
            rtrw.getAlamat() != null ? rtrw.getAlamat() : "");
        
        try {
            int rw = Integer.parseInt(rwStr);
            int rt = Integer.parseInt(rtStr);
            
            boolean success = service.updateRTRW(id, kecamatan, rw, rt, alamat, "Aktif");
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "RT dan RW harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang akan dihapus!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (Integer) table.getValueAt(selectedRow, 0);
            try {
                boolean success = service.deleteRTRW(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data untuk melihat detail!");
            return;
        }

        int id = (Integer) table.getValueAt(selectedRow, 0);
        RTRW rtrw = service.getRTRWById(id);
        
        if (rtrw != null) {
            String detail = String.format(
                "=== DETAIL RTRW ===\n\n" +
                "ID: %d\n" +
                "Desa/Kecamatan: %s\n" +
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
                rtrw.getUpdatedAt()
            );
            
            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Detail RTRW", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}