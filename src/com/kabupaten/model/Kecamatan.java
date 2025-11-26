package com.kabupaten.model;

import java.sql.Timestamp;

/**
 * Model class untuk tabel Kecamatan
 */
public class Kecamatan {
    private int idKecamatan;
    private String namaKecamatan;
    private String alamatKantor;
    private String namaKepala;
    private String alamatRumahKepala;
    private int jumlahPenduduk;
    private String noHp;
    private int jumlahDesa;
    private int jumlahKelurahan;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public Kecamatan() {
    }
    
    // Constructor dengan parameter lengkap
    public Kecamatan(int idKecamatan, String namaKecamatan, String alamatKantor, 
                     String namaKepala, String alamatRumahKepala, int jumlahPenduduk,
                     String noHp, int jumlahDesa, int jumlahKelurahan) {
        this.idKecamatan = idKecamatan;
        this.namaKecamatan = namaKecamatan;
        this.alamatKantor = alamatKantor;
        this.namaKepala = namaKepala;
        this.alamatRumahKepala = alamatRumahKepala;
        this.jumlahPenduduk = jumlahPenduduk;
        this.noHp = noHp;
        this.jumlahDesa = jumlahDesa;
        this.jumlahKelurahan = jumlahKelurahan;
    }
    
    // Constructor tanpa ID (untuk insert)
    public Kecamatan(String namaKecamatan, String alamatKantor, String namaKepala, 
                     String alamatRumahKepala, int jumlahPenduduk, String noHp,
                     int jumlahDesa, int jumlahKelurahan) {
        this.namaKecamatan = namaKecamatan;
        this.alamatKantor = alamatKantor;
        this.namaKepala = namaKepala;
        this.alamatRumahKepala = alamatRumahKepala;
        this.jumlahPenduduk = jumlahPenduduk;
        this.noHp = noHp;
        this.jumlahDesa = jumlahDesa;
        this.jumlahKelurahan = jumlahKelurahan;
    }

    // Getter dan Setter methods
    public int getIdKecamatan() {
        return idKecamatan;
    }

    public void setIdKecamatan(int idKecamatan) {
        this.idKecamatan = idKecamatan;
    }

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public void setNamaKecamatan(String namaKecamatan) {
        this.namaKecamatan = namaKecamatan;
    }

    public String getAlamatKantor() {
        return alamatKantor;
    }

    public void setAlamatKantor(String alamatKantor) {
        this.alamatKantor = alamatKantor;
    }

    public String getNamaKepala() {
        return namaKepala;
    }

    public void setNamaKepala(String namaKepala) {
        this.namaKepala = namaKepala;
    }

    public String getAlamatRumahKepala() {
        return alamatRumahKepala;
    }

    public void setAlamatRumahKepala(String alamatRumahKepala) {
        this.alamatRumahKepala = alamatRumahKepala;
    }

    public int getJumlahPenduduk() {
        return jumlahPenduduk;
    }

    public void setJumlahPenduduk(int jumlahPenduduk) {
        this.jumlahPenduduk = jumlahPenduduk;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public int getJumlahDesa() {
        return jumlahDesa;
    }

    public void setJumlahDesa(int jumlahDesa) {
        this.jumlahDesa = jumlahDesa;
    }

    public int getJumlahKelurahan() {
        return jumlahKelurahan;
    }

    public void setJumlahKelurahan(int jumlahKelurahan) {
        this.jumlahKelurahan = jumlahKelurahan;
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

    // Method untuk mendapatkan total wilayah administratif
    public int getTotalWilayahAdministratif() {
        return jumlahDesa + jumlahKelurahan;
    }

    // Method untuk menampilkan informasi Kecamatan
    @Override
    public String toString() {
        return "Kecamatan{" +
                "idKecamatan=" + idKecamatan +
                ", namaKecamatan='" + namaKecamatan + '\'' +
                ", alamatKantor='" + alamatKantor + '\'' +
                ", namaKepala='" + namaKepala + '\'' +
                ", alamatRumahKepala='" + alamatRumahKepala + '\'' +
                ", jumlahPenduduk=" + jumlahPenduduk +
                ", noHp='" + noHp + '\'' +
                ", jumlahDesa=" + jumlahDesa +
                ", jumlahKelurahan=" + jumlahKelurahan +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}