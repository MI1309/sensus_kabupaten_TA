package com.kabupaten.Dialog;

import com.kabupaten.model.Fasilitas;
import javax.swing.*;
import java.awt.*;

public class FasilitasFormDialog extends JDialog {
    private boolean confirmed = false;
    private Fasilitas fasilitas;

    private JTextField txtNama, txtDinas, txtAlamat;
    private JTextArea txtKeterangan;
    private JComboBox<String> cmbJenis;

    private static final String[] JENIS_LIST = {
        "Kesehatan", "Pendidikan", "Ibadah", "Olahraga",
        "Pemerintahan", "Perekonomian", "Transportasi",
        "Lingkungan", "Telekomunikasi", "Energi"
    };

    // Constructor: Tambah
    public FasilitasFormDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        initComponents(null);
    }

    // Constructor: Edit
    public FasilitasFormDialog(Frame parent, String title, boolean modal, Fasilitas existing) {
        super(parent, title, modal);
        initComponents(existing);
    }

    private void initComponents(Fasilitas existing) {
        setSize(500, 420);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        txtNama       = new JTextField(25);
        txtDinas      = new JTextField(25);
        txtAlamat     = new JTextField(25);
        txtKeterangan = new JTextArea(3, 25);
        txtKeterangan.setLineWrap(true);
        txtKeterangan.setWrapStyleWord(true);
        cmbJenis      = new JComboBox<>(JENIS_LIST);

        addRow(formPanel, gbc, 0, "Nama Fasilitas *", txtNama);
        addRow(formPanel, gbc, 1, "Jenis *",           cmbJenis);
        addRow(formPanel, gbc, 2, "Dinas Terkait",      txtDinas);
        addRow(formPanel, gbc, 3, "Alamat",             txtAlamat);

        // Keterangan (textarea)
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Keterangan:"), gbc);
        gbc.gridx = 1;
        JScrollPane keteranganScroll = new JScrollPane(txtKeterangan);
        keteranganScroll.setPreferredSize(new Dimension(280, 70));
        formPanel.add(keteranganScroll, gbc);

        // Isi data jika mode Edit
        if (existing != null) {
            this.fasilitas = existing;
            txtNama.setText(existing.getNamaFasilitas());
            txtDinas.setText(existing.getDinasTerkait());
            txtAlamat.setText(existing.getAlamat());
            txtKeterangan.setText(existing.getKeterangan());
            if (existing.getJenis() != null) cmbJenis.setSelectedItem(existing.getJenis());
        }

        // Tombol
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);

        JButton btnSimpan = new JButton("💾 Simpan");
        btnSimpan.setBackground(new Color(30, 60, 114));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpan.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnBatal = new JButton("✖ Batal");
        btnBatal.setBackground(new Color(200, 200, 200));
        btnBatal.setForeground(Color.DARK_GRAY);
        btnBatal.setFocusPainted(false);
        btnBatal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBatal.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSimpan.addActionListener(e -> onSimpan());
        btnBatal.addActionListener(e -> dispose());

        btnPanel.add(btnBatal);
        btnPanel.add(btnSimpan);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void onSimpan() {
        String nama = txtNama.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama Fasilitas tidak boleh kosong!",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus();
            return;
        }

        if (fasilitas == null) fasilitas = new Fasilitas();
        fasilitas.setNamaFasilitas(nama);
        fasilitas.setJenis((String) cmbJenis.getSelectedItem());
        fasilitas.setDinasTerkait(txtDinas.getText().trim());
        fasilitas.setAlamat(txtAlamat.getText().trim());
        fasilitas.setKeterangan(txtKeterangan.getText().trim());

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() { return confirmed; }
    public Fasilitas getFasilitas() { return fasilitas; }
}