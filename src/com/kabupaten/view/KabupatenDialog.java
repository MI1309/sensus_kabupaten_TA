package com.kabupaten.view;

import com.kabupaten.dao.KabupatenDAO;
import com.kabupaten.model.kabupaten;
import com.kabupaten.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Dialog untuk tambah/edit data kabupaten
 */
public class KabupatenDialog extends JDialog {
    private KabupatenDAO kabupatenDAO;
    private kabupaten currentKabupaten;
    private boolean confirmed = false;
    
    // Form components
    private JComboBox<ProvinsiItem> cmbProvinsi;
    private JTextField txtKode, txtNama, txtIbukota;
    private JTextField txtLuas, txtPenduduk, txtKecamatan, txtDesa;
    private JTextArea txtBatasUtara, txtBatasSelatan, txtBatasTimur, txtBatasBarat;
    private JButton btnSave, btnCancel;
    
    public KabupatenDialog(Frame parent, String title, kabupaten kabupaten) {
        super(parent, title, true);
        this.kabupatenDAO = new KabupatenDAO();
        this.currentKabupaten = kabupaten;
        
        initComponents();
        setupFrame();
        loadProvinsiData();
        
        if (kabupaten != null) {
            fillForm(kabupaten);
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Main panel dengan padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Provinsi
        addFormField(panel, gbc, row++, "Provinsi:", createProvinsiComboBox());
        
        // Kode Kabupaten
        txtKode = createTextField(15);
        addFormField(panel, gbc, row++, "Kode Kabupaten:", txtKode);
        
        // Nama Kabupaten
        txtNama = createTextField(30);
        addFormField(panel, gbc, row++, "Nama Kabupaten:", txtNama);
        
        // Ibukota
        txtIbukota = createTextField(25);
        addFormField(panel, gbc, row++, "Ibukota:", txtIbukota);
        
        // Luas Wilayah
        txtLuas = createTextField(15);
        addFormField(panel, gbc, row++, "Luas Wilayah (km²):", txtLuas);
        
        // Jumlah Penduduk
        txtPenduduk = createTextField(15);
        addFormField(panel, gbc, row++, "Jumlah Penduduk:", txtPenduduk);
        
        // Jumlah Kecamatan
        txtKecamatan = createTextField(10);
        addFormField(panel, gbc, row++, "Jumlah Kecamatan:", txtKecamatan);
        
        // Jumlah Desa
        txtDesa = createTextField(10);
        addFormField(panel, gbc, row++, "Jumlah Desa:", txtDesa);
        
        // Batas Wilayah - dalam panel terpisah
        JPanel batasPanel = createBatasWilayahPanel();
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        panel.add(batasPanel, gbc);
        
        return panel;
    }
    
    private JComboBox<ProvinsiItem> createProvinsiComboBox() {
        cmbProvinsi = new JComboBox<>();
        cmbProvinsi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbProvinsi.setPreferredSize(new Dimension(250, 30));
        return cmbProvinsi;
    }
    
    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    private void addFormField(JPanel parent, GridBagConstraints gbc, int row, String label, JComponent component) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        parent.add(lbl, gbc);
        
        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        parent.add(component, gbc);
    }
    
