package com.kabupaten.model;

import java.sql.Timestamp;

/**
 * Model class untuk tabel RTRW (alias RTRWMODEL untuk kompatibilitas)
 */
public class RTRW {
    private int idRtrw;
    private int idDesa;
    private String rt;
    private String rw;
    private String namaKetua;
    private String kontak;
    private String alamat;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Field tambahan untuk join dengan tabel desa dan kompatibilitas dengan view
    private String namaDesa;
    private String kecamatan; // alias untuk nama desa
    private String status; // field tambahan jika diperlukan
    
    // Default constructor
    public RTRW() {
    }
    
    // Constructor dengan parameter
    public RTRW(int idDesa, String rt, String rw, String namaKetua, String kontak, String alamat) {
        this.idDesa = idDesa;
        this.rt = rt;
        this.rw = rw;
        this.namaKetua = namaKetua;
        this.kontak = kontak;
        this.alamat = alamat;
    }
    
    // Getter dan Setter methods
    public int getIdRtrw() {
        return idRtrw;
    }
    
    public void setIdRtrw(int idRtrw) {
        this.idRtrw = idRtrw;
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
    
    public String getNamaDesa() {
        return namaDesa;
    }
    
    public void setNamaDesa(String namaDesa) {
        this.namaDesa = namaDesa;
        this.kecamatan = namaDesa; // set kecamatan sebagai alias
    }
    
    public String getKecamatan() {
        return kecamatan != null ? kecamatan : namaDesa;
    }
    
    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // Method alias untuk kompatibilitas dengan view
    public int getId() {
        return idRtrw;
    }
    
    public void setId(int id) {
        this.idRtrw = id;
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
    
    // Method untuk menampilkan informasi RTRW
    @Override
    public String toString() {
        return "RTRW{" +
                "idRtrw=" + idRtrw +
                ", idDesa=" + idDesa +
                ", rt='" + rt + '\'' +
                ", rw='" + rw + '\'' +
                ", namaKetua='" + namaKetua + '\'' +
                ", kontak='" + kontak + '\'' +
                ", alamat='" + alamat + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", namaDesa='" + namaDesa + '\'' +
                '}';
    }
    
    // Method untuk mendapatkan format RT/RW
    public String getRtRwFormat() {
        return "RT " + rt + " / RW " + rw;
    }
}