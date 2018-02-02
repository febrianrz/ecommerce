package com.example.febrian.ecomerce.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by febrian on 02/02/18.
 */

public class Produk {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("kode_barang")
    @Expose
    private String kodeBarang;
    @SerializedName("nama_barang")
    @Expose
    private String namaBarang;
    @SerializedName("satuan")
    @Expose
    private String satuan;
    @SerializedName("harga_jual")
    @Expose
    private String hargaJual;
    @SerializedName("id_katalog")
    @Expose
    private String idKatalog;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(String hargaJual) {
        this.hargaJual = hargaJual;
    }

    public String getIdKatalog() {
        return idKatalog;
    }

    public void setIdKatalog(String idKatalog) {
        this.idKatalog = idKatalog;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
