package com.kabupaten.view;

import com.kabupaten.model.Kabupaten;
import javax.swing.*;
import java.awt.*;

public class guest_dashboard extends JDialog {
    private boolean confirmed = false;
    private JTextField txtNamaKabupaten;
    private JTextField txtKode;
    private JButton btnSave, btnCancel;
    private Kabupaten kabupatenData;

    public guest_dashboard(Frame parent, String title, Kabupaten data) {
        super(parent, title, true);
        this.kabupatenData = data;
        initComponents();
        if (data != null) loadData();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(0,2,10,10));

        panel.add(new JLabel("Kode Kabupaten:"));
        txtKode = new JTextField(20);
        panel.add(txtKode);

        panel.add(new JLabel("Nama Kabupaten:"));
        txtNamaKabupaten = new JTextField(20);
        panel.add(txtNamaKabupaten);

        btnSave = new JButton("Simpan");
        btnSave.addActionListener(e -> { confirmed = true; dispose(); });

        btnCancel = new JButton("Batal");
        btnCancel.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        txtKode.setText(kabupatenData.getKodeKabupaten());
        txtNamaKabupaten.setText(kabupatenData.getNamaKabupaten());
    }

    public boolean isConfirmed() { return confirmed; }
    public String getKode() { return txtKode.getText(); }
    public String getNamaKabupaten() { return txtNamaKabupaten.getText(); }
}
