package com.kabupaten.dao;

import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.Warga;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WargaDAO {
    private Connection connection;

    public WargaDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<Warga> getAllWarga() {
        List<Warga> list = new ArrayList<>();
        String sql = "SELECT w.*, d.nama_desa, k.nama_kecamatan " +
                "FROM warga w " +
                "LEFT JOIN desa_kelurahan d ON w.id_desa = d.id_desa " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "ORDER BY w.id_warga DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToWarga(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Warga> searchWarga(String keyword) {
        List<Warga> list = new ArrayList<>();
        String sql = "SELECT w.*, d.nama_desa, k.nama_kecamatan " +
                "FROM warga w " +
                "LEFT JOIN desa_kelurahan d ON w.id_desa = d.id_desa " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "WHERE w.nik LIKE ? OR w.nama_lengkap LIKE ? OR w.alamat LIKE ? OR d.nama_desa LIKE ? OR k.nama_kecamatan LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            stmt.setString(1, k);
            stmt.setString(2, k);
            stmt.setString(3, k);
            stmt.setString(4, k);
            stmt.setString(5, k);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToWarga(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addWarga(Warga w) {
        String sql = "INSERT INTO warga (nik, nama_lengkap, jenis_kelamin, tempat_lahir, tanggal_lahir, alamat, id_desa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, w.getNik());
            stmt.setString(2, w.getNamaLengkap());
            stmt.setString(3, w.getJenisKelamin());
            stmt.setString(4, w.getTempatLahir());
            stmt.setDate(5, w.getTanggalLahir());
            stmt.setString(6, w.getAlamat());
            stmt.setInt(7, w.getIdDesa());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateWarga(Warga w) {
        String sql = "UPDATE warga SET nik=?, nama_lengkap=?, jenis_kelamin=?, tempat_lahir=?, tanggal_lahir=?, alamat=?, id_desa=? WHERE id_warga=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, w.getNik());
            stmt.setString(2, w.getNamaLengkap());
            stmt.setString(3, w.getJenisKelamin());
            stmt.setString(4, w.getTempatLahir());
            stmt.setDate(5, w.getTanggalLahir());
            stmt.setString(6, w.getAlamat());
            stmt.setInt(7, w.getIdDesa());
            stmt.setInt(8, w.getIdWarga());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteWarga(int id) {
        String sql = "DELETE FROM warga WHERE id_warga=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Warga getWargaById(int id) {
        String sql = "SELECT w.*, d.nama_desa, k.nama_kecamatan " +
                "FROM warga w " +
                "LEFT JOIN desa_kelurahan d ON w.id_desa = d.id_desa " +
                "LEFT JOIN kecamatan k ON d.id_kecamatan = k.id_kecamatan " +
                "WHERE w.id_warga=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToWarga(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Warga mapResultSetToWarga(ResultSet rs) throws SQLException {
        Warga w = new Warga();
        w.setIdWarga(rs.getInt("id_warga"));
        w.setNik(rs.getString("nik"));
        w.setNamaLengkap(rs.getString("nama_lengkap"));
        w.setJenisKelamin(rs.getString("jenis_kelamin"));
        w.setTempatLahir(rs.getString("tempat_lahir"));
        w.setTanggalLahir(rs.getDate("tanggal_lahir"));
        w.setAlamat(rs.getString("alamat"));
        w.setIdDesa(rs.getInt("id_desa"));
        w.setNamaDesa(rs.getString("nama_desa"));
        w.setNamaKecamatan(rs.getString("nama_kecamatan"));
        w.setCreatedAt(rs.getTimestamp("created_at"));
        w.setUpdatedAt(rs.getTimestamp("updated_at"));
        return w;
    }

    public int getTotalCount() {
        String sql = "SELECT COUNT(*) as total FROM warga";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error menghitung total Warga: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
