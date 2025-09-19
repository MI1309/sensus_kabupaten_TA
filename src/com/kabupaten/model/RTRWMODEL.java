package com.kabupaten.model;

public class RTRWMODEL {
    private int id;
    private String kecamatan;
    private int nomorRW;
    private int nomorRT;
    private String alamat;
    private String status;

    public RTRWMODEL(int id, String kecamatan, int nomorRW, int nomorRT, String alamat, String status) {
        this.id = id;
        this.kecamatan = kecamatan;
        this.nomorRW = nomorRW;
        this.nomorRT = nomorRT;
        this.alamat = alamat;
        this.status = status;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getKecamatan() { return kecamatan; }
    public int getNomorRW() { return nomorRW; }
    public int getNomorRT() { return nomorRT; }
    public String getAlamat() { return alamat; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setKecamatan(String kecamatan) { this.kecamatan = kecamatan; }
    public void setNomorRW(int nomorRW) { this.nomorRW = nomorRW; }
    public void setNomorRT(int nomorRT) { this.nomorRT = nomorRT; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "RTRW{" +
                "id=" + id +
                ", kecamatan='" + kecamatan + '\'' +
                ", RW=" + nomorRW +
                ", RT=" + nomorRT +
                ", alamat='" + alamat + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
