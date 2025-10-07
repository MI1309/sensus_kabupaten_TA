package com.kabupaten.view;

import com.kabupaten.model.RTRW;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RTRWFormDialog extends JDialog {
    private JTextField txtKecamatan;
    private JTextField txtRW;
    private JTextField txtRT;
    private JTextField txtNamaKetua;
    private JTextField txtKontak;
    private JTextArea txtAlamat;
    private JComboBox<String> cmbStatus;
    
    private boolean confirmed = false;
    private RTRW rtrw;

    public RTRWFormDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        this.rtrw = new RTRW();
        initComponents();
    }
    
    public RTRWFormDialog(Frame parent, String title, boolean modal, RTRW rtrw) {
        super(parent, title, modal);
        this.rtrw = rtrw;
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

        // Kecamatan/Desa
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Desa:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtKecamatan = new JTextField(20);
        formPanel.add(txtKecamatan, gbc);

        // RW
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("RW:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtRW = new JTextField(20);
        formPanel.add(txtRW, gbc);

        // RT
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("RT:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtRT = new JTextField(20);
        formPanel.add(txtRT, gbc);

        // Nama Ketua
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nama Ketua:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaKetua = new JTextField(20);
        formPanel.add(txtNamaKetua, gbc);

        // Kontak
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Kontak:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtKontak = new JTextField(20);
        formPanel.add(txtKontak, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbStatus = new JComboBox<>(new String[]{"Aktif", "Non-Aktif"});
        formPanel.add(cmbStatus, gbc);

        // Alamat
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        txtAlamat = new JTextArea(4, 20);
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        formPanel.add(scrollAlamat, gbc);

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

        // Event handlers
        btnSimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saveData();
                    confirmed = true;
                    dispose();
                }
            }
        });

        btnBatal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(getParent());
    }

    private void loadData() {
        if (rtrw != null) {
            txtKecamatan.setText(rtrw.getKecamatan() != null ? rtrw.getKecamatan() : 
                                (rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : ""));
            txtRW.setText(rtrw.getRw() != null ? rtrw.getRw() : "");
            txtRT.setText(rtrw.getRt() != null ? rtrw.getRt() : "");
            txtNamaKetua.setText(rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "");
            txtKontak.setText(rtrw.getKontak() != null ? rtrw.getKontak() : "");
            txtAlamat.setText(rtrw.getAlamat() != null ? rtrw.getAlamat() : "");
            cmbStatus.setSelectedItem(rtrw.getStatus() != null ? rtrw.getStatus() : "Aktif");
        }
    }

    private boolean validateInput() {
        if (txtKecamatan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Desa tidak boleh kosong!");
            txtKecamatan.requestFocus();
            return false;
        }

        if (txtRW.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "RW tidak boleh kosong!");
            txtRW.requestFocus();
            return false;
        }

        if (txtRT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "RT tidak boleh kosong!");
            txtRT.requestFocus();
            return false;
        }

        try {
            Integer.parseInt(txtRW.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "RW harus berupa angka!");
            txtRW.requestFocus();
            return false;
        }

        try {
            Integer.parseInt(txtRT.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "RT harus berupa angka!");
            txtRT.requestFocus();
            return false;
        }

        return true;
    }

    private void saveData() {
        rtrw.setNamaDesa(txtKecamatan.getText().trim());
        rtrw.setRw(txtRW.getText().trim());
        rtrw.setRt(txtRT.getText().trim());
        rtrw.setNamaKetua(txtNamaKetua.getText().trim());
        rtrw.setKontak(txtKontak.getText().trim());
        rtrw.setAlamat(txtAlamat.getText().trim());
        rtrw.setStatus(cmbStatus.getSelectedItem().toString());
        
        // Set ID desa default jika belum ada
        if (rtrw.getIdDesa() == 0) {
            rtrw.setIdDesa(1); // Default ID desa, bisa disesuaikan
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public RTRW getRTRW() {
        return rtrw;
    }
}