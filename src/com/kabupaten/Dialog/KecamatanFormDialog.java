package com.kabupaten.view;

import com.kabupaten.model.Kecamatan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KecamatanFormDialog extends JDialog {
    private JTextField txtNamaKecamatan;
    private JTextField txtAlamatKantor;
    private JTextField txtNamaKepala;
    private JTextField txtAlamatRumahKepala;
    private JTextField txtNoHp;
    
    private boolean confirmed = false;
    private Kecamatan kecamatan;

    public KecamatanFormDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        this.kecamatan = new Kecamatan();
        initComponents();
    }
    
    public KecamatanFormDialog(Frame parent, String title, boolean modal, Kecamatan kecamatan) {
        super(parent, title, modal);
        this.kecamatan = kecamatan;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nama Kecamatan
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Kecamatan:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaKecamatan = new JTextField(20);
        formPanel.add(txtNamaKecamatan, gbc);

        // Alamat Kantor
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat Kantor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAlamatKantor = new JTextField(20);
        formPanel.add(txtAlamatKantor, gbc);

        // Nama Kepala
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nama Kepala:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaKepala = new JTextField(20);
        formPanel.add(txtNamaKepala, gbc);

        // Alamat Rumah Kepala
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat Rumah Kepala:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAlamatRumahKepala = new JTextField(20);
        formPanel.add(txtAlamatRumahKepala, gbc);

        // No HP
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("No HP:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNoHp = new JTextField(20);
        formPanel.add(txtNoHp, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        
        btnSimpan.setBackground(new Color(46, 125, 50));
        btnSimpan.setForeground(Color.WHITE);
        btnBatal.setBackground(new Color(158, 158, 158));
        btnBatal.setForeground(Color.WHITE);

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers untuk tombol
        ActionListener simpanAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saveData();
                    confirmed = true;
                    dispose();
                }
            }
        };
        
        btnSimpan.addActionListener(simpanAction);

        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
        
        // Tambahkan ActionListener untuk Enter key pada semua text field
        txtNamaKecamatan.addActionListener(simpanAction);
        txtAlamatKantor.addActionListener(simpanAction);
        txtNamaKepala.addActionListener(simpanAction);
        txtAlamatRumahKepala.addActionListener(simpanAction);
        txtNoHp.addActionListener(simpanAction);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void loadData() {
        if (kecamatan != null) {
            txtNamaKecamatan.setText(kecamatan.getNamaKecamatan() != null ? kecamatan.getNamaKecamatan() : "");
            txtAlamatKantor.setText(kecamatan.getAlamatKantor() != null ? kecamatan.getAlamatKantor() : "");
            txtNamaKepala.setText(kecamatan.getNamaKepala() != null ? kecamatan.getNamaKepala() : "");
            txtAlamatRumahKepala.setText(kecamatan.getAlamatRumahKepala() != null ? kecamatan.getAlamatRumahKepala() : "");
            txtNoHp.setText(kecamatan.getNoHp() != null ? kecamatan.getNoHp() : "");
        }
    }

    private boolean validateInput() {
        // Validasi Nama Kecamatan
        if (txtNamaKecamatan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nama Kecamatan tidak boleh kosong!", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtNamaKecamatan.requestFocus();
            return false;
        }

        // Validasi Alamat Kantor
        if (txtAlamatKantor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Alamat Kantor tidak boleh kosong!", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtAlamatKantor.requestFocus();
            return false;
        }

        // Validasi Nama Kepala
        if (txtNamaKepala.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nama Kepala tidak boleh kosong!", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtNamaKepala.requestFocus();
            return false;
        }

        // Validasi Alamat Rumah Kepala
        if (txtAlamatRumahKepala.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Alamat Rumah Kepala tidak boleh kosong!", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtAlamatRumahKepala.requestFocus();
            return false;
        }

        // Validasi No HP
        if (txtNoHp.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No HP tidak boleh kosong!", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtNoHp.requestFocus();
            return false;
        }

        // Validasi format No HP (hanya angka dan karakter + di awal)
        String noHp = txtNoHp.getText().trim();
        if (!noHp.matches("^[+]?[0-9]{10,15}$")) {
            JOptionPane.showMessageDialog(this, 
                "Format No HP tidak valid!\n" +
                "No HP harus berisi 10-15 digit angka.\n" +
                "Boleh diawali dengan tanda + untuk kode negara.\n" +
                "Contoh: 081234567890 atau +6281234567890", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtNoHp.requestFocus();
            return false;
        }

        return true;
    }

    private void saveData() {
        kecamatan.setNamaKecamatan(txtNamaKecamatan.getText().trim());
        kecamatan.setAlamatKantor(txtAlamatKantor.getText().trim());
        kecamatan.setNamaKepala(txtNamaKepala.getText().trim());
        kecamatan.setAlamatRumahKepala(txtAlamatRumahKepala.getText().trim());
        kecamatan.setNoHp(txtNoHp.getText().trim());
        
        // Set default values untuk field yang tidak digunakan
        kecamatan.setJumlahPenduduk(0);
        kecamatan.setJumlahDesa(0);
        kecamatan.setJumlahKelurahan(0);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Kecamatan getKecamatan() {
        return kecamatan;
    }
}