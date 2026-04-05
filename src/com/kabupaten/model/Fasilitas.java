package com.kabupaten.model;

public class Fasilitas {
    private int idFasilitas;
    private String namaFasilitas;
    private String jenis;
    private String dinasTerkait;
    private String alamat;
    private String keterangan;
    private String createdAt;
    private String updatedAt;

    public Fasilitas() {}

    public Fasilitas(String namaFasilitas, String jenis, String dinasTerkait,
                     String alamat, String keterangan) {
        this.namaFasilitas = namaFasilitas;
        this.jenis = jenis;
        this.dinasTerkait = dinasTerkait;
        this.alamat = alamat;
        this.keterangan = keterangan;
    }

    // Getters & Setters
    public int getIdFasilitas() { return idFasilitas; }
    public void setIdFasilitas(int idFasilitas) { this.idFasilitas = idFasilitas; }

    public String getNamaFasilitas() { return namaFasilitas; }
    public void setNamaFasilitas(String namaFasilitas) { this.namaFasilitas = namaFasilitas; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public String getDinasTerkait() { return dinasTerkait; }
    public void setDinasTerkait(String dinasTerkait) { this.dinasTerkait = dinasTerkait; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}