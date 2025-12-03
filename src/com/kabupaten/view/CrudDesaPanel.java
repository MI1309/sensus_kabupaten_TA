package com.kabupaten.view;
import com.kabupaten.model.Desa;
import com.kabupaten.model.Kecamatan;
import com.kabupaten.dao.DesaDAO;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.listener.DataChangeListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Panel CRUD untuk data Desa dengan Filter Kecamatan
 */
public class CrudDesaPanel extends JPanel {
    private DesaDAO desaDAO = new DesaDAO();
    private KecamatanDAO kecamatanDAO = new KecamatanDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<String> cmbKecamatanFilter;
    private JComboBox<String> cmbJenisFilter;
    
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
            new Object[]{"ID", "Kecamatan", "Nama Desa", "Jenis", "Alamat Kantor", 
                        "Nama Kepala", "No HP"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel tombol CRUD
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

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Desa> desaList = desaDAO.getAllDesa();
            for (Desa desa : desaList) {
                tableModel.addRow(new Object[]{
                    desa.getIdDesa(),
                    desa.getNamaKecamatan() != null ? desa.getNamaKecamatan() : "-",
                    desa.getNamaDesa() != null ? desa.getNamaDesa() : "-",
                    desa.getJenis() != null ? desa.getJenis() : "-",
                    desa.getAlamatKantor() != null ? desa.getAlamatKantor() : "-",
                    desa.getNamaKepala() != null ? desa.getNamaKepala() : "-",
                    desa.getNoHp() != null ? desa.getNoHp() : "-"
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
                    
                    if (!namaDesa.contains(keyword) && !alamat.contains(keyword) && !namaKepala.contains(keyword)) {
                        continue;
                    }
                }
                
                tableModel.addRow(new Object[]{
                    desa.getIdDesa(),
                    namaKecamatanDesa != null ? namaKecamatanDesa : "-",
                    desa.getNamaDesa() != null ? desa.getNamaDesa() : "-",
                    jenisDesa != null ? jenisDesa : "-",
                    desa.getAlamatKantor() != null ? desa.getAlamatKantor() : "-",
                    desa.getNamaKepala() != null ? desa.getNamaKepala() : "-",
                    desa.getNoHp() != null ? desa.getNoHp() : "-"
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
            true
        );
        
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
        
        int idDesa = (int) table.getValueAt(row, 0);
        Desa desa = desaDAO.getDesaById(idDesa);
        
        if (desa == null) {
            JOptionPane.showMessageDialog(this, 
                "Data tidak ditemukan!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        DesaFormDialog dialog = new DesaFormDialog(
            (Frame) SwingUtilities.getWindowAncestor(this),
            "Edit Data Desa",
            true,
            desa
        );
        
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Desa updatedDesa = dialog.getDesa();
            
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
        
        int idDesa = (int) table.getValueAt(row, 0);
        String namaDesa = table.getValueAt(row, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus desa:\n" + namaDesa + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
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

        int idDesa = (int) table.getValueAt(row, 0);
        Desa desa = desaDAO.getDesaById(idDesa);
        
        if (desa != null) {
            String detail = String.format(
                "========== DETAIL DATA DESA ==========\n\n" +
                "ID Desa           : %d\n" +
                "Kecamatan         : %s\n" +
                "Nama Desa         : %s\n" +
                "Jenis             : %s\n" +
                "Alamat Kantor     : %s\n" +
                "Nama Kepala       : %s\n" +
                "Alamat Rumah      : %s\n" +
                "No HP             : %s\n\n" +
                "Dibuat pada       : %s\n" +
                "Terakhir diupdate : %s",
                desa.getIdDesa(),
                desa.getNamaKecamatan() != null ? desa.getNamaKecamatan() : "-",
                desa.getNamaDesa() != null ? desa.getNamaDesa() : "-",
                desa.getJenis() != null ? desa.getJenis() : "-",
                desa.getAlamatKantor() != null ? desa.getAlamatKantor() : "-",
                desa.getNamaKepala() != null ? desa.getNamaKepala() : "-",
                desa.getAlamatRumahKepala() != null ? desa.getAlamatRumahKepala() : "-",
                desa.getNoHp() != null ? desa.getNoHp() : "-",
                desa.getCreatedAt() != null ? desa.getCreatedAt() : "-",
                desa.getUpdatedAt() != null ? desa.getUpdatedAt() : "-"
            );
            
            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 350));
            
            JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Detail Data Desa - " + desa.getNamaDesa(), 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Data tidak ditemukan!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}