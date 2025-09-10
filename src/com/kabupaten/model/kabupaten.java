package com.kabupaten.model;

import java.sql.Timestamp;

/**
 * Model untuk data Kabupaten
 */
public class kabupaten {
    private int idKabupaten;
    private int idProvinsi;
    private String kodeKabupaten;
    private String namaKabupaten;
    private String ibukota;
    private double luasWilayah;
    private int jumlahPenduduk;
    private int jumlahKecamatan;
    private int jumlahDesa;
    private String batasUtara;
    private String batasSelatan;
    private String batasTimur;
    private String batasBarat;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Untuk join dengan provinsi
    private String namaProvinsi;
    
    // Constructors
    public kabupaten() {}
    
    public kabupaten(String kodeKabupaten, String namaKabupaten, String ibukota) {
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
    
    public double getLuasWilayah() {
        return luasWilayah;
    }
    
    public void setLuasWilayah(double luasWilayah) {
        this.luasWilayah = luasWilayah;
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
    
    public String getBatasUtara() {
        return batasUtara;
    }
    
    public void setBatasUtara(String batasUtara) {
        this.batasUtara = batasUtara;
    }
    
    public String getBatasSelatan() {
        return batasSelatan;
    }
    
    public void setBatasSelatan(String batasSelatan) {
        this.batasSelatan = batasSelatan;
    }
    
    public String getBatasTimur() {
        return batasTimur;
    }
    
    public void setBatasTimur(String batasTimur) {
        this.batasTimur = batasTimur;
    }
    
    public String getBatasBarat() {
        return batasBarat;
    }
    
    public void setBatasBarat(String batasBarat) {
        this.batasBarat = batasBarat;
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
    
    public String getNamaProvinsi() {
        return namaProvinsi;
    }
    
    public void setNamaProvinsi(String namaProvinsi) {
        this.namaProvinsi = namaProvinsi;
    }
    
    @Override
    public String toString() {
        return namaKabupaten;
    }
}