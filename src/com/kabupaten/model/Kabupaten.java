package com.kabupaten.model;

import java.sql.Timestamp;

/**
 * Model untuk data Kabupaten
 * 
 * PERBAIKAN:
 * 1. Nama class diubah dari lowercase "kabupaten" menjadi PascalCase "Kabupaten" (Java naming convention)
 * 2. Menghapus komentar field yang tidak dipakai untuk clean code
 * 3. Menambahkan field 'status' yang umum digunakan
 */
public class Kabupaten {
    private int idKabupaten;
    private int idProvinsi;
    private String kodeKabupaten;
    private String namaKabupaten;
    private String ibukota;
    private int jumlahPenduduk;
    private int jumlahKecamatan;
    private int jumlahDesa;
    private String status; // Tambahan: Aktif/Nonaktif
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public Kabupaten() {}
    
    public Kabupaten(String kodeKabupaten, String namaKabupaten, String ibukota) {
        this.kodeKabupaten = kodeKabupaten;
        this.namaKabupaten = namaKabupaten;
        this.ibukota = ibukota;
    }
    
    // Getters and Setters
    public int getIdKabupaten() {
        return idKabupaten;
    }
    
    public void setIdKabupaten(int idKabupaten) {
        this.idKabupaten = idKabupaten;
    }
    
    public int getIdProvinsi() {
        return idProvinsi;
    }
    
    public void setIdProvinsi(int idProvinsi) {
        this.idProvinsi = idProvinsi;
    }
    
    public String getKodeKabupaten() {
        return kodeKabupaten;
    }
    
    public void setKodeKabupaten(String kodeKabupaten) {
        this.kodeKabupaten = kodeKabupaten;
    }
    
    public String getNamaKabupaten() {
        return namaKabupaten;
    }
    
    public void setNamaKabupaten(String namaKabupaten) {
        this.namaKabupaten = namaKabupaten;
    }
    
    public String getIbukota() {
        return ibukota;
    }
    
    public void setIbukota(String ibukota) {
        this.ibukota = ibukota;
    }
    
    public int getJumlahPenduduk() {
        return jumlahPenduduk;
    }
    
    public void setJumlahPenduduk(int jumlahPenduduk) {
        this.jumlahPenduduk = jumlahPenduduk;
    }
    
    public int getJumlahKecamatan() {
        return jumlahKecamatan;
    }
    
    public void setJumlahKecamatan(int jumlahKecamatan) {
        this.jumlahKecamatan = jumlahKecamatan;
    }
    
    public int getJumlahDesa() {
        return jumlahDesa;
    }
    
    public void setJumlahDesa(int jumlahDesa) {
        this.jumlahDesa = jumlahDesa;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
        return namaKabupaten;
    }
}