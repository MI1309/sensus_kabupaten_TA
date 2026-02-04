package com.kabupaten.view;

import com.kabupaten.model.RTRW;
import com.kabupaten.model.Desa;
import com.kabupaten.dao.DesaDAO;
import com.kabupaten.util.ValidationUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RTRWFormDialog extends JDialog {
    private JComboBox<String> cmbDesa;
    private JTextField txtRW;
    private JTextField txtRT;
    private JTextField txtNamaKetua;
    private JTextField txtKontak;
    private JTextArea txtAlamat;
    private JComboBox<String> cmbStatus;
    
    private DesaDAO desaDAO = new DesaDAO();
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

        // Desa (ComboBox)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Desa:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbDesa = new JComboBox<>();
        loadDesaList();
        formPanel.add(cmbDesa, gbc);

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

        formPanel.add(scrollAlamat, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Real-time input filtering
        ValidationUtils.applyNumericFilter(txtRT, 3);
        ValidationUtils.applyNumericFilter(txtRW, 3);
        ValidationUtils.applyStringOnlyFilter(txtNamaKetua);
        ValidationUtils.applyNumericFilter(txtKontak, 15);

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

        // Tambahkan ActionListener untuk Enter key pada text field (Focus Traversal)
        cmbDesa.addActionListener(e -> txtRW.requestFocus());
        txtRW.addActionListener(e -> txtRT.requestFocus());
        txtRT.addActionListener(e -> txtNamaKetua.requestFocus());
        txtNamaKetua.addActionListener(e -> txtKontak.requestFocus());
        txtKontak.addActionListener(e -> {
            if (validateInput()) {
                saveData();
                confirmed = true;
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(getParent());
    }

    private void loadDesaList() {
        cmbDesa.removeAllItems();
        cmbDesa.addItem("-- Pilih Desa --");
        List<Desa> list = desaDAO.getAllDesa();
        for (Desa d : list) {
            cmbDesa.addItem(d.getNamaDesa());
        }
    }

    private void loadData() {
        if (rtrw != null) {
            if (rtrw.getNamaDesa() != null) {
                cmbDesa.setSelectedItem(rtrw.getNamaDesa());
            }
            txtRW.setText(rtrw.getRw() != null ? rtrw.getRw() : "");
            txtRT.setText(rtrw.getRt() != null ? rtrw.getRt() : "");
            txtNamaKetua.setText(rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "");
            txtKontak.setText(rtrw.getKontak() != null ? rtrw.getKontak() : "");
            txtAlamat.setText(rtrw.getAlamat() != null ? rtrw.getAlamat() : "");
            cmbStatus.setSelectedItem(rtrw.getStatus() != null ? rtrw.getStatus() : "Aktif");
        }
    }

    private boolean validateInput() {
        if (cmbDesa.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih Desa!");
            cmbDesa.requestFocus();
            return false;
        }

        String rwStr = txtRW.getText().trim();
        String rtStr = txtRT.getText().trim();
        String kontak = txtKontak.getText().trim();

        if (rwStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "RW tidak boleh kosong!");
            txtRW.requestFocus();
            return false;
        }

        if (rtStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "RT tidak boleh kosong!");
            txtRT.requestFocus();
            return false;
        }

        if (!rwStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "RW harus berupa angka!");
            txtRW.requestFocus();
            return false;
        }

        if (!rtStr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "RT harus berupa angka!");
            txtRT.requestFocus();
            return false;
        }

        String namaKetua = txtNamaKetua.getText().trim();
        if (!namaKetua.isEmpty() && !namaKetua.matches("[a-zA-Z\\s]*")) {
            JOptionPane.showMessageDialog(this, "Nama Ketua hanya boleh berisi huruf dan spasi!");
            txtNamaKetua.requestFocus();
            return false;
        }

        if (!kontak.isEmpty() && !kontak.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Kontak harus berupa angka!");
            txtKontak.requestFocus();
            return false;
        }

        // Format RT dan RW menjadi 3 digit (misal 1 -> 001)
        int rw = Integer.parseInt(rwStr);
        int rt = Integer.parseInt(rtStr);
        txtRW.setText(String.format("%03d", rw));
        txtRT.setText(String.format("%03d", rt));

        return true;
    }

    private void saveData() {
        String namaDesa = (String) cmbDesa.getSelectedItem();
        rtrw.setNamaDesa(namaDesa);
        rtrw.setRw(txtRW.getText().trim());
        rtrw.setRt(txtRT.getText().trim());
        rtrw.setNamaKetua(txtNamaKetua.getText().trim());
        rtrw.setKontak(txtKontak.getText().trim());
        rtrw.setAlamat(txtAlamat.getText().trim());
        rtrw.setStatus(cmbStatus.getSelectedItem().toString());
        
        // Cari ID Desa berdasarkan nama
        Desa d = desaDAO.getDesaByName(namaDesa);
        if (d != null) {
            rtrw.setIdDesa(d.getIdDesa());
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public RTRW getRTRW() {
        return rtrw;
    }
}