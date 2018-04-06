package com.example.febrian.ecomerce.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by febrian on 16/03/18.
 */

public class Bank {
    @SerializedName("id_bank")
    @Expose
    private String id;
    @SerializedName("nama_bank")
    @Expose
    private String namaBank;
    @SerializedName("atas_nama")
    @Expose
    private String atasNama;
    @SerializedName("no_rek")
    @Expose
    private String noRek;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaBank() {
        return namaBank;
    }

    public void setNamaBank(String namaBank) {
        this.namaBank = namaBank;
    }

    public String getAtasNama() {
        return atasNama;
    }

    public void setAtasNama(String atasNama) {
        this.atasNama = atasNama;
    }

    public String getNoRek() {
        return noRek;
    }

    public void setNoRek(String noRek) {
        this.noRek = noRek;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
