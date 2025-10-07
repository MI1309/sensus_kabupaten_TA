package com.kabupaten.model;

import java.sql.Timestamp;

import javax.print.DocFlavor.STRING;

/**
 * Alias untuk class RTRW untuk kompatibilitas dengan kode existing
 * Ini adalah wrapper/alias untuk memudahkan migrasi
 */
public class RTRWMODEL {
    private int id;
    private int idDesa;
    private String rt;
    private String rw;
    private String namaKetua;
    private String kontak;
    private String desa;
    private String alamat;
    private String status;
    private String namaDesa;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // desa function
    public String getDesa() {
        return desa;
    }

    public void setDesa(String desa) {
        this.desa = desa;
    }


    // Default constructor
    public RTRWMODEL() {
    }
    
    // Constructor dengan parameter
    public RTRWMODEL(int idDesa, String rt, String rw, String namaKetua, String kontak, String alamat) {
        this.idDesa = idDesa;
        this.rt = rt;
        this.rw = rw;
        this.namaKetua = namaKetua;
        this.kontak = kontak;
        this.alamat = alamat;
    }
    
    // Getter dan Setter methods
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdRtrw() {
        return id;
    }
    
    public void setIdRtrw(int id) {
        this.id = id;
    }
    
    public int getIdDesa() {
        return idDesa;
    }
    
    public void setIdDesa(int idDesa) {
        this.idDesa = idDesa;
    }
    
    public String getRt() {
        return rt;
    }
    
    public void setRt(String rt) {
        this.rt = rt;
    }
    
    public String getRw() {
        return rw;
    }
    
    public void setRw(String rw) {
        this.rw = rw;
    }
    
    public int getNomorRT() {
        try {
            return Integer.parseInt(rt != null ? rt : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void setNomorRT(int nomorRT) {
        this.rt = String.valueOf(nomorRT);
    }
    
    public int getNomorRW() {
        try {
            return Integer.parseInt(rw != null ? rw : "0");
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public void setNomorRW(int nomorRW) {
        this.rw = String.valueOf(nomorRW);
    }
    
    public String getNamaKetua() {
        return namaKetua;
    }
    
    public void setNamaKetua(String namaKetua) {
        this.namaKetua = namaKetua;
    }
    
    public String getKontak() {
        return kontak;
    }
    
    public void setKontak(String kontak) {
        this.kontak = kontak;
    }
    
    public String getAlamat() {
        return alamat;
    }
    
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNamaDesa() {
        return namaDesa;
    }
    
    public void setNamaDesa(String namaDesa) {
        this.namaDesa = namaDesa;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "RTRWMODEL{" +
                "id=" + id +
                ", idDesa=" + idDesa +
                ", rt='" + rt + '\'' +
                ", rw='" + rw + '\'' +
                ", namaKetua='" + namaKetua + '\'' +
                ", kontak='" + kontak + '\'' +
                ", alamat='" + alamat + '\'' +
                // ", kecamatan='" + kecamatan + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}