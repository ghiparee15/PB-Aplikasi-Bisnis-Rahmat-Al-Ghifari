package com.example.bussinesapp;

public class Barang {
    private String namaBarang;
    private int jumlah;
    private String id;

    // Konstruktor
    public Barang(String namaBarang, int jumlah) {
        this.namaBarang = namaBarang;
        this.jumlah = jumlah;
    }

    // Getter dan Setter
    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
