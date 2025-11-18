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
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Mengambil semua data RTRW
     */
    public List<RTRW> getAllRTRW() {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT DISTINCT * FROM rtrw ORDER BY nama_desa, rt, rw";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RTRW rtrw = mapResultSetToRTRW(rs);
                rtrwList.add(rtrw);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data RTRW: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rtrwList;
    }

    /**
     * Mencari RTRW berdasarkan RT, RW, nama desa, atau nama ketua
     */
    public List<RTRW> searchRTRW(String keyword) {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT DISTINCT * FROM rtrw " +
                    "WHERE nama_desa LIKE ? OR rt LIKE ? OR rw LIKE ? OR nama_ketua LIKE ? " +
                    "ORDER BY nama_desa, rt, rw";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            stmt.setString(4, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RTRW rtrw = mapResultSetToRTRW(rs);
                    rtrwList.add(rtrw);
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
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            System.err.println("Error cek duplikat RTRW: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Menambah RTRW baru dengan pengecekan duplikat
     */
    public boolean addRTRW(RTRW rtrw) {
        // Cek duplikat terlebih dahulu
        if (isDuplicateRTRW(rtrw.getNamaDesa(), rtrw.getRt(), rtrw.getRw(), null)) {
            System.err.println("Data RTRW duplikat: " + rtrw.getNamaDesa() + 
                             " RT " + rtrw.getRt() + " RW " + rtrw.getRw());
            return false;
        }
        
        String sql = "INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, rtrw.getNamaDesa());
            stmt.setString(2, rtrw.getRt());
            stmt.setString(3, rtrw.getRw());
            stmt.setString(4, rtrw.getNamaKetua());
            stmt.setString(5, rtrw.getKontak());
            stmt.setString(6, rtrw.getAlamat());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Ambil ID yang baru dibuat
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rtrw.setIdRtrw(generatedKeys.getInt(1));
                    }
                }
                System.out.println("Data RTRW berhasil ditambahkan dengan ID: " + rtrw.getIdRtrw());
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error menambah RTRW: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengupdate data RTRW dengan pengecekan duplikat
     */
    public boolean updateRTRW(RTRW rtrw) {
        // Cek duplikat terlebih dahulu (exclude ID yang sedang diedit)
        if (isDuplicateRTRW(rtrw.getNamaDesa(), rtrw.getRt(), rtrw.getRw(), rtrw.getIdRtrw())) {
            System.err.println("Data RTRW duplikat: " + rtrw.getNamaDesa() + 
                             " RT " + rtrw.getRt() + " RW " + rtrw.getRw());
            return false;
        }
        
        String sql = "UPDATE rtrw SET nama_desa=?, rt=?, rw=?, nama_ketua=?, kontak=?, alamat=? " +
                    "WHERE id_rtrw=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rtrw.getNamaDesa());
            stmt.setString(2, rtrw.getRt());
            stmt.setString(3, rtrw.getRw());
            stmt.setString(4, rtrw.getNamaKetua());
            stmt.setString(5, rtrw.getKontak());
            stmt.setString(6, rtrw.getAlamat());
            stmt.setInt(7, rtrw.getIdRtrw());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Data RTRW berhasil diupdate. ID: " + rtrw.getIdRtrw());
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate RTRW: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus RTRW
     */
    public boolean deleteRTRW(int idRtrw) {
        String sql = "DELETE FROM rtrw WHERE id_rtrw=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRtrw);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Data RTRW berhasil dihapus. ID: " + idRtrw);
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error menghapus RTRW: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil RTRW berdasarkan ID
     */
    public RTRW getRTRWById(int idRtrw) {
        String sql = "SELECT * FROM rtrw WHERE id_rtrw = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRtrw);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRTRW(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil RTRW by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Mengambil RTRW berdasarkan nama desa
     */
    public List<RTRW> getRTRWByDesaName(String namaDesa) {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT DISTINCT * FROM rtrw WHERE nama_desa = ? ORDER BY rt, rw";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaDesa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RTRW rtrw = mapResultSetToRTRW(rs);
                    rtrwList.add(rtrw);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil RTRW by desa: " + e.getMessage());
            e.printStackTrace();
        }
        
        return rtrwList;
    }

    /**
     * Mengambil daftar nama desa yang unik
     */
    public List<String> getAllDesaNames() {
        List<String> desaList = new ArrayList<>();
        String sql = "SELECT DISTINCT nama_desa FROM rtrw WHERE nama_desa IS NOT NULL ORDER BY nama_desa";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String namaDesa = rs.getString("nama_desa");
                if (namaDesa != null && !namaDesa.isEmpty()) {
                    desaList.add(namaDesa);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil daftar desa: " + e.getMessage());
            e.printStackTrace();
        }
        
        return desaList;
    }

    /**
     * Helper method untuk mapping ResultSet ke object RTRW
     */
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
        
        return rtrw;
    }
    
    /**
     * Mendapatkan statistik RTRW per desa
     */
    public int getCountByDesa(String namaDesa) {
        String sql = "SELECT COUNT(DISTINCT id_rtrw) as total FROM rtrw WHERE nama_desa = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaDesa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung RTRW per desa: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Mendapatkan total jumlah RTRW
     */
    public int getTotalCount() {
        String sql = "SELECT COUNT(DISTINCT id_rtrw) as total FROM rtrw";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total RTRW: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
}