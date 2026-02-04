package com.kabupaten.dao;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.Desa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk operasi CRUD Desa
 */
public class DesaDAO {

    private Connection connection;

    public DesaDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Mengambil semua data Desa
     */
    public List<Desa> getAllDesa() {
        List<Desa> desaList = new ArrayList<>();
        String sql = "SELECT d.*, k.nama_kecamatan " +
                "FROM desa_kelurahan d " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "ORDER BY d.id_desa ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Desa desa = mapResultSetToDesa(rs);
                desaList.add(desa);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data Desa: " + e.getMessage());
            e.printStackTrace();
        }

        return desaList;
    }

    /**
     * Mencari Desa berdasarkan keyword
     */
    public List<Desa> searchDesa(String keyword) {
        List<Desa> desaList = new ArrayList<>();
        String sql = "SELECT d.*, k.nama_kecamatan " +
                "FROM desa_kelurahan d " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "WHERE d.nama_desa LIKE ? OR d.nama_kepala LIKE ? OR d.alamat_kantor LIKE ? " +
                "OR k.nama_kecamatan LIKE ? " +
                "ORDER BY d.id_desa ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Desa desa = mapResultSetToDesa(rs);
                    desaList.add(desa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari Desa: " + e.getMessage());
            e.printStackTrace();
        }

        return desaList;
    }

    /**
     * Mencari Desa berdasarkan id_kecamatan
     */
    public List<Desa> getDesaByKecamatan(int idKecamatan) {
        List<Desa> desaList = new ArrayList<>();
        String sql = "SELECT d.*, k.nama_kecamatan " +
                "FROM desa_kelurahan d " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "WHERE d.id_kecamatan = ? " +
                "ORDER BY d.nama_desa ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKecamatan);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Desa desa = mapResultSetToDesa(rs);
                    desaList.add(desa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil Desa by Kecamatan: " + e.getMessage());
            e.printStackTrace();
        }

        return desaList;
    }

    /**
     * Cek apakah nama desa sudah ada dalam kecamatan yang sama
     */
    public boolean isDuplicateDesa(String namaDesa, int idKecamatan, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM desa_kelurahan WHERE nama_desa = ? AND id_kecamatan = ?";

        if (excludeId != null) {
            sql += " AND id_desa != ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaDesa);
            stmt.setInt(2, idKecamatan);

            if (excludeId != null) {
                stmt.setInt(3, excludeId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cek duplikat Desa: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Menambah Desa baru dengan pengecekan duplikat
     */
    public boolean addDesa(Desa desa) {
        // Cek duplikat terlebih dahulu
        if (isDuplicateDesa(desa.getNamaDesa(), desa.getIdKecamatan(), null)) {
            System.err.println("Data Desa duplikat: " + desa.getNamaDesa() +
                    " di Kecamatan ID: " + desa.getIdKecamatan());
            return false;
        }

        String sql = "INSERT INTO desa_kelurahan (id_kecamatan, nama_desa, jenis, alamat_kantor, " +
                "nama_kepala, alamat_rumah_kepala, no_hp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, desa.getIdKecamatan());
            stmt.setString(2, desa.getNamaDesa());
            stmt.setString(3, desa.getJenis());
            stmt.setString(4, desa.getAlamatKantor());
            stmt.setString(5, desa.getNamaKepala());
            stmt.setString(6, desa.getAlamatRumahKepala());
            stmt.setString(7, desa.getNoHp());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Ambil ID yang baru dibuat
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        desa.setIdDesa(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Data Desa berhasil ditambahkan dengan ID: " + desa.getIdDesa());
                return true;
            }

            return false;
        } catch (SQLException e) {
            System.err.println("Error menambah Desa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengupdate data Desa dengan pengecekan duplikat
     */
    public boolean updateDesa(Desa desa) {
        // Cek duplikat terlebih dahulu (exclude ID yang sedang diedit)
        if (isDuplicateDesa(desa.getNamaDesa(), desa.getIdKecamatan(), desa.getIdDesa())) {
            System.err.println("Data Desa duplikat: " + desa.getNamaDesa());
            return false;
        }

        String sql = "UPDATE desa_kelurahan SET id_kecamatan=?, nama_desa=?, jenis=?, alamat_kantor=?, " +
                "nama_kepala=?, alamat_rumah_kepala=?, no_hp=? WHERE id_desa=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, desa.getIdKecamatan());
            stmt.setString(2, desa.getNamaDesa());
            stmt.setString(3, desa.getJenis());
            stmt.setString(4, desa.getAlamatKantor());
            stmt.setString(5, desa.getNamaKepala());
            stmt.setString(6, desa.getAlamatRumahKepala());
            stmt.setString(7, desa.getNoHp());
            stmt.setInt(8, desa.getIdDesa());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data Desa berhasil diupdate. ID: " + desa.getIdDesa());
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate Desa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus Desa
     */
    public boolean deleteDesa(int idDesa) {
        String sql = "DELETE FROM desa_kelurahan WHERE id_desa=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDesa);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data Desa berhasil dihapus. ID: " + idDesa);
            }

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error menghapus Desa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil Desa berdasarkan ID
     */
    public Desa getDesaById(int idDesa) {
        String sql = "SELECT d.*, k.nama_kecamatan " +
                "FROM desa_kelurahan d " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "WHERE d.id_desa = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDesa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDesa(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil Desa by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mengambil Desa berdasarkan Nama
     */
    public Desa getDesaByName(String namaDesa) {
        String sql = "SELECT d.*, k.nama_kecamatan " +
                "FROM desa_kelurahan d " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "WHERE d.nama_desa = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaDesa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDesa(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil Desa by Name: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mengambil daftar nama desa berdasarkan kecamatan
     */
    public List<String> getDesaNamesByKecamatan(String namaKecamatan) {
        List<String> desaList = new ArrayList<>();
        String sql = "SELECT d.nama_desa FROM desa_kelurahan d " +
                "JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "WHERE k.nama_kecamatan = ? " +
                "ORDER BY d.nama_desa";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaKecamatan);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String namaDesa = rs.getString("nama_desa");
                    if (namaDesa != null && !namaDesa.isEmpty()) {
                        desaList.add(namaDesa);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil daftar nama desa: " + e.getMessage());
            e.printStackTrace();
        }

        return desaList;
    }

    /**
     * Helper method untuk mapping ResultSet ke object Desa
     */
    private Desa mapResultSetToDesa(ResultSet rs) throws SQLException {
        Desa desa = new Desa();
        desa.setIdDesa(rs.getInt("id_desa"));
        desa.setIdKecamatan(rs.getInt("id_kecamatan"));
        desa.setNamaDesa(rs.getString("nama_desa"));
        desa.setJenis(rs.getString("jenis"));
        desa.setAlamatKantor(rs.getString("alamat_kantor"));
        desa.setNamaKepala(rs.getString("nama_kepala"));
        desa.setAlamatRumahKepala(rs.getString("alamat_rumah_kepala"));
        desa.setNoHp(rs.getString("no_hp"));
        desa.setCreatedAt(rs.getTimestamp("created_at"));
        desa.setUpdatedAt(rs.getTimestamp("updated_at"));

        // Set nama kecamatan dari join
        try {
            desa.setNamaKecamatan(rs.getString("nama_kecamatan"));
        } catch (SQLException e) {
            // Kolom tidak ada dalam resultset
            desa.setNamaKecamatan(null);
        }

        return desa;
    }

    /**
     * Mendapatkan total jumlah Desa
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as total FROM desa_kelurahan";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total Desa: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Mendapatkan jumlah desa per kecamatan
     */
    public int getCountByKecamatan(int idKecamatan) {
        String sql = "SELECT COUNT(*) as total FROM desa_kelurahan WHERE id_kecamatan = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKecamatan);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung desa per kecamatan: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}