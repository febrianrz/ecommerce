package com.example.febrian.ecomerce.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.febrian.ecomerce.Config.Config;
import com.example.febrian.ecomerce.R;
import com.example.febrian.ecomerce.Response.Produk;
import com.example.febrian.ecomerce.Service.Transaksi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class KategoriFragment extends Fragment {


    public KategoriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadData();
        return inflater.inflate(R.layout.fragment_kategori, container, false);
    }

    private void loadData(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_APP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Transaksi service = retrofit.create(Transaksi.class);
        Call<List<Produk>> requestCall = service.getProdukList(Config.APP_KEY,
                    Config.KATALOG_ANAK
                );
        requestCall.enqueue(new Callback<List<Produk>>() {
            @Override
            public void onResponse(Call<List<Produk>> call, Response<List<Produk>> response) {

            }

            @Override
            public void onFailure(Call<List<Produk>> call, Throwable t) {

            }
        });
    }

}
