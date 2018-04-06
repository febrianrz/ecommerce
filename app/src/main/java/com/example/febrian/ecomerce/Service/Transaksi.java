package com.example.febrian.ecomerce.Service;

import com.example.febrian.ecomerce.Response.Bank;
import com.example.febrian.ecomerce.Response.Produk;
import com.example.febrian.ecomerce.Response.Register;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by febrian on 02/02/18.
 */

public interface Transaksi {

    @GET("produk")
    Call<List<Produk>> getProdukList(@Query("app_key") String app_key,
                                     @Query("katalog") String katalog);

    @GET("bank")
    Call<List<Bank>> getBank(@Query("app_key") String app_key);

    @FormUrlEncoded
    @POST("transaksi/checkout")
    Call<Register> postCheckout(@Field("app_key") String app_key,
                                @Field("id_produk[]") ArrayList<String> id_produk,
                                @Field("jumlah[]") ArrayList<String> jumlah,
                                @Field("keterangan[]") ArrayList<String> keterangan,
                                @Field("id_bank") String id_bank,
                                @Field("nama_pemesan") String nama_pemesan,
                                @Field("alamat_pengiriman") String alamat_pengiriman);
}
