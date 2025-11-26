package com.kabupaten.view;
import com.kabupaten.model.Kecamatan;
import com.kabupaten.dao.KecamatanDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel CRUD untuk data Kecamatan
 */
public class CrudWargaPanel extends JPanel {
    private KecamatanDAO dao = new KecamatanDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public CrudWargaPanel() {
        setLayout(new BorderLayout());

        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Pencarian:"));
        txtSearch = new JTextField(20);
        // JButton btnRefresh = new JButton("Refresh");
        
        searchPanel.add(txtSearch);
        // searchPanel.add(btnRefresh);
        add(searchPanel, BorderLayout.NORTH);

        // Tabel
        tableModel = new DefaultTableModel(
            new Object[]{"ID Kecamatan", "Nama Kecamatan", "Alamat Kantor Kecamatan", "Nama Kepala Kecamatan", "Alamat Rumah Kepala Kecamatan", "No HP"}, 0
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

        btnTambah.setBackground(new Color(46, 125, 50));
        btnTambah.setForeground(Color.WHITE);
        btnEdit.setBackground(new Color(33, 150, 243));
        btnEdit.setForeground(Color.WHITE);
        btnHapus.setBackground(new Color(244, 67, 54));
        btnHapus.setForeground(Color.WHITE);
        btnDetail.setBackground(new Color(158, 158, 158));
        btnDetail.setForeground(Color.WHITE);

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
        // btnRefresh.addActionListener(e -> refreshTable());
        
        // Live search menggunakan DocumentListener
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

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Kecamatan> kecamatanList = dao.getAllKecamatan();
            for (Kecamatan k : kecamatanList) {
                tableModel.addRow(new Object[]{
                    k.getIdKecamatan(), 
                    k.getNamaKecamatan() != null ? k.getNamaKecamatan() : "-",
                    k.getAlamatKantor() != null ? k.getAlamatKantor() : "-",
                    k.getNamaKepala() != null ? k.getNamaKepala() : "-",
                    k.getAlamatRumahKepala() != null ? k.getAlamatRumahKepala() : "-",
                    k.getNoHp() != null ? k.getNoHp() : "-"
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
            List<Kecamatan> kecamatanList = dao.searchKecamatan(keyword);
            for (Kecamatan k : kecamatanList) {
                tableModel.addRow(new Object[]{
                    k.getIdKecamatan(), 
                    k.getNamaKecamatan() != null ? k.getNamaKecamatan() : "-",
                    k.getAlamatKantor() != null ? k.getAlamatKantor() : "-",
                    k.getNamaKepala() != null ? k.getNamaKepala() : "-",
                    k.getAlamatRumahKepala() != null ? k.getAlamatRumahKepala() : "-",
                    k.getNoHp() != null ? k.getNoHp() : "-"
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
        KecamatanFormDialog dialog = new KecamatanFormDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Tambah Data Kecamatan",
            true
        );
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Kecamatan kecamatan = dialog.getKecamatan();
            try {
                boolean success = dao.addKecamatan(kecamatan);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Data berhasil ditambahkan!", 
                        "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                    txtSearch.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menambahkan data! Nama kecamatan mungkin sudah ada.", 
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
        
        int id = (int) table.getValueAt(row, 0);
        
        try {
            Kecamatan kecamatan = dao.getKecamatanById(id);
            
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
                kecamatan
            );
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Kecamatan updatedKecamatan = dialog.getKecamatan();
                boolean success = dao.updateKecamatan(updatedKecamatan);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Data berhasil diupdate!", 
                        "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal mengupdate data! Nama kecamatan mungkin sudah ada.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
        
        String namaKecamatan = table.getValueAt(row, 1).toString();
        
        int result = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus data kecamatan:\n" + namaKecamatan + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            int id = (int) table.getValueAt(row, 0);
            try {
                boolean success = dao.deleteKecamatan(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Data berhasil dihapus!", 
                        "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
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
        
        try {
            Kecamatan kecamatan = dao.getKecamatanById(id);
            
            if (kecamatan != null) {
                String detail = String.format(
                    "=== DETAIL DATA KECAMATAN ===\n\n" +
                    "ID Kecamatan: %d\n" +
                    "Nama Kecamatan: %s\n" +
                    "Alamat Kantor: %s\n" +
                    "Nama Kepala: %s\n" +
                    "Alamat Rumah Kepala: %s\n" +
                    "No HP: %s\n\n" +
                    "Dibuat: %s\n" +
                    "Diupdate: %s",
                    kecamatan.getIdKecamatan(),
                    kecamatan.getNamaKecamatan() != null ? kecamatan.getNamaKecamatan() : "-",
                    kecamatan.getAlamatKantor() != null ? kecamatan.getAlamatKantor() : "-",
                    kecamatan.getNamaKepala() != null ? kecamatan.getNamaKepala() : "-",
                    kecamatan.getAlamatRumahKepala() != null ? kecamatan.getAlamatRumahKepala() : "-",
                    kecamatan.getNoHp() != null ? kecamatan.getNoHp() : "-",
                    kecamatan.getCreatedAt() != null ? kecamatan.getCreatedAt() : "-",
                    kecamatan.getUpdatedAt() != null ? kecamatan.getUpdatedAt() : "-"
                );
                
                JTextArea textArea = new JTextArea(detail);
                textArea.setEditable(false);
                textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(450, 300));
                
                JOptionPane.showMessageDialog(this, 
                    scrollPane, 
                    "Detail Data Kecamatan", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Data tidak ditemukan!", 
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