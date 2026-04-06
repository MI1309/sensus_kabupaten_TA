package com.kabupaten.util;

import com.kabupaten.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class FasilitasSeeder {
    public static void main(String[] args) {
        String[] sqlStatements = {
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('RSUD Kabupaten Jombang', 'Kesehatan', 'Dinas Kesehatan', 'Jl. KH. Wahid Hasyim No. 52, Jombang', 'Rumah sakit umum daerah tipe B dengan fasilitas lengkap.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Puskesmas Peterongan', 'Kesehatan', 'Dinas Kesehatan', 'Jl. Raya Peterongan No. 10, Peterongan', 'Pusat kesehatan masyarakat melayani rawat inap dan jalan.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Pasar Pon Jombang', 'Ekonomi', 'Dinas Perdagangan', 'Jl. KH. Wahid Hasyim, Jombang', 'Pasar tradisional terbesar di pusat kota Jombang.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Pasar Mojoagung', 'Ekonomi', 'Dinas Perdagangan', 'Jl. Raya Mojoagung, Mojoagung', 'Pusat perdagangan utama di wilayah timur Jombang.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('SMAN 1 Jombang', 'Pendidikan', 'Dinas Pendidikan', 'Jl. Pattimura No. 1, Jombang', 'Sekolah menengah atas favorit di Kabupaten Jombang.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('SMPN 1 Diwek', 'Pendidikan', 'Dinas Pendidikan', 'Jl. KH. Hasyim Asy\\'ari, Diwek', 'Sekolah menengah pertama negeri di wilayah Diwek.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Polres Jombang', 'Keamanan', 'Kepolisian RI', 'Jl. KH. Wahid Hasyim No. 62, Jombang', 'Markas Kepolisian Resor Jombang.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Polsek Gudo', 'Keamanan', 'Kepolisian RI', 'Jl. Raya Gudo No. 5, Gudo', 'Kantor polisi sektor wilayah Gudo.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Kantor Bupati Jombang', 'Pemerintahan', 'Sekretariat Daerah', 'Jl. KH. Wahid Hasyim No. 137, Jombang', 'Pusat administrasi pemerintahan Kabupaten Jombang.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Dinas Kependudukan dan Catatan Sipil', 'Pemerintahan', 'Dispendukcapil', 'Jl. KH. Wahid Hasyim, Jombang', 'Layanan pengurusan KTP, KK, dan Akta Kelahiran.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Alun-Alun Jombang', 'Publik', 'Dinas Lingkungan Hidup', 'Jl. Diponegoro, Jombang', 'Ruang terbuka hijau dan pusat kegiatan masyarakat.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Stasiun Jombang', 'Transportasi', 'PT Kereta Api Indonesia', 'Jl. Jombang - Babat, Jombang', 'Stasiun kereta api utama yang menghubungkan berbagai kota.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Terminal Mojoagung', 'Transportasi', 'Dinas Perhubungan', 'Jl. Raya Mojoagung, Mojoagung', 'Terminal bus tipe B yang melayani rute antar kota.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('GOR Merdeka Jombang', 'Olahraga', 'Dinas Kepemudaan dan Olahraga', 'Jl. Gus Dur, Jombang', 'Gedung olahraga untuk berbagai ajang pertandingan.')",
            "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES ('Stadion Merdeka', 'Olahraga', 'Dinas Kepemudaan dan Olahraga', 'Jl. Gus Dur, Jombang', 'Stadion sepak bola utama di Kabupaten Jombang.')"
        };

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Starting seeding fasilitas table...");
            int count = 0;
            for (String sql : sqlStatements) {
                stmt.addBatch(sql);
                count++;
            }
            stmt.executeBatch();
            System.out.println("Successfully seeded " + count + " records into fasilitas table.");
            
        } catch (Exception e) {
            System.err.println("Error while seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
