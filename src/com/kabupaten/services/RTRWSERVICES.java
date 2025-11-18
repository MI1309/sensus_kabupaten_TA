package com.kabupaten.services;

import com.kabupaten.dao.RTRWDAO;
import com.kabupaten.model.RTRW;
import com.kabupaten.model.RTRWMODEL;
import java.util.List;
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

    public RTRW getRTRWById(int id) {
        return rtrwDAO.getRTRWById(id);
    }

    public List<RTRW> getRTRWByDesaName(String namaDesa) {
        return rtrwDAO.getRTRWByDesaName(namaDesa);
    }

    public List<String> getAllDesaNames() {
        return rtrwDAO.getAllDesaNames();
    }

    /** ===========================
     *  INSERT
     * =========================== */

    public boolean addRTRW(String namaDesa, int rw, int rt, String namaKetua, String kontak, String alamat, String status) {
        try {
            // Validasi input
            if (!validateRTRW(namaDesa, rw, rt, alamat)) {
                System.err.println("Validasi gagal: Data tidak lengkap atau tidak valid");
                return false;
            }

            // Format RT dan RW dengan padding 0
            String rtFormatted = String.format("%03d", rt);
            String rwFormatted = String.format("%03d", rw);

            // Cek duplikat sebelum insert
            if (rtrwDAO.isDuplicateRTRW(namaDesa, rtFormatted, rwFormatted, null)) {
                System.err.println("Data duplikat: Desa " + namaDesa + " RT " + rtFormatted + " RW " + rwFormatted + " sudah ada");
                return false;
            }

            // Buat object RTRW
            RTRW rtrw = new RTRW();
            rtrw.setNamaDesa(namaDesa);
            rtrw.setRw(rwFormatted);
            rtrw.setRt(rtFormatted);
            rtrw.setNamaKetua(namaKetua != null && !namaKetua.trim().isEmpty() ? namaKetua : null);
            rtrw.setKontak(kontak != null && !kontak.trim().isEmpty() ? kontak : null);
            rtrw.setAlamat(alamat);

            return rtrwDAO.addRTRW(rtrw);
        } catch (Exception e) {
            System.err.println("Error di addRTRW service: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /** ===========================
     *  UPDATE
     * =========================== */

    public boolean updateRTRW(int id, String namaDesa, int rw, int rt, String alamat, String status) {
        try {
            // Validasi input
            if (!validateRTRW(namaDesa, rw, rt, alamat)) {
                System.err.println("Validasi gagal: Data tidak lengkap atau tidak valid");
                return false;
            }

            // Format RT dan RW dengan padding 0
            String rtFormatted = String.format("%03d", rt);
            String rwFormatted = String.format("%03d", rw);

            // Cek duplikat sebelum update (exclude ID yang sedang diedit)
            if (rtrwDAO.isDuplicateRTRW(namaDesa, rtFormatted, rwFormatted, id)) {
                System.err.println("Data duplikat: Desa " + namaDesa + " RT " + rtFormatted + " RW " + rwFormatted + " sudah ada");
                return false;
            }

            // Ambil data existing untuk preserve field lain
            RTRW existing = rtrwDAO.getRTRWById(id);
            if (existing == null) {
                System.err.println("Data dengan ID " + id + " tidak ditemukan");
                return false;
            }

            // Update object RTRW
            existing.setIdRtrw(id);
            existing.setNamaDesa(namaDesa);
            existing.setRw(rwFormatted);
            existing.setRt(rtFormatted);
            existing.setAlamat(alamat);
            // Preserve nama ketua dan kontak jika tidak diubah
            // existing.setNamaKetua() dan existing.setKontak() tetap menggunakan nilai lama

            return rtrwDAO.updateRTRW(existing);
        } catch (Exception e) {
            System.err.println("Error di updateRTRW service: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRTRWFull(int id, String namaDesa, int rw, int rt, String namaKetua, String kontak, String alamat) {
        try {
            // Validasi input
            if (!validateRTRW(namaDesa, rw, rt, alamat)) {
                System.err.println("Validasi gagal: Data tidak lengkap atau tidak valid");
                return false;
            }

            // Format RT dan RW dengan padding 0
            String rtFormatted = String.format("%03d", rt);
            String rwFormatted = String.format("%03d", rw);

            // Cek duplikat sebelum update (exclude ID yang sedang diedit)
            if (rtrwDAO.isDuplicateRTRW(namaDesa, rtFormatted, rwFormatted, id)) {
                System.err.println("Data duplikat: Desa " + namaDesa + " RT " + rtFormatted + " RW " + rwFormatted + " sudah ada");
                return false;
            }

            // Update object RTRW lengkap
            RTRW rtrw = new RTRW();
            rtrw.setIdRtrw(id);
            rtrw.setNamaDesa(namaDesa);
            rtrw.setRw(rwFormatted);
            rtrw.setRt(rtFormatted);
            rtrw.setNamaKetua(namaKetua != null && !namaKetua.trim().isEmpty() ? namaKetua : null);
            rtrw.setKontak(kontak != null && !kontak.trim().isEmpty() ? kontak : null);
            rtrw.setAlamat(alamat);

            return rtrwDAO.updateRTRW(rtrw);
        } catch (Exception e) {
            System.err.println("Error di updateRTRWFull service: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public RTRWMODEL updateRTRWModel(int id, String namaDesa, int rw, int rt, String alamat, String status) {
        boolean success = updateRTRW(id, namaDesa, rw, rt, alamat, status);
        return success ? convertToModel(rtrwDAO.getRTRWById(id)) : null;
    }

    /** ===========================
     *  DELETE
     * =========================== */

    public boolean deleteRTRW(int id) {
        try {
            RTRW rtrw = rtrwDAO.getRTRWById(id);
            if (rtrw == null) {
                System.err.println("Data dengan ID " + id + " tidak ditemukan");
                return false;
            }
            
            System.out.println("Menghapus RTRW: " + rtrw.getNamaDesa() + " RT " + rtrw.getRt() + " RW " + rtrw.getRw());
            return rtrwDAO.deleteRTRW(id);
        } catch (Exception e) {
            System.err.println("Error di deleteRTRW service: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /** ===========================
     *  STATISTICS
     * =========================== */

    public int getCountByDesa(String namaDesa) {
        return rtrwDAO.getCountByDesa(namaDesa);
    }

    public int getTotalCount() {
        return rtrwDAO.getTotalCount();
    }

    /** ===========================
     *  VALIDATION
     * =========================== */

    public boolean validateRTRW(String namaDesa, int rw, int rt, String alamat) {
        if (namaDesa == null || namaDesa.trim().isEmpty()) {
            System.err.println("Validasi gagal: Nama desa kosong");
            return false;
        }
        if (rw <= 0) {
            System.err.println("Validasi gagal: RW harus lebih dari 0");
            return false;
        }
        if (rt <= 0) {
            System.err.println("Validasi gagal: RT harus lebih dari 0");
            return false;
        }
        if (alamat == null || alamat.trim().isEmpty()) {
            System.err.println("Validasi gagal: Alamat kosong");
            return false;
        }
        return true;
    }

    public boolean isDuplicateRTRW(String namaDesa, int rw, int rt, Integer excludeId) {
        String rtFormatted = String.format("%03d", rt);
        String rwFormatted = String.format("%03d", rw);
        return rtrwDAO.isDuplicateRTRW(namaDesa, rtFormatted, rwFormatted, excludeId);
    }

    /** ===========================
     *  HELPER / CONVERTER
     * =========================== */

    private RTRWMODEL convertToModel(RTRW rtrw) {
        if (rtrw == null) {
            return null;
        }
        
        RTRWMODEL model = new RTRWMODEL();
        model.setIdRtrw(rtrw.getIdRtrw());
        model.setRw(rtrw.getRw());
        model.setRt(rtrw.getRt());
        model.setAlamat(rtrw.getAlamat());
        model.setNamaKetua(rtrw.getNamaKetua());
        model.setKontak(rtrw.getKontak());
        model.setCreatedAt(rtrw.getCreatedAt());
        model.setUpdatedAt(rtrw.getUpdatedAt());
        
        // Set nama desa langsung tanpa relasi
        model.setDesa(rtrw.getNamaDesa());
        
        return model;   
    }
}