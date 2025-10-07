package com.kabupaten.services;

import com.kabupaten.dao.RTRWDAO;
import com.kabupaten.database.DatabaseConnection;
import com.kabupaten.model.RTRW;
import com.kabupaten.model.RTRWMODEL;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RTRWSERVICES {
    private RTRWDAO rtrwDAO;

    public RTRWSERVICES() {
        this.rtrwDAO = new RTRWDAO();
    }

    /** ===========================
     *  GETTERS
     * =========================== */

    public List<RTRW> getAllRTRW() {
        return rtrwDAO.getAllRTRW();
    }

    public List<RTRWMODEL> getAllRTRWModel() {
        List<RTRW> rtrwList = rtrwDAO.getAllRTRW();
        List<RTRWMODEL> rtrwModelList = new ArrayList<>();
        for (RTRW rtrw : rtrwList) {
            rtrwModelList.add(convertToModel(rtrw));
        }
        return rtrwModelList;
    }

    public List<RTRW> searchRTRW(String keyword) {
        return rtrwDAO.searchRTRW(keyword);
    }

    /** ===========================
     *  INSERT
     * =========================== */

    public boolean addRTRW(String desa, int rw, int rt, String namaKetua, String kontak, String alamat, String status) {
        try {
            RTRW rtrw = new RTRW();
            rtrw.setNamaDesa(desa);
            rtrw.setRw(String.valueOf(rw));
            rtrw.setRt(String.valueOf(rt));
            rtrw.setAlamat(alamat);
            rtrw.setStatus(status);
            rtrw.setDesa(desa);
            rtrw.setNamaKetua(namaKetua);
            rtrw.setKontak(kontak);

            return rtrwDAO.addRTRW(rtrw);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    /** ===========================
     *  UPDATE
     * =========================== */

    public boolean updateRTRW(int id, String desa, int rw, int rt, String alamat, String status) {
        try {
            RTRW rtrw = new RTRW();
            rtrw.setIdRtrw(id);
            rtrw.setIdDesa(getDesaIdByName(desa));
            rtrw.setRw(String.valueOf(rw));
            rtrw.setRt(String.valueOf(rt));
            rtrw.setAlamat(alamat);
            rtrw.setStatus(status);
            rtrw.setDesa(desa);

            return rtrwDAO.updateRTRW(rtrw);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public RTRWMODEL updateRTRWModel(int id, String desa, int rw, int rt, String alamat, String status) {
        boolean success = updateRTRW(id, desa, rw, rt, alamat, status);
        return success ? convertToModel(rtrwDAO.getRTRWById(id)) : null;
    }

    /** ===========================
     *  DELETE
     * =========================== */

    public boolean deleteRTRW(int id) {
        return rtrwDAO.deleteRTRW(id);
    }

    /** ===========================
     *  HELPER
     * =========================== */

    private RTRWMODEL convertToModel(RTRW rtrw) {
        RTRWMODEL model = new RTRWMODEL();
        model.setIdRtrw(rtrw.getIdRtrw());
        model.setIdDesa(rtrw.getIdDesa());
        model.setRw(rtrw.getRw());
        model.setRt(rtrw.getRt());
        model.setAlamat(rtrw.getAlamat());
        model.setNamaKetua(rtrw.getNamaKetua());
        model.setKontak(rtrw.getKontak());
        model.setDesa(rtrw.getDesa());
        model.setStatus(rtrw.getStatus());
        model.setCreatedAt(rtrw.getCreatedAt());
        model.setUpdatedAt(rtrw.getUpdatedAt());
        return model;   
    }

    public int getDesaIdByName(String namaDesa) {
        int id = -1;
        String sql = "SELECT id_desa FROM desa WHERE nama_desa = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, namaDesa);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id_desa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public boolean validateRTRW(String desa, int rw, int rt, String alamat) {
        return !(desa == null || desa.trim().isEmpty() ||
                 rw <= 0 || rt <= 0 ||
                 alamat == null || alamat.trim().isEmpty());
    }
}
