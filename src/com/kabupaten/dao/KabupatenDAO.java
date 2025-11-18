package com.kabupaten.dao;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.Kabupaten;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk operasi CRUD Kabupaten
 */
public class KabupatenDAO {
    private Connection connection;
    
    public KabupatenDAO() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    /**
     * Mengambil semua data kabupaten dengan join provinsi
     */
    public List<Kabupaten> getAllKabupaten() {
        List<Kabupaten> kabupatenList = new ArrayList<>();
        String sql = "SELECT k.*, p.nama_provinsi FROM kabupaten k " +
                    "JOIN provinsi p ON k.id_provinsi = p.id_provinsi " +
                    "ORDER BY k.nama_kabupaten";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Kabupaten kabupaten = new Kabupaten();
                kabupaten.setIdKabupaten(rs.getInt("id_kabupaten"));
                kabupaten.setIdProvinsi(rs.getInt("id_provinsi"));
                kabupaten.setKodeKabupaten(rs.getString("kode_kabupaten"));
                kabupaten.setNamaKabupaten(rs.getString("nama_kabupaten"));
                kabupaten.setIbukota(rs.getString("ibukota"));
                // kabupaten.setLuasWilayah(rs.getDouble("luas_wilayah"));
                kabupaten.setJumlahPenduduk(rs.getInt("jumlah_penduduk"));
                kabupaten.setJumlahKecamatan(rs.getInt("jumlah_kecamatan"));
                kabupaten.setJumlahDesa(rs.getInt("jumlah_desa"));
                // kabupaten.setBatasUtara(rs.getString("batas_utara"));
                // kabupaten.setBatasSelatan(rs.getString("batas_selatan"));
                // kabupaten.setBatasTimur(rs.getString("batas_timur"));
                // kabupaten.setBatasBarat(rs.getString("batas_barat"));
                kabupaten.setCreatedAt(rs.getTimestamp("created_at"));
                kabupaten.setUpdatedAt(rs.getTimestamp("updated_at"));
                // kabupaten.setNamaProvinsi(rs.getString("nama_provinsi"));
                
                kabupatenList.add(kabupaten);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data kabupaten: " + e.getMessage());
        }
        
        return kabupatenList;
    }
    
    /**
     * Mencari kabupaten berdasarkan nama
     */
    public List<Kabupaten> searchKabupaten(String keyword) {
        List<Kabupaten> kabupatenList = new ArrayList<>();
        String sql = "SELECT k.*, p.nama_provinsi FROM kabupaten k " +
                    "JOIN provinsi p ON k.id_provinsi = p.id_provinsi " +
                    "WHERE k.nama_kabupaten LIKE ? OR k.kode_kabupaten LIKE ? " +
                    "ORDER BY k.nama_kabupaten";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Kabupaten kabupaten = mapResultSetToKabupaten(rs);
                    kabupatenList.add(kabupaten);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari kabupaten: " + e.getMessage());
        }
        
        return kabupatenList;
    }
    
    /**
     * Menambah kabupaten baru
     */
    public boolean addKabupaten(Kabupaten kabupaten) {
        String sql = "INSERT INTO kabupaten (id_provinsi, kode_kabupaten, nama_kabupaten, " +
                    "ibukota, luas_wilayah, jumlah_penduduk, jumlah_kecamatan, jumlah_desa, " +
                    ") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // stmt.setInt(1, kabupaten.getIdProvinsi());
            stmt.setString(2, kabupaten.getKodeKabupaten());
            stmt.setString(3, kabupaten.getNamaKabupaten());
            stmt.setString(4, kabupaten.getIbukota());
            // stmt.setDouble(5, kabupaten.getLuasWilayah());
            stmt.setInt(6, kabupaten.getJumlahPenduduk());
            stmt.setInt(7, kabupaten.getJumlahKecamatan());
            stmt.setInt(8, kabupaten.getJumlahDesa());
            // stmt.setString(9, kabupaten.getBatasUtara());
            // stmt.setString(10, kabupaten.getBatasSelatan());
            // stmt.setString(11, kabupaten.getBatasTimur());
            // stmt.setString(12, kabupaten.getBatasBarat());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error menambah kabupaten: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mengupdate data kabupaten
     */
    public boolean updateKabupaten(Kabupaten kabupaten) {
        String sql = "UPDATE kabupaten SET id_provinsi=?, kode_kabupaten=?, nama_kabupaten=?, " +
                    "ibukota=?, luas_wilayah=?, jumlah_penduduk=?, jumlah_kecamatan=?, jumlah_desa=?, " +
                    "WHERE id_kabupaten=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // stmt.setInt(1, kabupaten.getIdProvinsi());
            stmt.setString(2, kabupaten.getKodeKabupaten());
            stmt.setString(3, kabupaten.getNamaKabupaten());
            stmt.setString(4, kabupaten.getIbukota());
            // stmt.setDouble(5, kabupaten.getLuasWilayah());
            stmt.setInt(6, kabupaten.getJumlahPenduduk());
            stmt.setInt(7, kabupaten.getJumlahKecamatan());
            stmt.setInt(8, kabupaten.getJumlahDesa());
            // stmt.setString(9, kabupaten.getBatasUtara());
            // stmt.setString(10, kabupaten.getBatasSelatan());
            // stmt.setString(11, kabupaten.getBatasTimur());
            // stmt.setString(12, kabupaten.getBatasBarat());
            stmt.setInt(13, kabupaten.getIdKabupaten());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate kabupaten: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Menghapus kabupaten
     */
    public boolean deleteKabupaten(int idKabupaten) {
        String sql = "DELETE FROM kabupaten WHERE id_kabupaten=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKabupaten);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error menghapus kabupaten: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mengambil kabupaten berdasarkan ID
     */
    public Kabupaten getKabupatenById(int idKabupaten) {
        String sql = "SELECT k.*, p.nama_provinsi FROM kabupaten k " +
                    "JOIN provinsi p ON k.id_provinsi = p.id_provinsi " +
                    "WHERE k.id_kabupaten = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idKabupaten);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKabupaten(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil kabupaten by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Helper method untuk mapping ResultSet ke object Kabupaten
     */
    private Kabupaten mapResultSetToKabupaten(ResultSet rs) throws SQLException {
        Kabupaten kabupaten = new Kabupaten();
        kabupaten.setIdKabupaten(rs.getInt("id_kabupaten"));
        // kabupaten.setIdProvinsi(rs.getInt("id_provinsi"));
        kabupaten.setKodeKabupaten(rs.getString("kode_kabupaten"));
        kabupaten.setNamaKabupaten(rs.getString("nama_kabupaten"));
        kabupaten.setIbukota(rs.getString("ibukota"));
        // kabupaten.setLuasWilayah(rs.getDouble("luas_wilayah"));
        kabupaten.setJumlahPenduduk(rs.getInt("jumlah_penduduk"));
        kabupaten.setJumlahKecamatan(rs.getInt("jumlah_kecamatan"));
        kabupaten.setJumlahDesa(rs.getInt("jumlah_desa"));
        // kabupaten.setBatasUtara(rs.getString("batas_utara"));
        // kabupaten.setBatasSelatan(rs.getString("batas_selatan"));
        // kabupaten.setBatasTimur(rs.getString("batas_timur"));
        // kabupaten.setBatasBarat(rs.getString("batas_barat"));
        kabupaten.setCreatedAt(rs.getTimestamp("created_at"));
        kabupaten.setUpdatedAt(rs.getTimestamp("updated_at"));
        // kabupaten.setNamaProvinsi(rs.getString("nama_provinsi"));
        return kabupaten;
    }
}