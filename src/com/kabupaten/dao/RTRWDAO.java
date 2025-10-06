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

    // buat koneksi database
    private Connection connection;
    
    public RTRWDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    // end koneksi database


    /**
     * Mengambil semua data RTRW dengan join desa
     */
    public List<RTRW> getAllRTRW() {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT r.*, d.nama_desa FROM rtrw r " +
                    "LEFT JOIN desa d ON r.id_desa = d.id_desa " +
                    "ORDER BY r.rt, r.rw";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                RTRW rtrw = mapResultSetToRTRW(rs);
                rtrwList.add(rtrw);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data RTRW: " + e.getMessage());
        }
        
        return rtrwList;
    }

    // end getAllRTRW
    
    /**
     * Mencari RTRW berdasarkan RT, RW, atau nama ketua
     */
    public List<RTRW> searchRTRW(String keyword) {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT r.*, d.nama_desa FROM rtrw r " +
                    "LEFT JOIN desa d ON r.id_desa = d.id_desa " +
                    "WHERE r.rt LIKE ? OR r.rw LIKE ? OR r.nama_ketua LIKE ? " +
                    "ORDER BY r.rt, r.rw";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RTRW rtrw = mapResultSetToRTRW(rs);
                    rtrwList.add(rtrw);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari RTRW: " + e.getMessage());
        }
        
        return rtrwList;
    }
    // end searchRTRW
    
    /**
     * Menambah RTRW baru
     */
    public boolean addRTRW(RTRW rtrw) {
        String sql = "INSERT INTO rtrw (id_desa, rt, rw, nama_ketua, kontak, alamat) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.println("DEBUG RTRW insert: id_desa=" + rtrw.getIdDesa() +
                            ", rt=" + rtrw.getRt() +
                            ", rw=" + rtrw.getRw() +
                            ", nama_ketua=" + rtrw.getNamaKetua() +
                            ", kontak=" + rtrw.getKontak() +
                            ", alamat=" + rtrw.getAlamat());
            
            stmt.setInt(1, rtrw.getIdDesa());
            stmt.setString(2, rtrw.getRt());
            stmt.setString(3, rtrw.getRw());
            stmt.setString(4, rtrw.getNamaKetua());
            stmt.setString(5, rtrw.getKontak());
            stmt.setString(6, rtrw.getAlamat());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // end addRTRW

    
    /**
     * Mengupdate data RTRW
     */
    public boolean updateRTRW(RTRW rtrw) {
        String sql = "UPDATE rtrw SET id_desa=?, rt=?, rw=?, nama_ketua=?, kontak=?, alamat=? " +
                    "WHERE id_rtrw=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rtrw.getIdDesa());
            stmt.setString(2, rtrw.getRt());
            stmt.setString(3, rtrw.getRw());
            stmt.setString(4, rtrw.getNamaKetua());
            stmt.setString(5, rtrw.getKontak());
            stmt.setString(6, rtrw.getAlamat());
            stmt.setInt(7, rtrw.getIdRtrw());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate RTRW: " + e.getMessage());
            return false;
        }
    }
    // end updateRTRW
    
    /**
     * Menghapus RTRW
     */
    public boolean deleteRTRW(int idRtrw) {
        String sql = "DELETE FROM rtrw WHERE id_rtrw=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRtrw);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error menghapus RTRW: " + e.getMessage());
            return false;
        }
    }
    // end deleteRTRW
    
    /**
     * Mengambil RTRW berdasarkan ID
     */
    public RTRW getRTRWById(int idRtrw) {
        String sql = "SELECT r.*, d.nama_desa FROM rtrw r " +
                    "LEFT JOIN desa d ON r.id_desa = d.id_desa " +
                    "WHERE r.id_rtrw = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idRtrw);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRTRW(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil RTRW by ID: " + e.getMessage());
        }
        
        return null;
    }
    // end getRTRWById
    
    /**
     * Mengambil RTRW berdasarkan ID Desa
     */
    public List<RTRW> getRTRWByDesaId(int idDesa) {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT r.*, d.nama_desa FROM rtrw r " +
                    "LEFT JOIN desa d ON r.id_desa = d.id_desa " +
                    "WHERE r.id_desa = ? " +
                    "ORDER BY r.rt, r.rw";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idDesa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RTRW rtrw = mapResultSetToRTRW(rs);
                    rtrwList.add(rtrw);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil RTRW by Desa ID: " + e.getMessage());
        }
        
        return rtrwList;
    }
    // end getRTRWByDesaId
    
    /**
     * Mengambil RTRW berdasarkan RT dan RW
     */
    public RTRW getRTRWByRTRW(String rt, String rw, int idDesa) {
        String sql = "SELECT r.*, d.nama_desa FROM rtrw r " +
                    "LEFT JOIN desa d ON r.id_desa = d.id_desa " +
                    "WHERE r.rt = ? AND r.rw = ? AND r.id_desa = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, rt);
            stmt.setString(2, rw);
            stmt.setInt(3, idDesa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRTRW(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil RTRW by RT RW: " + e.getMessage());
        }
        
        return null;
    }
    // end getRTRWByRTRW
    
    /**
     * Helper method untuk mapping ResultSet ke object RTRW
     */
    private RTRW mapResultSetToRTRW(ResultSet rs) throws SQLException {
        RTRW rtrw = new RTRW();
        rtrw.setIdRtrw(rs.getInt("id_rtrw"));
        rtrw.setIdDesa(rs.getInt("id_desa"));
        rtrw.setRt(rs.getString("rt"));
        rtrw.setRw(rs.getString("rw"));
        rtrw.setNamaKetua(rs.getString("nama_ketua"));
        rtrw.setKontak(rs.getString("kontak"));
        rtrw.setAlamat(rs.getString("alamat"));
        rtrw.setCreatedAt(rs.getTimestamp("created_at"));
        rtrw.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set nama desa jika ada
        String namaDesa = rs.getString("nama_desa");
        if (namaDesa != null) {
            rtrw.setNamaDesa(namaDesa);
        } else {
            rtrw.setKecamatan("Desa Tidak Ditemukan");
        }
        
        return rtrw;
    }
    // end mapResultSetToRTRW
}