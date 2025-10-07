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
        String sql = "SELECT * FROM rtrw ORDER BY rt, rw";
        
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
        String sql = "SELECT * FROM rtrw " +
                    "WHERE rt LIKE ? OR rw LIKE ? OR nama_ketua LIKE ? " +
                    "ORDER BY rt, rw";
        
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
        String sql = "INSERT INTO rtrw (nama_desa, rt, rw, nama_ketua, kontak, alamat) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, rtrw.getNamaDesa());
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
        }
        
        return null;
    }

    // end getRTRWById
    
    /**
     * Mengambil RTRW berdasarkan nama desa
     */
    public List<RTRW> getRTRWByDesaName(String namaDesa) {
        List<RTRW> rtrwList = new ArrayList<>();
        String sql = "SELECT * FROM rtrw WHERE nama_desa = ? ORDER BY rt, rw";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, namaDesa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RTRW rtrw = mapResultSetToRTRW(rs);
                    rtrwList.add(rtrw);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rtrwList;
    }
    // end getRTRWByDesaId

    
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
        
        // Set nama desa jika ada
        String namaDesa = rs.getString("nama_desa");
        if (namaDesa != null) {
            rtrw.setNamaDesa(namaDesa);
        } else {
            rtrw.setNamaDesa("Desa Tidak Ditemukan");
        }
        
        return rtrw;
    }
    // end mapResultSetToRTRW
}