// package com.kabupaten.view;

// import com.kabupaten.model.RTRW;
// import com.kabupaten.services.RTRWSERVICES;

// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;
// import java.awt.*;
// import java.util.*;
// import java.util.List;

// public class CrudRTRWPanel extends JPanel {
//     private RTRWSERVICES service = new RTRWSERVICES();
//     private JTable table;
//     private DefaultTableModel tableModel;
//     private JTextField txtSearch;

//     public CrudRTRWPanel() {
//         setLayout(new BorderLayout());

//         // Panel pencarian
//         JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         searchPanel.add(new JLabel("Pencarian:"));
//         txtSearch = new JTextField(20);
//         JButton btnSearch = new JButton("Cari");
//         JButton btnRefresh = new JButton("Refresh");
        
//         searchPanel.add(txtSearch);
//         searchPanel.add(btnSearch);
//         searchPanel.add(btnRefresh);
//         add(searchPanel, BorderLayout.NORTH);

//         // Tabel
//         tableModel = new DefaultTableModel(
//             new Object[]{"ID", "Desa", "RW", "RT", "Nama Ketua", "Kontak", "Alamat"}, 0
//         ) {
//             @Override
//             public boolean isCellEditable(int row, int column) {
//                 return false;
//             }
//         };
        
//         table = new JTable(tableModel);
//         table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//         JScrollPane scrollPane = new JScrollPane(table);
//         add(scrollPane, BorderLayout.CENTER);

//         // Panel tombol CRUD
//         JPanel buttonPanel = new JPanel(new FlowLayout());
//         JButton btnTambah = new JButton("Tambah");
//         JButton btnEdit = new JButton("Edit");
//         JButton btnHapus = new JButton("Hapus");
//         JButton btnDetail = new JButton("Detail");

//         buttonPanel.add(btnTambah);
//         buttonPanel.add(btnEdit);
//         buttonPanel.add(btnHapus);
//         buttonPanel.add(btnDetail);
//         add(buttonPanel, BorderLayout.SOUTH);

//         // Event handlers
//         btnTambah.addActionListener(e -> tambahData());
//         btnEdit.addActionListener(e -> editData());
//         btnHapus.addActionListener(e -> hapusData());
//         btnDetail.addActionListener(e -> showDetail());
//         btnSearch.addActionListener(e -> searchData());
//         btnRefresh.addActionListener(e -> refreshTable());
        
//         // Enter key untuk search
//         txtSearch.addActionListener(e -> searchData());
        
//         // Load initial data
//         refreshTable();
        
//         // Debug: print semua data saat panel dibuat
//         debugPrintAllData();
        
//         // Cek apakah ada data duplikat existing
//         checkExistingDuplicates();
//     }

//     private void refreshTable() {
//         tableModel.setRowCount(0);
//         Set<Integer> addedIds = new HashSet<>(); // Mencegah duplikat
        
//         try {
//             for (RTRW rtrw : service.getAllRTRW()) {
//                 // Cek apakah ID sudah ditambahkan
//                 if (!addedIds.contains(rtrw.getIdRtrw())) {
//                     tableModel.addRow(new Object[]{
//                         rtrw.getIdRtrw(),
//                         rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
//                         rtrw.getRw(),
//                         rtrw.getRt(),
//                         rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
//                         rtrw.getKontak() != null ? rtrw.getKontak() : "-",
//                         rtrw.getAlamat() != null ? rtrw.getAlamat() : "-"
//                     });
//                     addedIds.add(rtrw.getIdRtrw());
//                     txtSearch.setText("");
//                 }
//             }
//         } catch (Exception e) {
//             JOptionPane.showMessageDialog(this, 
//                 "Error memuat data: " + e.getMessage(), 
//                 "Error", 
//                 JOptionPane.ERROR_MESSAGE);
//             e.printStackTrace();
//         }
//     }
    
//     private void searchData() {
//         String keyword = txtSearch.getText().trim();
//         if (keyword.isEmpty()) {
//             refreshTable();
//             return;
//         }
        
//         tableModel.setRowCount(0);
//         Set<Integer> addedIds = new HashSet<>(); // Mencegah duplikat
        
