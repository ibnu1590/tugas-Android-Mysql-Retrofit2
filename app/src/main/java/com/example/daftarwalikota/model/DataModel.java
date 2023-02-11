package com.example.daftarwalikota.model;

public class DataModel {
    private int id;
    private String nama, alamat, partai;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPartai() {
        return partai;
    }

    public void setPartai(String partai) {
        this.partai = partai;
    }
}
