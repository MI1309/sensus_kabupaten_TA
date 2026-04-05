package com.kabupaten.Dialog;

import com.kabupaten.model.Kecamatan;
import com.kabupaten.util.ValidationUtils;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class KecamatanFormDialog extends JDialog {
    private JTextField txtNamaKecamatan;
    private JTextField txtAlamatKantor;
    private JTextField txtNamaKepala;
    private JTextField txtAlamatRumahKepala;
    private JTextField txtNoHp;
    private JLabel lblFotoPreview;
    private JButton btnPilihFoto;
    private String selectedFotoPath = ""; // Path relatif gambar

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
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nama Kecamatan:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaKecamatan = new JTextField(20);
        formPanel.add(txtNamaKecamatan, gbc);

        // Alamat Kantor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat Kantor:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAlamatKantor = new JTextField(20);
        formPanel.add(txtAlamatKantor, gbc);

        // Nama Kepala
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Nama Kepala:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNamaKepala = new JTextField(20);
        formPanel.add(txtNamaKepala, gbc);

        // Alamat Rumah Kepala
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Alamat Rumah Kepala:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAlamatRumahKepala = new JTextField(20);
        formPanel.add(txtAlamatRumahKepala, gbc);

        // No HP
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("No HP:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtNoHp = new JTextField(20);
        formPanel.add(txtNoHp, gbc);

        // Pilih Foto
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Foto Kecamatan:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel fotoPanel = new JPanel(new BorderLayout(5, 5));
        btnPilihFoto = new JButton("📁 Pilih Gambar");
        btnPilihFoto.setBackground(new Color(30, 60, 114));
        btnPilihFoto.setForeground(Color.WHITE);
        btnPilihFoto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblFotoPreview = new JLabel("Belum ada gambar dipilih", SwingConstants.CENTER);
        lblFotoPreview.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblFotoPreview.setForeground(Color.GRAY);
        lblFotoPreview.setPreferredSize(new Dimension(150, 80));
        lblFotoPreview.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        fotoPanel.add(btnPilihFoto, BorderLayout.NORTH);
        fotoPanel.add(lblFotoPreview, BorderLayout.CENTER);
        formPanel.add(fotoPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Real-time input filtering
        ValidationUtils.applyStringOnlyFilter(txtNamaKecamatan);
        ValidationUtils.applyStringOnlyFilter(txtNamaKepala);
        ValidationUtils.applyNumericFilter(txtNoHp, 15);

        // Event pilih foto
        btnPilihFoto.addActionListener(e -> pilihFoto());

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
        ActionListener simpanAction = e -> {
            if (validateInput()) {
                saveData();
                confirmed = true;
                dispose();
            }
        };

        btnSimpan.addActionListener(simpanAction);
        btnBatal.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        // Tambahkan ActionListener untuk Enter key
        txtNamaKecamatan.addActionListener(e -> txtAlamatKantor.requestFocus());
        txtAlamatKantor.addActionListener(e -> txtNamaKepala.requestFocus());
        txtNamaKepala.addActionListener(e -> txtAlamatRumahKepala.requestFocus());
        txtAlamatRumahKepala.addActionListener(e -> txtNoHp.requestFocus());
        txtNoHp.addActionListener(e -> btnPilihFoto.requestFocus());
        btnPilihFoto.addActionListener(e -> pilihFoto());

        pack();
        setLocationRelativeTo(getParent());
    }

    private void pilihFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Foto Kecamatan");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "File Gambar (JPG, PNG, JPEG)", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Buat folder images/kecamatan jika belum ada
                String projectPath = System.getProperty("user.dir");
                File targetDir = new File(projectPath + "/images/kecamatan");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                
                // Buat nama file unik dengan timestamp
                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                Path targetPath = Paths.get(targetDir.getAbsolutePath(), fileName);
                
                // Copy file ke folder images
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                
                // Simpan path relatif
                selectedFotoPath = "images/kecamatan/" + fileName;
                
                // Tampilkan preview
                ImageIcon icon = new ImageIcon(selectedFile.getPath());
                Image scaled = icon.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
                lblFotoPreview.setIcon(new ImageIcon(scaled));
                lblFotoPreview.setText("");
                lblFotoPreview.setHorizontalAlignment(SwingConstants.CENTER);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan gambar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void loadData() {
        if (kecamatan != null) {
            txtNamaKecamatan.setText(kecamatan.getNamaKecamatan() != null ? kecamatan.getNamaKecamatan() : "");
            txtAlamatKantor.setText(kecamatan.getAlamatKantor() != null ? kecamatan.getAlamatKantor() : "");
            txtNamaKepala.setText(kecamatan.getNamaKepala() != null ? kecamatan.getNamaKepala() : "");
            txtAlamatRumahKepala.setText(kecamatan.getAlamatRumahKepala() != null ? kecamatan.getAlamatRumahKepala() : "");
            txtNoHp.setText(kecamatan.getNoHp() != null ? kecamatan.getNoHp() : "");
            
            // Load preview foto jika ada
            String fotoUrl = kecamatan.getFotoUrl();
            if (fotoUrl != null && !fotoUrl.isEmpty()) {
                selectedFotoPath = fotoUrl;
                try {
                    File imgFile = new File(fotoUrl);
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imgFile.getPath());
                        Image scaled = icon.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
                        lblFotoPreview.setIcon(new ImageIcon(scaled));
                        lblFotoPreview.setText("");
                    } else {
                        // Coba dari project path
                        String projectPath = System.getProperty("user.dir");
                        File fullPathFile = new File(projectPath + "/" + fotoUrl);
                        if (fullPathFile.exists()) {
                            ImageIcon icon = new ImageIcon(fullPathFile.getPath());
                            Image scaled = icon.getImage().getScaledInstance(140, 70, Image.SCALE_SMOOTH);
                            lblFotoPreview.setIcon(new ImageIcon(scaled));
                            lblFotoPreview.setText("");
                        }
                    }
                } catch (Exception e) {
                    lblFotoPreview.setText("Gambar tidak ditemukan");
                }
            }
        }
    }

    private boolean validateInput() {
        // Validasi sama seperti sebelumnya...
        String namaKecamatan = txtNamaKecamatan.getText().trim();
        if (namaKecamatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kecamatan tidak boleh kosong!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            txtNamaKecamatan.requestFocus();
            return false;
        }

        if (txtAlamatKantor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alamat Kantor tidak boleh kosong!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            txtAlamatKantor.requestFocus();
            return false;
        }

        String namaKepala = txtNamaKepala.getText().trim();
        if (namaKepala.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kepala tidak boleh kosong!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            txtNamaKepala.requestFocus();
            return false;
        }

        if (txtAlamatRumahKepala.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alamat Rumah Kepala tidak boleh kosong!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            txtAlamatRumahKepala.requestFocus();
            return false;
        }

        String noHp = txtNoHp.getText().trim();
        if (noHp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No HP tidak boleh kosong!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            txtNoHp.requestFocus();
            return false;
        }

        if (!noHp.matches("^[0-9]{10,15}$")) {
            JOptionPane.showMessageDialog(this, "Format No HP tidak valid! Harus 10-15 digit angka.", "Validasi Error", JOptionPane.WARNING_MESSAGE);
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
        kecamatan.setFotoUrl(selectedFotoPath); // Simpan path relatif

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