//         try {
//             for (RTRW rtrw : service.searchRTRW(keyword)) {
//                 // Cek apakah ID sudah ditambahkan
//                 if (!addedIds.contains(rtrw.getIdRtrw())) {
//                     tableModel.addRow(new Object[]{
//                         rtrw.getIdRtrw(),
//                         rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
//                         rtrw.getRw(),
//                         rtrw.getRt(),
//                         rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
//                         rtrw.getKontak() != null ? rtrw.getKontak() : "-",
//                         rtrw.getAlamat() != null ? rtrw.getAlamat() : "-"
//                     });
//                     addedIds.add(rtrw.getIdRtrw());
//                 }
//             }
//         } catch (Exception e) {
//             JOptionPane.showMessageDialog(this, 
//                 "Error pencarian: " + e.getMessage(), 
//                 "Error", 
//                 JOptionPane.ERROR_MESSAGE);
//             e.printStackTrace();
//         }
//     }

//     /**
//      * Validasi apakah kombinasi Desa-RT-RW sudah ada
//      * @param namaDesa Nama desa yang akan dicek
//      * @param rt Nomor RT (sudah dalam format 3 digit, misal: "001")
//      * @param rw Nomor RW (sudah dalam format 3 digit, misal: "005")
//      * @param excludeId ID yang dikecualikan (untuk mode edit, null untuk mode tambah)
//      * @return true jika data duplikat, false jika tidak duplikat
//      */
//     private boolean isDuplicateRTRW(String namaDesa, String rt, String rw, Integer excludeId) {
//         try {
//             List<RTRW> allData = service.getAllRTRW();
            
//             System.out.println("=== CEK DUPLIKAT DESA-RT-RW ===");
//             System.out.println("Input - Desa: " + namaDesa + ", RT: " + rt + ", RW: " + rw + ", ExcludeID: " + excludeId);
//             System.out.println("Total data di database: " + allData.size());
            
//             for (RTRW existing : allData) {
//                 // Skip jika ini adalah data yang sedang diedit
//                 if (excludeId != null && existing.getIdRtrw() == excludeId) {
//                     System.out.println("Skip ID " + existing.getIdRtrw() + " (sedang diedit)");
//                     continue;
//                 }
                
//                 // Normalisasi data untuk perbandingan
//                 String existingDesa = existing.getNamaDesa() != null ? existing.getNamaDesa().trim() : "";
//                 String existingRt = existing.getRt() != null ? existing.getRt().trim() : "";
//                 String existingRw = existing.getRw() != null ? existing.getRw().trim() : "";
                
//                 String inputDesa = namaDesa != null ? namaDesa.trim() : "";
//                 String inputRt = rt != null ? rt.trim() : "";
//                 String inputRw = rw != null ? rw.trim() : "";
                
//                 // Cek duplikat (case-insensitive untuk nama desa)
//                 boolean desaMatch = existingDesa.equalsIgnoreCase(inputDesa);
//                 boolean rtMatch = existingRt.equals(inputRt);
//                 boolean rwMatch = existingRw.equals(inputRw);
                
//                 System.out.println("Bandingkan dengan ID " + existing.getIdRtrw() + 
//                                  ": Desa=" + existingDesa + " RT=" + existingRt + " RW=" + existingRw +
//                                  " | Match: Desa=" + desaMatch + " RT=" + rtMatch + " RW=" + rwMatch);
                
//                 if (desaMatch && rtMatch && rwMatch) {
//                     System.out.println(">>> DUPLIKAT DITEMUKAN! ID: " + existing.getIdRtrw());
//                     return true;
//                 }
//             }
            
//             System.out.println(">>> Tidak ada duplikat Desa-RT-RW");
//             return false;
            
//         } catch (Exception e) {
//             System.err.println("Error saat cek duplikat: " + e.getMessage());
//             e.printStackTrace();
//         }
//         return false;
//     }
    
//     /**
//      * Cek apakah nama ketua sudah ada (opsional - uncomment jika ingin aktifkan)
//      */
//     private boolean isDuplicateNamaKetua(String namaKetua, Integer excludeId) {
//         if (namaKetua == null || namaKetua.trim().isEmpty()) {
//             return false; // Nama ketua boleh kosong
//         }
        
//         try {
//             List<RTRW> allData = service.getAllRTRW();
            
//             System.out.println("=== CEK DUPLIKAT NAMA KETUA ===");
//             System.out.println("Input - Nama Ketua: " + namaKetua + ", ExcludeID: " + excludeId);
            
