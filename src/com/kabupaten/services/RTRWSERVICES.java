package com.kabupaten.services;

import com.kabupaten.model.RTRWMODEL;
import java.util.ArrayList;
import java.util.List;

public class RTRWSERVICES {
    private List<RTRWMODEL> dataDummy = new ArrayList<>();

    public RTRWMODEL tambahRTRW(String kecamatan, int rw, int rt, String alamat, String status) {
        int newId = dataDummy.size() + 1;
        RTRWMODEL model = new RTRWMODEL(newId, kecamatan, rw, rt, alamat, status);
        dataDummy.add(model);
        return model;
    }

    public List<RTRWMODEL> getAllRTRW() {
        return dataDummy;
    }

    public RTRWMODEL getRTRWById(int id) {
        return dataDummy.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    public RTRWMODEL updateRTRW(int id, String kecamatan, int rw, int rt, String alamat, String status) {
        RTRWMODEL existing = getRTRWById(id);
        if (existing != null) {
            existing.setKecamatan(kecamatan);
            existing.setNomorRW(rw);
            existing.setNomorRT(rt);
            existing.setAlamat(alamat);
            existing.setStatus(status);
        }
        return existing;
    }

    public boolean deleteRTRW(int id) {
        return dataDummy.removeIf(r -> r.getId() == id);
    }

    public List<RTRWMODEL> getRTRWByKecamatan(String kecamatan) {
        List<RTRWMODEL> result = new ArrayList<>();
        for (RTRWMODEL r : dataDummy) {
            if (r.getKecamatan().equalsIgnoreCase(kecamatan)) {
                result.add(r);
            }
        }
        return result;
    }
}
