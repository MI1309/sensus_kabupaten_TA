package com.kabupaten.dao;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.Kecamatan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk operasi CRUD Kecamatan
 */
public class KecamatanDAO {

    private Connection connection;

    public KecamatanDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Mengambil semua data Kecamatan dengan jumlah desa dan kelurahan
     */
    public List<Kecamatan> getAllKecamatan() {
        List<Kecamatan> kecamatanList = new ArrayList<>();
        String sql = "SELECT k.*, " +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'DESA') as jumlah_desa, "
                +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'KELURAHAN') as jumlah_kelurahan, "
                +
                "(SELECT COUNT(*) FROM warga w LEFT JOIN desa_kelurahan d ON w.id_desa = d.id_desa WHERE d.id_kecamatan = k.id_kecamatan) as jumlah_penduduk "
                +
                "FROM kecamatan k " +
                "ORDER BY k.id_kecamatan ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Kecamatan kecamatan = mapResultSetToKecamatan(rs);
                kecamatanList.add(kecamatan);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data Kecamatan: " + e.getMessage());
            e.printStackTrace();
        }

        return kecamatanList;
    }

    /**
     * Mencari Kecamatan berdasarkan nama kecamatan atau nama kepala
     */
    public List<Kecamatan> searchKecamatan(String keyword) {
        List<Kecamatan> kecamatanList = new ArrayList<>();
        String sql = "SELECT k.*, " +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'DESA') as jumlah_desa, "
                +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'KELURAHAN') as jumlah_kelurahan "
                +
                "FROM kecamatan k " +
                "WHERE k.nama_kecamatan LIKE ? OR k.nama_kepala LIKE ? OR k.alamat_kantor LIKE ? " +
                "ORDER BY k.id_kecamatan ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Kecamatan kecamatan = mapResultSetToKecamatan(rs);
                    kecamatanList.add(kecamatan);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari Kecamatan: " + e.getMessage());
            e.printStackTrace();
        }

        return kecamatanList;
    }

    /**
     * Cek apakah nama kecamatan sudah ada
     */
    public boolean isDuplicateKecamatan(String namaKecamatan, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM kecamatan WHERE nama_kecamatan = ?";

        if (excludeId != null) {
            sql += " AND id_kecamatan != ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaKecamatan);

            if (excludeId != null) {
                stmt.setInt(2, excludeId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cek duplikat Kecamatan: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Menambah Kecamatan baru dengan pengecekan duplikat
     */
    public boolean addKecamatan(Kecamatan kecamatan) {
        // Cek duplikat terlebih dahulu
        if (isDuplicateKecamatan(kecamatan.getNamaKecamatan(), null)) {
            System.err.println("Data Kecamatan duplikat: " + kecamatan.getNamaKecamatan());
            return false;
        }

        String sql = "INSERT INTO kecamatan (nama_kecamatan, alamat_kantor, nama_kepala, " +
                "alamat_rumah_kepala, no_hp) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kecamatan.getNamaKecamatan());
            stmt.setString(2, kecamatan.getAlamatKantor());
            stmt.setString(3, kecamatan.getNamaKepala());
            stmt.setString(4, kecamatan.getAlamatRumahKepala());
            stmt.setString(5, kecamatan.getNoHp());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Ambil ID yang baru dibuat
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        kecamatan.setIdKecamatan(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Data Kecamatan berhasil ditambahkan dengan ID: " + kecamatan.getIdKecamatan());
                return true;
            }

            return false;
        } catch (SQLException e) {
            System.err.println("Error menambah Kecamatan: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengupdate data Kecamatan dengan pengecekan duplikat
     */
    public boolean updateKecamatan(Kecamatan kecamatan) {
        // Cek duplikat terlebih dahulu (exclude ID yang sedang diedit)
        if (isDuplicateKecamatan(kecamatan.getNamaKecamatan(), kecamatan.getIdKecamatan())) {
            System.err.println("Data Kecamatan duplikat: " + kecamatan.getNamaKecamatan());
            return false;
        }

        String sql = "UPDATE kecamatan SET nama_kecamatan=?, alamat_kantor=?, nama_kepala=?, " +
                "alamat_rumah_kepala=?, no_hp=? WHERE id_kecamatan=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kecamatan.getNamaKecamatan());
            stmt.setString(2, kecamatan.getAlamatKantor());
            stmt.setString(3, kecamatan.getNamaKepala());
            stmt.setString(4, kecamatan.getAlamatRumahKepala());
            stmt.setString(5, kecamatan.getNoHp());
            stmt.setInt(6, kecamatan.getIdKecamatan());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data Kecamatan berhasil diupdate. ID: " + kecamatan.getIdKecamatan());
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate Kecamatan: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus Kecamatan
     * WARNING: Akan menghapus semua desa/kelurahan yang terkait jika menggunakan
     * CASCADE
     */
    public boolean deleteKecamatan(int idKecamatan) {
        // Cek apakah ada desa/kelurahan yang terkait
        int jumlahDesa = getJumlahDesaByKecamatan(idKecamatan);
        if (jumlahDesa > 0) {
            System.err.println("Tidak bisa menghapus kecamatan. Masih ada " + jumlahDesa + " desa/kelurahan terkait.");
            return false;
        }

        String sql = "DELETE FROM kecamatan WHERE id_kecamatan=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKecamatan);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data Kecamatan berhasil dihapus. ID: " + idKecamatan);
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error menghapus Kecamatan: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil Kecamatan berdasarkan ID dengan jumlah desa dan kelurahan
     */
    public Kecamatan getKecamatanById(int idKecamatan) {
        String sql = "SELECT k.*, " +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'DESA') as jumlah_desa, "
                +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'KELURAHAN') as jumlah_kelurahan "
                +
                "FROM kecamatan k " +
                "WHERE k.id_kecamatan = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKecamatan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKecamatan(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil Kecamatan by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mengambil Kecamatan berdasarkan nama
     */
    public Kecamatan getKecamatanByName(String namaKecamatan) {
        String sql = "SELECT k.*, " +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'DESA') as jumlah_desa, "
                +
                "(SELECT COUNT(*) FROM desa_kelurahan d WHERE d.id_kecamatan = k.id_kecamatan AND d.jenis = 'KELURAHAN') as jumlah_kelurahan "
                +
                "FROM kecamatan k " +
                "WHERE k.nama_kecamatan = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaKecamatan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKecamatan(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil Kecamatan by name: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mengambil daftar nama kecamatan yang unik
     */
    public List<String> getAllKecamatanNames() {
        List<String> kecamatanList = new ArrayList<>();
        String sql = "SELECT nama_kecamatan FROM kecamatan WHERE nama_kecamatan IS NOT NULL ORDER BY nama_kecamatan";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String namaKecamatan = rs.getString("nama_kecamatan");
                if (namaKecamatan != null && !namaKecamatan.isEmpty()) {
                    kecamatanList.add(namaKecamatan);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil daftar nama kecamatan: " + e.getMessage());
            e.printStackTrace();
        }

        return kecamatanList;
    }

    /**
     * Menghitung jumlah desa berdasarkan kecamatan dan jenis
     */
    public int getJumlahDesaByKecamatan(int idKecamatan, String jenis) {
        String sql = "SELECT COUNT(*) as total FROM desa_kelurahan WHERE id_kecamatan = ? AND jenis = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKecamatan);
            stmt.setString(2, jenis);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung jumlah desa: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Menghitung total desa dan kelurahan berdasarkan kecamatan
     */
    public int getJumlahDesaByKecamatan(int idKecamatan) {
        String sql = "SELECT COUNT(*) as total FROM desa_kelurahan WHERE id_kecamatan = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKecamatan);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total desa/kelurahan: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Helper method untuk mapping ResultSet ke object Kecamatan
     */
    private Kecamatan mapResultSetToKecamatan(ResultSet rs) throws SQLException {
        Kecamatan kecamatan = new Kecamatan();
        kecamatan.setIdKecamatan(rs.getInt("id_kecamatan"));
        kecamatan.setNamaKecamatan(rs.getString("nama_kecamatan"));
        kecamatan.setAlamatKantor(rs.getString("alamat_kantor"));
        kecamatan.setNamaKepala(rs.getString("nama_kepala"));
        kecamatan.setAlamatRumahKepala(rs.getString("alamat_rumah_kepala"));
        kecamatan.setNoHp(rs.getString("no_hp"));
        kecamatan.setCreatedAt(rs.getTimestamp("created_at"));
        kecamatan.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Set jumlah desa dan kelurahan dari query
        try {
            kecamatan.setJumlahDesa(rs.getInt("jumlah_desa"));
            kecamatan.setJumlahKelurahan(rs.getInt("jumlah_kelurahan"));
        } catch (SQLException e) {
            // Jika kolom tidak ada, set ke 0
            kecamatan.setJumlahDesa(0);
            kecamatan.setJumlahKelurahan(0);
        }

        // Set jumlah penduduk from query
        try {
            kecamatan.setJumlahPenduduk(rs.getInt("jumlah_penduduk"));
        } catch (SQLException e) {
            kecamatan.setJumlahPenduduk(0);
        }

        return kecamatan;
    }

    /**
     * Mendapatkan total jumlah Kecamatan
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as total FROM kecamatan";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total Kecamatan: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Mendapatkan statistik lengkap kecamatan
     */
    public String getStatistikKecamatan(int idKecamatan) {
        Kecamatan kec = getKecamatanById(idKecamatan);
        if (kec != null) {
            return String.format(
                    "Kecamatan: %s\nJumlah Desa: %d\nJumlah Kelurahan: %d\nTotal: %d",
                    kec.getNamaKecamatan(),
                    kec.getJumlahDesa(),
                    kec.getJumlahKelurahan(),
                    kec.getTotalWilayahAdministratif());
        }
        return "Data tidak ditemukan";
    }
}