//             for (RTRW existing : allData) {
//                 if (excludeId != null && existing.getIdRtrw() == excludeId) {
//                     continue;
//                 }
                
//                 String existingNama = existing.getNamaKetua() != null ? existing.getNamaKetua().trim() : "";
//                 String inputNama = namaKetua.trim();
                
//                 if (!existingNama.isEmpty() && existingNama.equalsIgnoreCase(inputNama)) {
//                     System.out.println(">>> NAMA KETUA DUPLIKAT! Sudah digunakan di ID: " + existing.getIdRtrw());
//                     System.out.println("    Desa: " + existing.getNamaDesa() + " RT " + existing.getRt() + " RW " + existing.getRw());
//                     return true;
//                 }
//             }
            
//             System.out.println(">>> Nama ketua unik");
//             return false;
            
//         } catch (Exception e) {
//             System.err.println("Error saat cek duplikat nama ketua: " + e.getMessage());
//             e.printStackTrace();
//         }
//         return false;
//     }
    
//     /**
//      * Cek statistik desa - berapa banyak RT/RW per desa
//      */
//     private void showDesaStatistics(String namaDesa) {
//         try {
//             List<RTRW> dataByDesa = service.getRTRWByDesaName(namaDesa);
            
//             if (dataByDesa.isEmpty()) {
//                 System.out.println("Desa '" + namaDesa + "' tidak ditemukan.");
//                 return;
//             }
            
//             System.out.println("\n=== STATISTIK DESA: " + namaDesa.toUpperCase() + " ===");
//             System.out.println("Total RT/RW: " + dataByDesa.size());
//             System.out.println("\nDaftar RT/RW:");
            
//             for (RTRW rtrw : dataByDesa) {
//                 System.out.println("  - RT " + rtrw.getRt() + " RW " + rtrw.getRw() + 
//                                  " (Ketua: " + (rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-") + ")");
//             }
//             System.out.println("===========================================\n");
            
//         } catch (Exception e) {
//             System.err.println("Error saat tampilkan statistik desa: " + e.getMessage());
//         }
//     }
    
//     /**
//      * Menampilkan semua data RTRW untuk debugging
//      */
//     private void debugPrintAllData() {
//         try {
//             List<RTRW> allData = service.getAllRTRW();
//             System.out.println("\n=== SEMUA DATA RTRW ===");
//             System.out.println("Total: " + allData.size() + " records");
//             System.out.println("ID\tDesa\t\tRT\tRW");
//             System.out.println("-------------------------------------------");
            
//             for (RTRW rtrw : allData) {
//                 System.out.println(
//                     rtrw.getIdRtrw() + "\t" +
//                     (rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "NULL") + "\t\t" +
//                     (rtrw.getRt() != null ? rtrw.getRt() : "NULL") + "\t" +
//                     (rtrw.getRw() != null ? rtrw.getRw() : "NULL")
//                 );
//             }
//             System.out.println("===========================================\n");
            
//         } catch (Exception e) {
//             System.err.println("Error saat debug print: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }
    
//     /**
//      * Cek apakah ada data duplikat di database dan tampilkan
//      */
//     private void checkExistingDuplicates() {
//         try {
//             List<RTRW> allData = service.getAllRTRW();
//             Set<String> uniqueKeys = new HashSet<>();
//             Set<String> uniqueNamaKetua = new HashSet<>();
//             List<String> duplicatesRTRW = new ArrayList<>();
//             List<String> duplicatesNamaKetua = new ArrayList<>();
            
//             // Cek duplikat Desa-RT-RW
//             for (RTRW rtrw : allData) {
//                 String desa = rtrw.getNamaDesa() != null ? rtrw.getNamaDesa().toLowerCase().trim() : "";
//                 String rt   = rtrw.getRt() != null ? rtrw.getRt().toLowerCase().trim() : "";
//                 String rw   = rtrw.getRw() != null ? rtrw.getRw().toLowerCase().trim() : "";
//             //
//                 String key = desa + "|" + rt + "|" + rw;
                
//                 if (uniqueKeys.contains(key)) {
//                     duplicatesRTRW.add("ID " + rtrw.getIdRtrw() + ": " + 
//                                  rtrw.getNamaDesa() + " RT " + rtrw.getRt() + " RW " + rtrw.getRw());
//                 } else {
//                     uniqueKeys.add(key);
//                 }
                
