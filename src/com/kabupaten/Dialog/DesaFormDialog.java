package com.kabupaten.Dialog;

import com.kabupaten.model.Desa;
import com.kabupaten.dao.KecamatanDAO;
import com.kabupaten.model.Kecamatan;
import com.kabupaten.util.ValidationUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;

public class DesaFormDialog extends JDialog {
    private JComboBox<String> cmbKecamatan;
    private JTextField txtNamaDesa;
    private JComboBox<String> cmbJenis;
    private JTextField txtAlamatKantor;
    private JTextField txtNamaKepala;
    private JTextField txtNoHp;
    private JLabel lblFotoPreview;
    private JButton btnPilihFoto;
    private String selectedFotoPath = ""; // Path relatif gambar

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
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Kecamatan:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbKecamatan = new JComboBox<>();
        loadKecamatanList();
        formPanel.add(cmbKecamatan, gbc);

        // Nama Desa
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nama Desa / Kelurahan:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaDesa = new JTextField(20);
        formPanel.add(txtNamaDesa, gbc);

        // Jenis (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Jenis:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbJenis = new JComboBox<>(new String[] { "DESA", "KELURAHAN" });
        formPanel.add(cmbJenis, gbc);

        // Alamat Kantor
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat Kantor:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAlamatKantor = new JTextField(20);
        formPanel.add(txtAlamatKantor, gbc);

        // Nama Kepala
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nama Kepala :"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaKepala = new JTextField(20);
        formPanel.add(txtNamaKepala, gbc);


        // No HP
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("No HP:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNoHp = new JTextField(20);
        formPanel.add(txtNoHp, gbc);

        // Pilih Foto
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Foto Desa:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel fotoPanel = new JPanel(new BorderLayout(5, 5));
        btnPilihFoto = new JButton("📁 Pilih Gambar");
        btnPilihFoto.setBackground(new Color(30, 60, 114));
        btnPilihFoto.setForeground(Color.WHITE);
        btnPilihFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblFotoPreview = new JLabel("Belum ada gambar", SwingConstants.CENTER);
        lblFotoPreview.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFotoPreview.setForeground(Color.GRAY);
        lblFotoPreview.setPreferredSize(new Dimension(150, 80));
        lblFotoPreview.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        fotoPanel.add(btnPilihFoto, BorderLayout.NORTH);
        fotoPanel.add(lblFotoPreview, BorderLayout.CENTER);
        formPanel.add(fotoPanel, gbc);

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

        // Real-time input filtering
        ValidationUtils.applyNameFilter(txtNamaDesa, "Nama Desa");
        ValidationUtils.applyNameFilter(txtNamaKepala, "Nama Kepala Desa");
        ValidationUtils.applyNumericFilter(txtNoHp, 15, "No HP");

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

        // Tambahkan ActionListener untuk Enter key pada text field (Focus Traversal)
        txtNamaDesa.addActionListener(e -> txtAlamatKantor.requestFocus());
        txtAlamatKantor.addActionListener(e -> txtNamaKepala.requestFocus());
        txtNamaKepala.addActionListener(e -> txtNoHp.requestFocus());
        txtNoHp.addActionListener(simpanAction); // Terakhir baru simpan

        // Event pilih foto
        btnPilihFoto.addActionListener(e -> pilihFoto());

        pack();
        setLocationRelativeTo(getParent());
    }

    private void pilihFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Foto Desa");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "File Gambar (JPG, PNG, JPEG)", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Buat folder images/desa jika belum ada
                String projectPath = System.getProperty("user.dir");
                File targetDir = new File(projectPath + "/images/desa");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                // Buat nama file unik dengan timestamp
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = Paths.get(targetDir.getAbsolutePath(), fileName);

                // Copy file ke folder images
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Simpan path relatif
                selectedFotoPath = "images/desa/" + fileName;

                // Tampilkan preview
                ImageIcon icon = new ImageIcon(selectedFile.getPath());
                Image scaled = icon.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
                lblFotoPreview.setIcon(new ImageIcon(scaled));
                lblFotoPreview.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan gambar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
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
            txtNoHp.setText(desa.getNoHp() != null ? desa.getNoHp() : "");

            // Load preview foto jika ada
            String fotoUrl = desa.getFotoUrl();
            if (fotoUrl != null && !fotoUrl.isEmpty()) {
                selectedFotoPath = fotoUrl;
                try {
                    File imgFile = new File(fotoUrl);
                    if (!imgFile.exists()) {
                        String projectPath = System.getProperty("user.dir");
                        imgFile = new File(projectPath + "/" + fotoUrl);
                    }

                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imgFile.getPath());
                        Image scaled = icon.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
                        lblFotoPreview.setIcon(new ImageIcon(scaled));
                        lblFotoPreview.setText("");
                    }
                } catch (Exception e) {
                    lblFotoPreview.setText("Gambar tidak ditemukan");
                }
            }
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
        String namaDesa = txtNamaDesa.getText().trim();
        if (namaDesa.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama Desa tidak boleh kosong!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
            txtNamaDesa.requestFocus();
            return false;
        }

        if (!namaDesa.matches("[a-zA-Z0-9\\s.,\\-()]*")) {
            JOptionPane.showMessageDialog(this,
                    "Nama Desa mengandung karakter yang tidak valid!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
            txtNamaDesa.requestFocus();
            return false;
        }

        // Validasi Nama Kepala
        String namaKepala = txtNamaKepala.getText().trim();
        if (!namaKepala.isEmpty() && !namaKepala.matches("[a-zA-Z0-9\\s.,\\-()]*")) {
            JOptionPane.showMessageDialog(this,
                    "Nama Kepala mengandung karakter yang tidak valid!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
            txtNamaKepala.requestFocus();
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
        if (!noHp.isEmpty() && !noHp.matches("^[0-9]{10,15}$")) {
            JOptionPane.showMessageDialog(this,
                    "Format No HP tidak valid!\n" +
                            "No HP harus berisi 10-15 digit angka.\n" +
                            "Contoh: 081234567890",
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
        desa.setNoHp(txtNoHp.getText().trim());
        desa.setFotoUrl(selectedFotoPath);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Desa getDesa() {
        return desa;
    }
}