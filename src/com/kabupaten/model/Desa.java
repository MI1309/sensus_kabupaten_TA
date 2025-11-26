package com.kabupaten.model;

import java.sql.Timestamp;

/**
 * Model class untuk tabel Desa
 */
public class Desa {
    private int idDesa;
    private int idKecamatan;
    private String namaDesa;
    private String jenis; // ENUM: 'DESA' atau 'KELURAHAN'
    private String alamatKantor;
    private String namaKepala;
    private String alamatRumahKepala;
    private String noHp;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Field tambahan untuk relasi
    private String namaKecamatan;

    // Default constructor
    public Desa() {
    }

    // Constructor dengan parameter lengkap (tanpa ID untuk insert)
    public Desa(int idKecamatan, String namaDesa, String jenis, String alamatKantor,
                String namaKepala, String alamatRumahKepala, String noHp) {
        this.idKecamatan = idKecamatan;
        this.namaDesa = namaDesa;
        this.jenis = jenis;
        this.alamatKantor = alamatKantor;
        this.namaKepala = namaKepala;
        this.alamatRumahKepala = alamatRumahKepala;
        this.noHp = noHp;
    }

    // Constructor dengan semua field termasuk ID
    public Desa(int idDesa, int idKecamatan, String namaDesa, String jenis, 
                String alamatKantor, String namaKepala, String alamatRumahKepala, String noHp) {
        this.idDesa = idDesa;
        this.idKecamatan = idKecamatan;
        this.namaDesa = namaDesa;
        this.jenis = jenis;
        this.alamatKantor = alamatKantor;
        this.namaKepala = namaKepala;
        this.alamatRumahKepala = alamatRumahKepala;
        this.noHp = noHp;
    }

    // Getters and Setters
    public int getIdDesa() {
        return idDesa;
    }

    public void setIdDesa(int idDesa) {
        this.idDesa = idDesa;
    }

    public int getIdKecamatan() {
        return idKecamatan;
    }

    public void setIdKecamatan(int idKecamatan) {
        this.idKecamatan = idKecamatan;
    }

    public String getNamaDesa() {
        return namaDesa;
    }

    public void setNamaDesa(String namaDesa) {
        this.namaDesa = namaDesa;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
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

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
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

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public void setNamaKecamatan(String namaKecamatan) {
        this.namaKecamatan = namaKecamatan;
    }

    // Helper method untuk mendapatkan jenis lengkap
    public String getJenisLengkap() {
        if (jenis != null) {
            return jenis.equals("DESA") ? "Desa" : "Kelurahan";
        }
        return "-";
    }

    // Helper method untuk validasi jenis
    public boolean isValidJenis() {
        return jenis != null && (jenis.equals("DESA") || jenis.equals("KELURAHAN"));
    }

    // Helper method untuk mendapatkan label lengkap desa
    public String getLabelLengkap() {
        StringBuilder label = new StringBuilder();
        if (jenis != null && !jenis.isEmpty()) {
            label.append(jenis.equals("DESA") ? "Desa " : "Kelurahan ");
        }
        if (namaDesa != null) {
            label.append(namaDesa);
        }
        if (namaKecamatan != null && !namaKecamatan.isEmpty()) {
            label.append(", Kecamatan ").append(namaKecamatan);
        }
        return label.toString();
    }

    @Override
    public String toString() {
        return "Desa{" +
                "idDesa=" + idDesa +
                ", idKecamatan=" + idKecamatan +
                ", namaDesa='" + namaDesa + '\'' +
                ", jenis='" + jenis + '\'' +
                ", alamatKantor='" + alamatKantor + '\'' +
                ", namaKepala='" + namaKepala + '\'' +
                ", alamatRumahKepala='" + alamatRumahKepala + '\'' +
                ", noHp='" + noHp + '\'' +
                ", namaKecamatan='" + namaKecamatan + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // Method untuk menampilkan informasi singkat
    public String toShortString() {
        return String.format("%s %s (Kec. %s)", 
            jenis != null ? jenis : "-", 
            namaDesa != null ? namaDesa : "-",
            namaKecamatan != null ? namaKecamatan : "-");
    }
}