//                 // Cek duplikat nama ketua
//                 if (rtrw.getNamaKetua() != null && !rtrw.getNamaKetua().trim().isEmpty()) {
//                     String namaKetuaKey = rtrw.getNamaKetua().toLowerCase().trim();
//                     if (uniqueNamaKetua.contains(namaKetuaKey)) {
//                         duplicatesNamaKetua.add("ID " + rtrw.getIdRtrw() + ": " + 
//                                               rtrw.getNamaKetua() + " di " + 
//                                               rtrw.getNamaDesa() + " RT " + rtrw.getRt() + " RW " + rtrw.getRw());
//                     } else {
//                         uniqueNamaKetua.add(namaKetuaKey);
//                     }
//                 }
//             }
            
//             // Statistik per desa
//             Map<String, Integer> desaCount = new HashMap<>();
//             for (RTRW rtrw : allData) {
//                 String desa = rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Unknown";
//                 desaCount.put(desa, desaCount.getOrDefault(desa, 0) + 1);
//             }
            
//             // Tampilkan hasil
//             StringBuilder message = new StringBuilder();
//             // message.append("=== LAPORAN DUPLIKAT DATABASE ===\n\n");
            
//             // 1. Duplikat Desa-RT-RW
//             if (!duplicatesRTRW.isEmpty()) {
//                 message.append("⚠️ DUPLIKAT DESA-RT-RW: ").append(duplicatesRTRW.size()).append(" data\n");
//                 System.out.println("\n!!! PERINGATAN: Ditemukan duplikat Desa-RT-RW !!!");
//                 for (String dup : duplicatesRTRW) {
//                     message.append("  - ").append(dup).append("\n");
//                     System.out.println("- " + dup);
//                 }
//                 message.append("\n");
//             }
            
//             // 2. Duplikat Nama Ketua
//             if (!duplicatesNamaKetua.isEmpty()) {
//                 message.append("⚠️ DUPLIKAT NAMA KETUA: ").append(duplicatesNamaKetua.size()).append(" data\n");
//                 System.out.println("\n!!! PERINGATAN: Ditemukan duplikat Nama Ketua !!!");
//                 for (String dup : duplicatesNamaKetua) {
//                     message.append("  - ").append(dup).append("\n");
//                     System.out.println("- " + dup);
//                 }
//                 message.append("\n");
//             } else {
//                 message.append("✅ Tidak ada duplikat Nama Ketua\n\n");
//                 System.out.println("✅ Tidak ada duplikat Nama Ketua");
//             }
        
            
//         } catch (Exception e) {
//             System.err.println("Error saat cek duplikat existing: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     private void tambahData() {
//         JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

//         JTextField txtDesa = new JTextField();
//         JTextField txtRW = new JTextField();
//         JTextField txtRT = new JTextField();
//         JTextField txtNamaKetua = new JTextField();
//         JTextField txtKontak = new JTextField();
//         JTextArea txtAlamat = new JTextArea(3, 20);
//         txtAlamat.setLineWrap(true);
//         txtAlamat.setWrapStyleWord(true);
//         JScrollPane scrollAlamat = new JScrollPane(txtAlamat);

//         panel.add(new JLabel("Desa:*"));
//         panel.add(txtDesa);
//         panel.add(new JLabel("RW:*"));
//         panel.add(txtRW);
//         panel.add(new JLabel("RT:*"));
//         panel.add(txtRT);
//         panel.add(new JLabel("Nama Ketua:"));
//         panel.add(txtNamaKetua);
//         panel.add(new JLabel("Kontak:"));
//         panel.add(txtKontak);
//         panel.add(new JLabel("Alamat:*"));
//         panel.add(scrollAlamat);

//         int result = JOptionPane.showConfirmDialog(
//             this,
//             panel,
//             "Tambah Data RTRW",
//             JOptionPane.OK_CANCEL_OPTION,
//             JOptionPane.PLAIN_MESSAGE
//         );

//         if (result == JOptionPane.OK_OPTION) {
//             try {
//                 String namaDesa = txtDesa.getText().trim();
//                 String rwStr = txtRW.getText().trim();
//                 String rtStr = txtRT.getText().trim();
//                 String namaKetua = txtNamaKetua.getText().trim();
//                 String kontak = txtKontak.getText().trim();
//                 String alamat = txtAlamat.getText().trim();

