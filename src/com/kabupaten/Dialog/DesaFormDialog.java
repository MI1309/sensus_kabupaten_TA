package com.kabupaten.view;

import com.kabupaten.model.Desa;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.model.Kecamatan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DesaFormDialog extends JDialog {
    private JComboBox<String> cmbKecamatan;
    private JTextField txtNamaDesa;
    private JComboBox<String> cmbJenis;
    private JTextField txtAlamatKantor;
    private JTextField txtNamaKepala;
    private JTextField txtAlamatRumahKepala;
    private JTextField txtNoHp;
    
    private KecamatanDAO kecamatanDAO;
    private boolean confirmed = false;
    private Desa desa;

    public DesaFormDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        this.desa = new Desa();
        this.kecamatanDAO = new KecamatanDAO();
        initComponents();
    }
    
    public DesaFormDialog(Frame parent, String title, boolean modal, Desa desa) {
        super(parent, title, modal);
        this.desa = desa;
        this.kecamatanDAO = new KecamatanDAO();
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

        // Kecamatan (ComboBox)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Kecamatan:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbKecamatan = new JComboBox<>();
        loadKecamatanList();
        formPanel.add(cmbKecamatan, gbc);

        // Nama Desa
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nama Desa:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaDesa = new JTextField(20);
        formPanel.add(txtNamaDesa, gbc);

        // Jenis (ComboBox)
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Jenis:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbJenis = new JComboBox<>(new String[]{"DESA", "KELURAHAN"});
        formPanel.add(cmbJenis, gbc);

        // Alamat Kantor
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat Kantor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAlamatKantor = new JTextField(20);
        formPanel.add(txtAlamatKantor, gbc);

        // Nama Kepala
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nama Kepala:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaKepala = new JTextField(20);
        formPanel.add(txtNamaKepala, gbc);

        // Alamat Rumah Kepala
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat Rumah Kepala:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAlamatRumahKepala = new JTextField(20);
        formPanel.add(txtAlamatRumahKepala, gbc);

        // No HP
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
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
        
        // Tambahkan ActionListener untuk Enter key pada text field
        txtNamaDesa.addActionListener(simpanAction);
        txtAlamatKantor.addActionListener(simpanAction);
        txtNamaKepala.addActionListener(simpanAction);
        txtAlamatRumahKepala.addActionListener(simpanAction);
        txtNoHp.addActionListener(simpanAction);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void loadKecamatanList() {
        cmbKecamatan.removeAllItems();
        cmbKecamatan.addItem("-- Pilih Kecamatan --");
        
        List<Kecamatan> kecamatanList = kecamatanDAO.getAllKecamatan();
        for (Kecamatan kecamatan : kecamatanList) {
            cmbKecamatan.addItem(kecamatan.getNamaKecamatan());
        }
    }

    private void loadData() {
        if (desa != null) {
            // Set kecamatan
            if (desa.getNamaKecamatan() != null && !desa.getNamaKecamatan().isEmpty()) {
                cmbKecamatan.setSelectedItem(desa.getNamaKecamatan());
            }
            
            txtNamaDesa.setText(desa.getNamaDesa() != null ? desa.getNamaDesa() : "");
            
            // Set jenis
            if (desa.getJenis() != null && !desa.getJenis().isEmpty()) {
                cmbJenis.setSelectedItem(desa.getJenis());
            }
            
            txtAlamatKantor.setText(desa.getAlamatKantor() != null ? desa.getAlamatKantor() : "");
            txtNamaKepala.setText(desa.getNamaKepala() != null ? desa.getNamaKepala() : "");
            txtAlamatRumahKepala.setText(desa.getAlamatRumahKepala() != null ? desa.getAlamatRumahKepala() : "");
            txtNoHp.setText(desa.getNoHp() != null ? desa.getNoHp() : "");
        }
    }

    private boolean validateInput() {
        // Validasi Kecamatan
        if (cmbKecamatan.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Kecamatan harus dipilih!", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            cmbKecamatan.requestFocus();
            return false;
        }

        // Validasi Nama Desa
        if (txtNamaDesa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nama Desa tidak boleh kosong!", 
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtNamaDesa.requestFocus();
            return false;
        }

        // Validasi Alamat Kantor (opsional, bisa diubah sesuai kebutuhan)
        if (txtAlamatKantor.getText().trim().isEmpty()) {
            int result = JOptionPane.showConfirmDialog(this,
                "Alamat Kantor kosong. Lanjutkan?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (result != JOptionPane.YES_OPTION) {
                txtAlamatKantor.requestFocus();
                return false;
            }
        }

        // Validasi No HP (jika diisi)
        String noHp = txtNoHp.getText().trim();
        if (!noHp.isEmpty() && !noHp.matches("^[+]?[0-9]{10,15}$")) {
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
        // Ambil id_kecamatan dari nama yang dipilih
        String namaKecamatan = (String) cmbKecamatan.getSelectedItem();
        Kecamatan kecamatan = kecamatanDAO.getKecamatanByName(namaKecamatan);
        
        if (kecamatan != null) {
            desa.setIdKecamatan(kecamatan.getIdKecamatan());
            desa.setNamaKecamatan(namaKecamatan);
        }
        
        desa.setNamaDesa(txtNamaDesa.getText().trim());
        desa.setJenis((String) cmbJenis.getSelectedItem());
        desa.setAlamatKantor(txtAlamatKantor.getText().trim());
        desa.setNamaKepala(txtNamaKepala.getText().trim());
        desa.setAlamatRumahKepala(txtAlamatRumahKepala.getText().trim());
        desa.setNoHp(txtNoHp.getText().trim());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Desa getDesa() {
        return desa;
    }
}