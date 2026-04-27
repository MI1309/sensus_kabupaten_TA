package com.kabupaten.dao;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.RTRW;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk operasi CRUD RTRW
 */
public class RTRWDAO {

    private Connection connection;
    
    public RTRWDAO() {
        // Inisialisasi awal, tapi query akan selalu memastikan koneksi valid
        this.connection = DatabaseConnection.getConnection();
    }

    private Connection ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getConnection();
        }
        return connection;
    }

    /**
     * Mengambil semua data RTRW
     */
    public List<RTRW> getAllRTRW() {
        List<RTRW> rtrwList = new ArrayList<>();
        // PERBAIKAN: Hapus DISTINCT karena tidak kompatibel dengan kolom TEXT (alamat)
        String sql = "SELECT r.*, k.nama_kecamatan " +
                     "FROM rtrw r " +
                     "LEFT JOIN desa_kelurahan d ON r.nama_desa COLLATE utf8mb4_general_ci = d.nama_desa COLLATE utf8mb4_general_ci " +
                     "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                     "ORDER BY r.nama_desa, r.rt, r.rw";
        
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    rtrwList.add(mapResultSetToRTRW(rs));
                }
                System.out.println("[DIAGNOSTIC] RTRWDAO.getAllRTRW: Found " + rtrwList.size() + " records.");
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data RTRW: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rtrwList;
    }

    /**
     * Mencari RTRW berdasarkan keyword
     */
    public List<RTRW> searchRTRW(String keyword) {
        List<RTRW> rtrwList = new ArrayList<>();
        // PERBAIKAN: Hapus DISTINCT
        String sql = "SELECT r.*, k.nama_kecamatan " +
                     "FROM rtrw r " +
                     "LEFT JOIN desa_kelurahan d ON r.nama_desa COLLATE utf8mb4_general_ci = d.nama_desa COLLATE utf8mb4_general_ci " +
                     "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                     "WHERE (r.nama_desa COLLATE utf8mb4_general_ci LIKE ? OR r.nama_ketua COLLATE utf8mb4_general_ci LIKE ? OR r.alamat COLLATE utf8mb4_general_ci LIKE ? OR k.nama_kecamatan COLLATE utf8mb4_general_ci LIKE ?) " +
                     "ORDER BY r.nama_desa, r.rt, r.rw";
        
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                String searchPattern = "%" + keyword + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                stmt.setString(4, searchPattern);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        rtrwList.add(mapResultSetToRTRW(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari RTRW: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rtrwList;
    }

    /**
     * Cek apakah kombinasi Desa-RT-RW sudah ada
     */
    public boolean isDuplicateRTRW(String namaDesa, String rt, String rw, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM rtrw WHERE nama_desa = ? AND rt = ? AND rw = ?";
        if (excludeId != null) {
            sql += " AND id_rtrw != ?";
        }
        
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, namaDesa);
                stmt.setString(2, rt);
                stmt.setString(3, rw);
                if (excludeId != null) {
                    stmt.setInt(4, excludeId);
                }
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error cek duplikat RTRW: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Menambah RTRW baru
     */
    public boolean addRTRW(RTRW rtrw) {
        if (isDuplicateRTRW(rtrw.getNamaDesa(), rtrw.getRt(), rtrw.getRw(), null)) {
            return false;
        }
        
        String sql = "INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, rtrw.getNamaDesa());
                stmt.setString(2, rtrw.getRt());
                stmt.setString(3, rtrw.getRw());
                stmt.setString(4, rtrw.getNamaKetua());
                stmt.setString(5, rtrw.getKontak());
                stmt.setString(6, rtrw.getAlamat());
                
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            rtrw.setIdRtrw(generatedKeys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Memperbarui data RTRW
     */
    public boolean updateRTRW(RTRW rtrw) {
        if (isDuplicateRTRW(rtrw.getNamaDesa(), rtrw.getRt(), rtrw.getRw(), rtrw.getIdRtrw())) {
            return false;
        }
        
        String sql = "UPDATE rtrw SET nama_desa=?, rt=?, rw=?, nama_ketua=?, kontak=?, alamat=? WHERE id_rtrw=?";
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, rtrw.getNamaDesa());
                stmt.setString(2, rtrw.getRt());
                stmt.setString(3, rtrw.getRw());
                stmt.setString(4, rtrw.getNamaKetua());
                stmt.setString(5, rtrw.getKontak());
                stmt.setString(6, rtrw.getAlamat());
                stmt.setInt(7, rtrw.getIdRtrw());
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Menghapus data RTRW
     */
    public boolean deleteRTRW(int idRtrw) {
        String sql = "DELETE FROM rtrw WHERE id_rtrw=?";
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idRtrw);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mendapatkan RTRW berdasarkan ID
     */
    public RTRW getRTRWById(int idRtrw) {
        String sql = "SELECT r.*, k.nama_kecamatan FROM rtrw r " +
                     "LEFT JOIN desa_kelurahan d ON r.nama_desa COLLATE utf8mb4_general_ci = d.nama_desa COLLATE utf8mb4_general_ci " +
                     "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                     "WHERE r.id_rtrw = ?";
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idRtrw);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToRTRW(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mendapatkan daftar RTRW berdasarkan nama desa
     */
    public List<RTRW> getRTRWByDesaName(String namaDesa) {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT r.*, k.nama_kecamatan FROM rtrw r " +
                     "LEFT JOIN desa_kelurahan d ON r.nama_desa COLLATE utf8mb4_general_ci = d.nama_desa COLLATE utf8mb4_general_ci " +
                     "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                     "WHERE r.nama_desa = ? ORDER BY r.rt, r.rw";
        try {
            Connection conn = ensureConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, namaDesa);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        rtrwList.add(mapResultSetToRTRW(rs));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rtrwList;
    }

    /**
     * Mendapatkan total count
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as total FROM rtrw";
        try {
            Connection conn = ensureConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private RTRW mapResultSetToRTRW(ResultSet rs) throws SQLException {
        RTRW rtrw = new RTRW();
        rtrw.setIdRtrw(rs.getInt("id_rtrw"));
        rtrw.setNamaDesa(rs.getString("nama_desa"));
        rtrw.setRt(rs.getString("rt"));
        rtrw.setRw(rs.getString("rw"));
        rtrw.setNamaKetua(rs.getString("nama_ketua"));
        rtrw.setKontak(rs.getString("kontak"));
        rtrw.setAlamat(rs.getString("alamat"));
        rtrw.setCreatedAt(rs.getTimestamp("created_at"));
        rtrw.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        try {
            // Field dari JOIN
            String kecamatan = rs.getString("nama_kecamatan");
            if (kecamatan != null) {
                rtrw.setKecamatan(kecamatan);
            }
        } catch (SQLException e) {
            // Ignore if column not found
        }
        
        return rtrw;
    }
}