//                 // Validasi field wajib
//                 if (namaDesa.isEmpty() || rwStr.isEmpty() || rtStr.isEmpty() || alamat.isEmpty()) {
//                     JOptionPane.showMessageDialog(this, 
//                         "Desa, RW, RT, dan Alamat wajib diisi!", 
//                         "Peringatan", 
//                         JOptionPane.WARNING_MESSAGE);
//                     return;
//                 }

//                 // Validasi format angka untuk RW dan RT
//                 if (!rwStr.matches("\\d+") || !rtStr.matches("\\d+")) {
//                     JOptionPane.showMessageDialog(this, 
//                         "RW dan RT harus berupa angka!", 
//                         "Error", 
//                         JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 int rw = Integer.parseInt(rwStr);
//                 int rt = Integer.parseInt(rtStr);

//                 // Validasi rentang nilai
//                 if (rw <= 0 || rt <= 0) {
//                     JOptionPane.showMessageDialog(this, 
//                         "RW dan RT harus lebih dari 0!", 
//                         "Error", 
//                         JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 // Cek duplikat
//                 if (isDuplicateRTRW(namaDesa, String.format("%03d", rt), String.format("%03d", rw), null)) {
//                     JOptionPane.showMessageDialog(this, 
//                         "Data RT/RW untuk desa ini sudah ada!\nDesa: " + namaDesa + ", RT: " + rt + ", RW: " + rw, 
//                         "Data Duplikat", 
//                         JOptionPane.WARNING_MESSAGE);
//                     return;
//                 }
//                 // Cek duplikat nama ketua (opsional)
//                 if (!namaKetua.isEmpty() && isDuplicateNamaKetua(namaKetua, null)) {
//                     int response = JOptionPane.showConfirmDialog(this,
//                         "Nama ketua '" + namaKetua + "' sudah digunakan di RT/RW lain!\n" +
//                         "Apakah Anda yakin ingin tetap menggunakan nama ini?",
//                         "Peringatan Nama Ketua Duplikat",
//                         JOptionPane.YES_NO_OPTION,
//                         JOptionPane.WARNING_MESSAGE);
                    
//                     if (response != JOptionPane.YES_OPTION) {
//                         return;
//                     }
//                 }
//                 // cek nama desa duplikat
//                 if (!namaDesa.isEmpty() && isDuplicateNamaKetua(namaDesa, null)) {
//                     int response = JOptionPane.showConfirmDialog(this,
//                         "Nama ketua '" + namaDesa + "' sudah digunakan di RT/RW lain!\n" +
//                         "Apakah Anda yakin ingin tetap menggunakan nama ini?",
//                         "Peringatan Nama Ketua Duplikat",
//                         JOptionPane.YES_NO_OPTION,
//                         JOptionPane.WARNING_MESSAGE);
                    
//                     if (response != JOptionPane.YES_OPTION) {
//                         return;
//                     }
//                 }

//                 // Format RT dan RW dengan padding 0
//                 String rtFormatted = String.format("%03d", rt);
//                 String rwFormatted = String.format("%03d", rw);

//                 boolean success = service.addRTRW(namaDesa, rw, rt, namaKetua, kontak, alamat, "Aktif");
                
//                 if (success) {
//                     JOptionPane.showMessageDialog(this, 
//                         "Data berhasil ditambahkan!\nDesa: " + namaDesa + "\nRT: " + rtFormatted + "\nRW: " + rwFormatted);
//                     refreshTable();
//                     txtSearch.setText(""); // Clear search
//                 } else {
//                     JOptionPane.showMessageDialog(this, 
//                         "Gagal menambahkan data!", 
//                         "Error", 
//                         JOptionPane.ERROR_MESSAGE);
//                 }

//             } catch (NumberFormatException e) {
//                 JOptionPane.showMessageDialog(this, 
//                     "RT dan RW harus berupa angka yang valid!", 
//                     "Error", 
//                     JOptionPane.ERROR_MESSAGE);
//             } catch (Exception e) {
//                 JOptionPane.showMessageDialog(this, 
//                     "Error: " + e.getMessage(), 
//                     "Error", 
//                     JOptionPane.ERROR_MESSAGE);
//                 e.printStackTrace();
//             }
//         }
//     }

//     private void editData() {
//         int selectedRow = table.getSelectedRow();
//         if (selectedRow == -1) {
//             JOptionPane.showMessageDialog(this, 
//                 "Silakan pilih data yang akan diedit!", 
//                 "Peringatan", 
//                 JOptionPane.WARNING_MESSAGE);
//             return;
//         }

