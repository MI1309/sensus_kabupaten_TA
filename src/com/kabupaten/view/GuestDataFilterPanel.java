package com.kabupaten.view;

import com.kabupaten.dao.DesaDAO;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.dao.RTRWDAO;
import com.kabupaten.dao.WargaDAO;
import com.kabupaten.model.Desa;
import com.kabupaten.model.Kecamatan;
import com.kabupaten.model.RTRW;
import com.kabupaten.model.Warga;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GuestDataFilterPanel extends JPanel {
    private WargaDAO wargaDAO = new WargaDAO();
    private KecamatanDAO kecamatanDAO = new KecamatanDAO();
    private DesaDAO desaDAO = new DesaDAO();
    private RTRWDAO rtrwDAO = new RTRWDAO();

    private JComboBox<KecamatanItem> cmbKecamatan;
    private JComboBox<DesaItem> cmbDesa;
    private JComboBox<RTRWItem> cmbRTRW;

    private JTable tableWarga;
    private DefaultTableModel tableModelWarga;
    private JLabel lblTotalWarga;
    private JTable tableRTRW;
    private DefaultTableModel tableModelRTRW;
    private JLabel lblTotalRTRW;
    private JTextField txtSearch;

    public GuestDataFilterPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header Panel with Filter Options
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        cmbKecamatan = new JComboBox<>();
        cmbDesa = new JComboBox<>();
        cmbRTRW = new JComboBox<>();

        cmbKecamatan.setPreferredSize(new Dimension(200, 30));
        cmbDesa.setPreferredSize(new Dimension(200, 30));
        cmbRTRW.setPreferredSize(new Dimension(200, 30));

        filterPanel.add(new JLabel("Kecamatan:"));
        filterPanel.add(cmbKecamatan);
        filterPanel.add(new JLabel("Desa/Kelurahan:"));
        filterPanel.add(cmbDesa);
        filterPanel.add(new JLabel("RT/RW:"));
        filterPanel.add(cmbRTRW);

        filterPanel.add(new JLabel("Cari nama warga : "));
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 30));
        filterPanel.add(txtSearch);

        JButton btnReset = new JButton("Reset Filter");
        btnReset.setBackground(new Color(108, 117, 125));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        btnReset.addActionListener(e -> {
            cmbKecamatan.setSelectedIndex(0);
            txtSearch.setText("");
        });
        filterPanel.add(btnReset);

        add(filterPanel, BorderLayout.NORTH);

        // --- Main TabbedPane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // --- Tab 1: Warga Table ---
        tableModelWarga = new DefaultTableModel(
                new Object[] { "No", "NIK", "Nama Lengkap", "L/P", "Kecamatan", "Desa", "Alamat" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableWarga = new JTable(tableModelWarga);
        tableWarga.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableWarga.setRowHeight(35);
        tableWarga.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableWarga.getTableHeader().setBackground(new Color(39, 82, 139));
        tableWarga.getTableHeader().setForeground(Color.WHITE);
        tableWarga.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableWarga.setSelectionBackground(new Color(79, 172, 254));
        tableWarga.setGridColor(new Color(230, 230, 230));

        JScrollPane scrollWarga = new JScrollPane(tableWarga);
        scrollWarga.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollWarga.getViewport().setBackground(Color.WHITE);

        JPanel panelWarga = new JPanel(new BorderLayout());
        panelWarga.add(scrollWarga, BorderLayout.CENTER);

        lblTotalWarga = new JLabel("Total Data Warga: 0");
        lblTotalWarga.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JPanel footerWarga = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerWarga.setBackground(Color.WHITE);
        footerWarga.add(lblTotalWarga);
        panelWarga.add(footerWarga, BorderLayout.SOUTH);

        tabbedPane.addTab("👥 Data Warga", panelWarga);
        tabbedPane.setFont(new Font("Arial Emoji", Font.PLAIN, 14));

        // --- Tab 2: RT/RW Table ---
        tableModelRTRW = new DefaultTableModel(
                new Object[] { "No", "Kecamatan", "Desa", "RT", "RW", "Ketua RT/RW", "Kontak", "Alamat" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableRTRW = new JTable(tableModelRTRW);
        tableRTRW.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRTRW.setRowHeight(35);
        tableRTRW.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableRTRW.getTableHeader().setBackground(new Color(39, 82, 139));
        tableRTRW.getTableHeader().setForeground(Color.WHITE);
        tableRTRW.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableRTRW.setSelectionBackground(new Color(79, 172, 254));
        tableRTRW.setGridColor(new Color(230, 230, 230));

        JScrollPane scrollRTRW = new JScrollPane(tableRTRW);
        scrollRTRW.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollRTRW.getViewport().setBackground(Color.WHITE);

        JPanel panelRTRW = new JPanel(new BorderLayout());
        panelRTRW.add(scrollRTRW, BorderLayout.CENTER);

        lblTotalRTRW = new JLabel("Total Data RT/RW: 0");
        lblTotalRTRW.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JPanel footerRTRW = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerRTRW.setBackground(Color.WHITE);
        footerRTRW.add(lblTotalRTRW);
        panelRTRW.add(footerRTRW, BorderLayout.SOUTH);

        tabbedPane.addTab("🏘️ Data RT/RW", panelRTRW);

        add(tabbedPane, BorderLayout.CENTER);

        // Setup Event Listeners
        setupListeners();

        // Initial Load
        loadKecamatanCombo();
        refreshTable();
    }

    private boolean isUpdating = false;

    private void setupListeners() {
        cmbKecamatan.addActionListener(e -> {
            if (isUpdating)
                return;
            if (cmbKecamatan.getSelectedItem() != null) {
                isUpdating = true;
                loadDesaCombo();
                loadRTRWCombo();
                refreshTable();
                isUpdating = false;
            }
        });

        cmbDesa.addActionListener(e -> {
            if (isUpdating)
                return;
            if (cmbDesa.getSelectedItem() != null) {
                isUpdating = true;
                loadRTRWCombo();
                refreshTable();
                isUpdating = false;
            }
        });

        cmbRTRW.addActionListener(e -> {
            if (isUpdating)
                return;
            if (cmbRTRW.getSelectedItem() != null) {
                refreshTable();
            }
        });

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
        });
    }

    private void loadKecamatanCombo() {
        cmbKecamatan.removeAllItems();
        cmbKecamatan.addItem(new KecamatanItem(0, "Semua Kecamatan"));

        List<Kecamatan> list = kecamatanDAO.getAllKecamatan();
        for (Kecamatan k : list) {
            cmbKecamatan.addItem(new KecamatanItem(k.getIdKecamatan(), k.getNamaKecamatan()));
        }
    }

    private void loadDesaCombo() {
        cmbDesa.removeAllItems();
        cmbDesa.addItem(new DesaItem(0, "Semua Desa", ""));

        KecamatanItem selectedKec = (KecamatanItem) cmbKecamatan.getSelectedItem();
        if (selectedKec != null && selectedKec.id != 0) {
            List<Desa> list = desaDAO.getDesaByKecamatan(selectedKec.id);
            for (Desa d : list) {
                cmbDesa.addItem(new DesaItem(d.getIdDesa(), d.getNamaDesa(), d.getNamaKecamatan()));
            }
        }
    }

    private void loadRTRWCombo() {
        cmbRTRW.removeAllItems();
        cmbRTRW.addItem(new RTRWItem(0, "Semua RT/RW", "", ""));

        DesaItem selectedDesa = (DesaItem) cmbDesa.getSelectedItem();
        if (selectedDesa != null && selectedDesa.id != 0) {
            List<RTRW> list = rtrwDAO.getRTRWByDesaName(selectedDesa.nama);
            for (RTRW r : list) {
                String label = "RT " + r.getRt() + " / RW " + r.getRw();
                cmbRTRW.addItem(new RTRWItem(r.getIdRtrw(), label, r.getRt(), r.getRw()));
            }
        }
    }

    private void refreshTable() {
        if (cmbKecamatan.getSelectedItem() == null)
            return;

        KecamatanItem selKec = (KecamatanItem) cmbKecamatan.getSelectedItem();
        DesaItem selDesa = (DesaItem) cmbDesa.getSelectedItem();
        RTRWItem selRTRW = (RTRWItem) cmbRTRW.getSelectedItem();
        String searchText = txtSearch.getText().toLowerCase().trim();

        tableModelWarga.setRowCount(0);
        tableModelRTRW.setRowCount(0);

        // --- 1. Filter Warga Data ---
        List<Warga> listWarga = wargaDAO.getAllWarga();
        int countWarga = 0;
        for (Warga w : listWarga) {
            boolean matchKecamatan = true;
            boolean matchDesa = true;
            boolean matchRTRW = true;

            if (selKec != null && selKec.id != 0) {
                matchKecamatan = (w.getNamaKecamatan() != null && w.getNamaKecamatan().equalsIgnoreCase(selKec.nama));
            }
            if (selDesa != null && selDesa.id != 0) {
                matchDesa = (w.getIdDesa() == selDesa.id);
            }
            if (selRTRW != null && selRTRW.id != 0) {
                String alamat = w.getAlamat() != null ? w.getAlamat().toLowerCase() : "";
                String rtStr = selRTRW.rt.toLowerCase();
                String rwStr = selRTRW.rw.toLowerCase();

                if (!rtStr.isEmpty() && !alamat.contains(rtStr))
                    matchRTRW = false;
                if (!rwStr.isEmpty() && !alamat.contains(rwStr))
                    matchRTRW = false;
            }

            // Live Search Filter for Warga
            boolean matchSearch = true;
            if (!searchText.isEmpty()) {
                String nik = w.getNik() != null ? w.getNik().toLowerCase() : "";
                String nama = w.getNamaLengkap() != null ? w.getNamaLengkap().toLowerCase() : "";
                if (!nik.contains(searchText) && !nama.contains(searchText)) {
                    matchSearch = false;
                }
            }

            if (matchKecamatan && matchDesa && matchRTRW && matchSearch) {
                tableModelWarga.addRow(new Object[] {
                        (countWarga + 1), w.getNik(), w.getNamaLengkap(), w.getJenisKelamin(), w.getNamaKecamatan(),
                        w.getNamaDesa(), w.getAlamat()
                });
                countWarga++;
            }
        }
        lblTotalWarga.setText("Total Data Warga: " + countWarga);

        // --- 2. Filter RT/RW Data ---
        List<Desa> allDesaList = desaDAO.getAllDesa();
        java.util.Map<String, String> desaToKecMap = new java.util.HashMap<>();
        for (Desa d : allDesaList) {
            desaToKecMap.put(d.getNamaDesa(), d.getNamaKecamatan());
        }

        List<RTRW> listRTRW = rtrwDAO.getAllRTRW();
        int countRTRW = 0;
        for (RTRW r : listRTRW) {
            boolean matchKecamatan = true;
            boolean matchDesa = true;
            boolean matchSpecificRTRW = true;

            if (selDesa != null && selDesa.id != 0) {
                matchDesa = (r.getNamaDesa() != null && r.getNamaDesa().equalsIgnoreCase(selDesa.nama));
            } else if (selKec != null && selKec.id != 0) {
                String rtrwKec = desaToKecMap.get(r.getNamaDesa());
                if (rtrwKec == null || !rtrwKec.equalsIgnoreCase(selKec.nama)) {
                    matchKecamatan = false;
                }
            }

            if (selRTRW != null && selRTRW.id != 0) {
                matchSpecificRTRW = (r.getIdRtrw() == selRTRW.id);
            }

            // Live Search Filter for RT/RW
            boolean matchSearch = true;
            if (!searchText.isEmpty()) {
                String ketua = r.getNamaKetua() != null ? r.getNamaKetua().toLowerCase() : "";
                if (!ketua.contains(searchText)) {
                    matchSearch = false;
                }
            }

            if (matchKecamatan && matchDesa && matchSpecificRTRW && matchSearch) {
                String namaKec = desaToKecMap.getOrDefault(r.getNamaDesa(), "-");

                tableModelRTRW.addRow(new Object[] {
                        (countRTRW + 1), namaKec, r.getNamaDesa(),
                        r.getRt(), r.getRw(),
                        r.getNamaKetua(), r.getKontak(), r.getAlamat()
                });
                countRTRW++;
            }
        }
        lblTotalRTRW.setText("Total Data RT/RW: " + countRTRW);
    }

    // --- Helper Classes for ComboBox ---

    private class KecamatanItem {
        int id;
        String nama;

        KecamatanItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    private class DesaItem {
        int id;
        String nama;
        String namaKec;

        DesaItem(int id, String nama, String namaKec) {
            this.id = id;
            this.nama = nama;
            this.namaKec = namaKec;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    private class RTRWItem {
        int id;
        String label;
        String rt;
        String rw;

        RTRWItem(int id, String label, String rt, String rw) {
            this.id = id;
            this.label = label;
            this.rt = rt;
            this.rw = rw;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
