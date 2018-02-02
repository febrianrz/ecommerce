package com.example.febrian.ecomerce.Service;

import com.example.febrian.ecomerce.Response.Produk;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by febrian on 02/02/18.
 */

public interface Transaksi {

    @GET("produk")
    Call<List<Produk>> getProdukList(@Query("app_key") String app_key,
                                     @Query("katalog") String katalog);

}