//         int id = (Integer) table.getValueAt(selectedRow, 0);
//         RTRW rtrw = service.getRTRWById(id);
        
//         if (rtrw == null) {
//             JOptionPane.showMessageDialog(this, 
//                 "Data tidak ditemukan!", 
//                 "Error", 
//                 JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         // Create panel with form fields
//         JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

//         JTextField txtDesa = new JTextField(rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "");
//         JTextField txtRW = new JTextField(rtrw.getRw());
//         JTextField txtRT = new JTextField(rtrw.getRt());
//         JTextField txtNamaKetua = new JTextField(rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "");
//         JTextField txtKontak = new JTextField(rtrw.getKontak() != null ? rtrw.getKontak() : "");
//         JTextArea txtAlamat = new JTextArea(rtrw.getAlamat() != null ? rtrw.getAlamat() : "", 3, 20);
//         txtAlamat.setLineWrap(true);
//         txtAlamat.setWrapStyleWord(true);
//         JScrollPane scrollAlamat = new JScrollPane(txtAlamat);

//         panel.add(new JLabel("Desa:*"));
//         panel.add(txtDesa);
//         panel.add(new JLabel("RW:*"));
//         panel.add(txtRW);
//         panel.add(new JLabel("RT:*"));
//         panel.add(txtRT);
//         panel.add(new JLabel("Nama Ketua:"));
//         panel.add(txtNamaKetua);
//         panel.add(new JLabel("Kontak:"));
//         panel.add(txtKontak);
//         panel.add(new JLabel("Alamat:*"));
//         panel.add(scrollAlamat);

//         int result = JOptionPane.showConfirmDialog(
//             this,
//             panel,
//             "Edit Data RTRW",
//             JOptionPane.OK_CANCEL_OPTION,
//             JOptionPane.PLAIN_MESSAGE
//         );

//         if (result == JOptionPane.OK_OPTION) {
//             try {
//                 String namaDesa = txtDesa.getText().trim();
//                 String rwStr = txtRW.getText().trim();
//                 String rtStr = txtRT.getText().trim();
//                 String namaKetua = txtNamaKetua.getText().trim();
//                 String kontak = txtKontak.getText().trim();
//                 String alamat = txtAlamat.getText().trim();

//                 // Validasi field wajib
//                 if (namaDesa.isEmpty() || rwStr.isEmpty() || rtStr.isEmpty() || alamat.isEmpty()) {
//                     JOptionPane.showMessageDialog(this, 
//                         "Desa, RW, RT, dan Alamat wajib diisi!", 
//                         "Peringatan", 
//                         JOptionPane.WARNING_MESSAGE);
//                     return;
//                 }

//                 // Validasi format angka
//                 if (!rwStr.matches("\\d+") || !rtStr.matches("\\d+")) {
//                     JOptionPane.showMessageDialog(this, 
//                         "RW dan RT harus berupa angka!", 
//                         "Error", 
//                         JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 int rw = Integer.parseInt(rwStr);
//                 int rt = Integer.parseInt(rtStr);

//                 // Validasi rentang nilai
//                 if (rw <= 0 || rt <= 0) {
//                     JOptionPane.showMessageDialog(this, 
//                         "RW dan RT harus lebih dari 0!", 
//                         "Error", 
//                         JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 // Format RT dan RW
//                 String rtFormatted = String.format("%03d", rt);
//                 String rwFormatted = String.format("%03d", rw);

//                 // Cek duplikat (exclude data yang sedang diedit)
//                 if (isDuplicateRTRW(namaDesa, rtFormatted, rwFormatted, id)) {
//                     JOptionPane.showMessageDialog(this, 
//                         "Data RT/RW untuk desa ini sudah ada!\nDesa: " + namaDesa + ", RT: " + rt + ", RW: " + rw, 
//                         "Data Duplikat", 
//                         JOptionPane.WARNING_MESSAGE);
//                     return;
//                 }

//                 boolean success = service.updateRTRW(id, namaDesa, rw, rt, alamat, "Aktif");
                
