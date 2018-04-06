package com.example.febrian.ecomerce;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.febrian.ecomerce.Config.Config;
import com.example.febrian.ecomerce.Libraries.Auth;
import com.example.febrian.ecomerce.Libraries.Cart;
import com.example.febrian.ecomerce.Response.Bank;
import com.example.febrian.ecomerce.Response.Produk;
import com.example.febrian.ecomerce.Response.Register;
import com.example.febrian.ecomerce.Service.Transaksi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutActivity extends AppCompatActivity {
    TextInputEditText etNama, etAlamat;
    Button btnSubmit;
    ListView listBank;
    String id_bank = null;
    ArrayList<String> id_produk = new ArrayList<>();
    ArrayList<String> jumlah = new ArrayList<>();
    ArrayList<String> keterangan = new ArrayList<>();
    Cart cartHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setTitle("Checkout");
        etNama      = (TextInputEditText) findViewById(R.id.etNamaPemesan);
        etAlamat    = (TextInputEditText) findViewById(R.id.etAlamat);
        btnSubmit   = (Button) findViewById(R.id.btnSubmit);
        listBank    = (ListView) findViewById(R.id.listBank);
        cartHelper      = new Cart(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });

        etNama.setText(Auth.getUser(this).getNama());
        getBank();
        loadDataFromDb();

    }

    private void loadDataFromDb(){
        SQLiteDatabase db = cartHelper.getReadableDatabase();
        String[] projection = {
                "id",
                "nama_barang",
                "jumlah",
                "harga",
                "keterangan",
                "gambar",
                "id_produk"
        };
        Cursor cursor = db.query(
                "table_cart",   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        while(cursor.moveToNext()) {
//            id_produk
            id_produk.add(cursor.getString(cursor.getColumnIndex("id_produk")));
            jumlah.add(cursor.getString(cursor.getColumnIndex("jumlah")));
            keterangan.add(cursor.getString(cursor.getColumnIndex("keterangan")));
        }
        cursor.close();
    }

    private void postData(){
        if(id_bank == null){
            Toast.makeText(this,"Bank wajib dipilih",Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Konfirmasi");
            builder.setMessage("Apakah Anda yakin data telah sesuai?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
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
                    Call<Register> checkoutCall = service.postCheckout(
                            Config.APP_KEY,
                            id_produk,
                            jumlah,
                            keterangan,
                            id_bank,
                            etNama.getText().toString(),
                            etAlamat.getText().toString()
                    );
                    checkoutCall.enqueue(new Callback<Register>() {
                        @Override
                        public void onResponse(Call<Register> call, Response<Register> response) {
                            Toast.makeText(CheckoutActivity.this,response.body().getMsg(),
                                    Toast.LENGTH_SHORT).show();
                            truncateTable();
                            CheckoutActivity.this.finish();
                        }

                        @Override
                        public void onFailure(Call<Register> call, Throwable t) {

                        }
                    });
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(CheckoutActivity.this,"Submit dibatalkan",Toast.LENGTH_SHORT)
                            .show();
                }
            });
            builder.show();
        }
    }

    private void getBank(){
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
        Call<List<Bank>> requestCall = service.getBank(Config.APP_KEY);
        requestCall.enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                final List<Bank> bankList = response.body();
                final String[] namaBank = new String[bankList.size()];
                int i = 0;
                for(Bank bank : bankList){
                    namaBank[i] = bank.getNamaBank();
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        CheckoutActivity.this,R.layout.item_bank,
                        namaBank);
                listBank.setAdapter(adapter);
//                listBank.
                listBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(CheckoutActivity.this,"Bank : "+namaBank[i],
                                Toast.LENGTH_SHORT).show();
                        id_bank = bankList.get(i).getId();
//                        Toast.makeText(CheckoutActivity.this,"id bank "+bankList.get(i).getId(),
//                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {

            }
        });
    }

    private void truncateTable(){
        SQLiteDatabase db = cartHelper.getWritableDatabase();
        db.execSQL("DELETE FROM table_cart");
        db.close();
    }
}
