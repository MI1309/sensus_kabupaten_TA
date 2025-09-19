package com.kabupaten.view;

import com.kabupaten.model.RTRWMODEL;
import com.kabupaten.services.RTRWSERVICES;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CrudWargaPanel extends JPanel {
    private RTRWSERVICES service = new RTRWSERVICES();
    private JTable table;
    private DefaultTableModel tableModel;

    public CrudWargaPanel() {
        setLayout(new BorderLayout());

        // Tabel
        tableModel = new DefaultTableModel(new Object[]{"ID", "Kecamatan", "RW", "RT", "Alamat", "Status"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tombol CRUD
        JPanel buttonPanel = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Tombol
        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());

        // Data awal
        service.tambahRTRW("Kecamatan A", 1, 1, "Jl. Mawar", "Aktif");
        service.tambahRTRW("Kecamatan B", 2, 3, "Jl. Melati", "Non-Aktif");
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (RTRWMODEL r : service.getAllRTRW()) {
            tableModel.addRow(new Object[]{r.getId(), r.getKecamatan(), r.getNomorRW(), r.getNomorRT(), r.getAlamat(), r.getStatus()});
        }
    }

    private void tambahData() {
        String kecamatan = JOptionPane.showInputDialog("Kecamatan:");
        int rw = Integer.parseInt(JOptionPane.showInputDialog("RW:"));
        int rt = Integer.parseInt(JOptionPane.showInputDialog("RT:"));
        String alamat = JOptionPane.showInputDialog("Alamat:");
        String status = JOptionPane.showInputDialog("Status:");

        service.tambahRTRW(kecamatan, rw, rt, alamat, status);
        refreshTable();
    }

    private void editData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }
        int id = (int) table.getValueAt(row, 0);

        String kecamatan = JOptionPane.showInputDialog("Kecamatan:", table.getValueAt(row, 1));
        int rw = Integer.parseInt(JOptionPane.showInputDialog("RW:", table.getValueAt(row, 2)));
        int rt = Integer.parseInt(JOptionPane.showInputDialog("RT:", table.getValueAt(row, 3)));
        String alamat = JOptionPane.showInputDialog("Alamat:", table.getValueAt(row, 4));
        String status = JOptionPane.showInputDialog("Status:", table.getValueAt(row, 5));

        service.updateRTRW(id, kecamatan, rw, rt, alamat, status);
        refreshTable();
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu!");
            return;
        }
        int id = (int) table.getValueAt(row, 0);
        service.deleteRTRW(id);
        refreshTable();
    }
}
