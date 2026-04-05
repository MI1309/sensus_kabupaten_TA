package com.kabupaten.dao;

import com.kabupaten.model.Fasilitas;
import com.kabupaten.database.DatabaseConnection; // ← sesuaikan dengan nama class koneksi-mu
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FasilitasDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection(); // ← sesuaikan method koneksi-mu
    }

    // READ ALL
    public List<Fasilitas> getAllFasilitas() {
        List<Fasilitas> list = new ArrayList<>();
        String sql = "SELECT * FROM fasilitas ORDER BY id_fasilitas DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // READ BY ID
    public Fasilitas getFasilitasById(int id) {
        String sql = "SELECT * FROM fasilitas WHERE id_fasilitas = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // CREATE
    public boolean addFasilitas(Fasilitas f) {
        String sql = "INSERT INTO fasilitas (nama_fasilitas, jenis, dinas_terkait, alamat, keterangan) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNamaFasilitas());
            ps.setString(2, f.getJenis());
            ps.setString(3, f.getDinasTerkait());
            ps.setString(4, f.getAlamat());
            ps.setString(5, f.getKeterangan());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // UPDATE
    public boolean updateFasilitas(Fasilitas f) {
        String sql = "UPDATE fasilitas SET nama_fasilitas=?, jenis=?, dinas_terkait=?, alamat=?, keterangan=? WHERE id_fasilitas=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNamaFasilitas());
            ps.setString(2, f.getJenis());
            ps.setString(3, f.getDinasTerkait());
            ps.setString(4, f.getAlamat());
            ps.setString(5, f.getKeterangan());
            ps.setInt(6, f.getIdFasilitas());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public boolean deleteFasilitas(int id) {
        String sql = "DELETE FROM fasilitas WHERE id_fasilitas = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // HELPER: mapping ResultSet → Fasilitas object
    private Fasilitas mapResultSet(ResultSet rs) throws SQLException {
        Fasilitas f = new Fasilitas();
        f.setIdFasilitas(rs.getInt("id_fasilitas"));
        f.setNamaFasilitas(rs.getString("nama_fasilitas"));
        f.setJenis(rs.getString("jenis"));
        f.setDinasTerkait(rs.getString("dinas_terkait"));
        f.setAlamat(rs.getString("alamat"));
        f.setKeterangan(rs.getString("keterangan"));
        f.setCreatedAt(rs.getString("created_at"));
        f.setUpdatedAt(rs.getString("updated_at"));
        return f;
    }
}