    private JPanel createBatasWilayahPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Batas Wilayah",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 12)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        
        // Batas Utara
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createBatasField("Utara:", txtBatasUtara = createTextArea(3, 20)), gbc);
        
        // Batas Selatan
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(createBatasField("Selatan:", txtBatasSelatan = createTextArea(3, 20)), gbc);
        
        // Batas Timur
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(createBatasField("Timur:", txtBatasTimur = createTextArea(3, 20)), gbc);
        
        // Batas Barat
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(createBatasField("Barat:", txtBatasBarat = createTextArea(3, 20)), gbc);
        
        return panel;
    }
    
    private JPanel createBatasField(String label, JTextArea textArea) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(180, 60));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JTextArea createTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return area;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        btnSave = createStyledButton("Simpan", new Color(46, 125, 50));
        btnCancel = createStyledButton("Batal", new Color(158, 158, 158));
        
        panel.add(btnCancel);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(btnSave);
        
        setupButtonActions();
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void setupButtonActions() {
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void loadProvinsiData() {
        String sql = "SELECT id_provinsi, nama_provinsi FROM provinsi ORDER BY nama_provinsi";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            cmbProvinsi.removeAllItems();
            cmbProvinsi.addItem(new ProvinsiItem(0, "-- Pilih Provinsi --"));
            
            while (rs.next()) {
                ProvinsiItem item = new ProvinsiItem(
                    rs.getInt("id_provinsi"),
                    rs.getString("nama_provinsi")
                );
                cmbProvinsi.addItem(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading provinsi data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error memuat data provinsi: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void fillForm(kabupaten kabupaten) {
        // Set provinsi
        for (int i = 0; i < cmbProvinsi.getItemCount(); i++) {
            ProvinsiItem item = cmbProvinsi.getItemAt(i);
            if (item.getId() == kabupaten.getIdProvinsi()) {
                cmbProvinsi.setSelectedIndex(i);
                break;
            }
        }
        
        txtKode.setText(kabupaten.getKodeKabupaten());
        txtNama.setText(kabupaten.getNamaKabupaten());
        txtIbukota.setText(kabupaten.getIbukota());
        txtLuas.setText(String.valueOf(kabupaten.getLuasWilayah()));
        txtPenduduk.setText(String.valueOf(kabupaten.getJumlahPenduduk()));
        txtKecamatan.setText(String.valueOf(kabupaten.getJumlahKecamatan()));
        txtDesa.setText(String.valueOf(kabupaten.getJumlahDesa()));
        
        txtBatasUtara.setText(kabupaten.getBatasUtara() != null ? kabupaten.getBatasUtara() : "");
        txtBatasSelatan.setText(kabupaten.getBatasSelatan() != null ? kabupaten.getBatasSelatan() : "");
        txtBatasTimur.setText(kabupaten.getBatasTimur() != null ? kabupaten.getBatasTimur() : "");
        txtBatasBarat.setText(kabupaten.getBatasBarat() != null ? kabupaten.getBatasBarat() : "");
    }
    
    private void saveData() {
        if (!validateForm()) {
            return;
        }
        
        try {
            kabupaten kabupaten = new kabupaten();
            
            if (currentKabupaten != null) {
                kabupaten.setIdKabupaten(currentKabupaten.getIdKabupaten());
            }
            
            ProvinsiItem selectedProvinsi = (ProvinsiItem) cmbProvinsi.getSelectedItem();
            kabupaten.setIdProvinsi(selectedProvinsi.getId());
            kabupaten.setKodeKabupaten(txtKode.getText().trim());
            kabupaten.setNamaKabupaten(txtNama.getText().trim());
            kabupaten.setIbukota(txtIbukota.getText().trim());
            kabupaten.setLuasWilayah(Double.parseDouble(txtLuas.getText().trim()));
            kabupaten.setJumlahPenduduk(Integer.parseInt(txtPenduduk.getText().trim()));
            kabupaten.setJumlahKecamatan(Integer.parseInt(txtKecamatan.getText().trim()));
            kabupaten.setJumlahDesa(Integer.parseInt(txtDesa.getText().trim()));
            kabupaten.setBatasUtara(txtBatasUtara.getText().trim());
            kabupaten.setBatasSelatan(txtBatasSelatan.getText().trim());
            kabupaten.setBatasTimur(txtBatasTimur.getText().trim());
            kabupaten.setBatasBarat(txtBatasBarat.getText().trim());
            
            boolean success;
            if (currentKabupaten == null) {
                success = kabupatenDAO.addKabupaten(kabupaten);
            } else {
                success = kabupatenDAO.updateKabupaten(kabupaten);
            }
            
            if (success) {
                String message = currentKabupaten == null ? "Data kabupaten berhasil ditambahkan!" : "Data kabupaten berhasil diupdate!";
                JOptionPane.showMessageDialog(this, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                confirmed = true;
                dispose();
            } else {
                String message = currentKabupaten == null ? "Gagal menambahkan data kabupaten!" : "Gagal mengupdate data kabupaten!";
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Format angka tidak valid! Periksa input luas wilayah, jumlah penduduk, kecamatan, dan desa.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateForm() {
        if (cmbProvinsi.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih provinsi!", "Validasi", JOptionPane.WARNING_MESSAGE);
            cmbProvinsi.requestFocus();
            return false;
        }
        
        if (txtKode.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kode kabupaten tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtKode.requestFocus();
            return false;
        }
        
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kabupaten tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus();
            return false;
        }
        
        if (txtIbukota.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ibukota tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtIbukota.requestFocus();
            return false;
        }
        
        try {
            if (!txtLuas.getText().trim().isEmpty()) {
                Double.parseDouble(txtLuas.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Luas wilayah harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtLuas.requestFocus();
            return false;
        }
        
        try {
            if (!txtPenduduk.getText().trim().isEmpty()) {
                Integer.parseInt(txtPenduduk.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah penduduk harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtPenduduk.requestFocus();
            return false;
        }
        
        try {
            if (!txtKecamatan.getText().trim().isEmpty()) {
                Integer.parseInt(txtKecamatan.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah kecamatan harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtKecamatan.requestFocus();
            return false;
        }
        
        try {
            if (!txtDesa.getText().trim().isEmpty()) {
                Integer.parseInt(txtDesa.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah desa harus berupa angka!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtDesa.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    private void setupFrame() {
        setSize(600, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    /**
     * Inner class untuk item provinsi di ComboBox
     */
    private static class ProvinsiItem {
        private int id;
        private String nama;
        
        public ProvinsiItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }
        
        public int getId() { return id; }
        public String getNama() { return nama; }
        
        @Override
        public String toString() { return nama; }
    }
}