//                 if (success) {
//                     JOptionPane.showMessageDialog(this, 
//                         "Data berhasil diupdate!");
//                     refreshTable();
//                 } else {
//                     JOptionPane.showMessageDialog(this, 
//                         "Gagal mengupdate data!", 
//                         "Error", 
//                         JOptionPane.ERROR_MESSAGE);
//                 }
//             } catch (NumberFormatException e) {
//                 JOptionPane.showMessageDialog(this, 
//                     "RT dan RW harus berupa angka yang valid!", 
//                     "Error", 
//                     JOptionPane.ERROR_MESSAGE);
//             } catch (Exception e) {
//                 JOptionPane.showMessageDialog(this, 
//                     "Error: " + e.getMessage(), 
//                     "Error", 
//                     JOptionPane.ERROR_MESSAGE);
//                 e.printStackTrace();
//             }
//         }
//     }

//     private void hapusData() {
//         int selectedRow = table.getSelectedRow();
//         if (selectedRow == -1) {
//             JOptionPane.showMessageDialog(this, 
//                 "Silakan pilih data yang akan dihapus!", 
//                 "Peringatan", 
//                 JOptionPane.WARNING_MESSAGE);
//             return;
//         }

//         int result = JOptionPane.showConfirmDialog(
//             this,
//             "Apakah Anda yakin ingin menghapus data ini?\nData yang sudah dihapus tidak dapat dikembalikan!",
//             "Konfirmasi Hapus",
//             JOptionPane.YES_NO_OPTION,
//             JOptionPane.WARNING_MESSAGE
//         );

//         if (result == JOptionPane.YES_OPTION) {
//             int id = (Integer) table.getValueAt(selectedRow, 0);
//             try {
//                 boolean success = service.deleteRTRW(id);
//                 if (success) {
//                     JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
//                     refreshTable();
//                     txtSearch.setText(""); // Clear search
//                 } else {
//                     JOptionPane.showMessageDialog(this, 
//                         "Gagal menghapus data!", 
//                         "Error", 
//                         JOptionPane.ERROR_MESSAGE);
//                 }
//             } catch (Exception e) {
//                 JOptionPane.showMessageDialog(this, 
//                     "Error: " + e.getMessage(), 
//                     "Error", 
//                     JOptionPane.ERROR_MESSAGE);
//                 e.printStackTrace();
//             }
//         }
//     }
    
//     private void showDetail() {
//         int selectedRow = table.getSelectedRow();
//         if (selectedRow == -1) {
//             JOptionPane.showMessageDialog(this, 
//                 "Silakan pilih data untuk melihat detail!", 
//                 "Peringatan", 
//                 JOptionPane.WARNING_MESSAGE);
//             return;
//         }

//         int id = (Integer) table.getValueAt(selectedRow, 0);
//         RTRW rtrw = service.getRTRWById(id);
        
//         if (rtrw != null) {
//             String detail = String.format(
//                 "=== DETAIL RTRW ===\n\n" +
//                 "ID: %d\n" +
//                 "Desa: %s\n" +
//                 "RT: %s\n" +
//                 "RW: %s\n" +
//                 "Nama Ketua: %s\n" +
//                 "Kontak: %s\n" +
//                 "Alamat: %s\n" +
//                 "Dibuat: %s\n" +
//                 "Diupdate: %s",
//                 rtrw.getIdRtrw(),
//                 rtrw.getNamaDesa() != null ? rtrw.getNamaDesa() : "Tidak Ada",
//                 rtrw.getRt(),
//                 rtrw.getRw(),
//                 rtrw.getNamaKetua() != null ? rtrw.getNamaKetua() : "-",
//                 rtrw.getKontak() != null ? rtrw.getKontak() : "-",
//                 rtrw.getAlamat() != null ? rtrw.getAlamat() : "-",
//                 rtrw.getCreatedAt(),
//                 rtrw.getUpdatedAt()
//             );
            
//             JTextArea textArea = new JTextArea(detail);
//             textArea.setEditable(false);
//             textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
//             JScrollPane scrollPane = new JScrollPane(textArea);
//             scrollPane.setPreferredSize(new Dimension(400, 300));
            
//             JOptionPane.showMessageDialog(this, 
//                 scrollPane, 
//                 "Detail RTRW", 
//                 JOptionPane.INFORMATION_MESSAGE);
//         } else {
//             JOptionPane.showMessageDialog(this, 
//                 "Data tidak ditemukan!", 
//                 "Error", 
//                 JOptionPane.ERROR_MESSAGE);
//         }
//     }
// }