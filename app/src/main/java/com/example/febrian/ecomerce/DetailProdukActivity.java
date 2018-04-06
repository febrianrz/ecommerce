package com.example.febrian.ecomerce;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.febrian.ecomerce.Adapter.KatergoriAdapter;
import com.example.febrian.ecomerce.Libraries.Cart;
import com.squareup.picasso.Picasso;

public class DetailProdukActivity extends AppCompatActivity {
    ImageView ivGambar;
    TextView tvNamaProduk, tvHarga, tvDeskripsi;
    TextInputEditText etJumlahBeli, etKeterangan;
    Button btnCart;
    Cart cartHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent i = getIntent();
        tvNamaProduk    = (TextView) findViewById(R.id.tvNamaProduk);
        tvHarga         = (TextView) findViewById(R.id.tvHarga);
        tvDeskripsi     = (TextView) findViewById(R.id.tvDeskripsi);
        ivGambar        = (ImageView) findViewById(R.id.tvGambar);
        btnCart         = (Button) findViewById(R.id.btnAddCard);
        etJumlahBeli    = (TextInputEditText) findViewById(R.id.etJumlahBeli);
        etKeterangan    = (TextInputEditText) findViewById(R.id.etKeterangan);
        cartHelper      = new Cart(this);


        getSupportActionBar().setTitle(i.getStringExtra("namaBarang"));
        tvNamaProduk.setText(i.getStringExtra("namaBarang"));
        tvHarga.setText(KatergoriAdapter.formatRupiah(Double.parseDouble(i.getStringExtra("hargaBarang"))));
        tvDeskripsi.setText(i.getStringExtra("deskripsi"));
        Picasso.with(this).load(i.getStringExtra("gambarBarang"))
                .fit()
                .error(R.drawable.user_bg)
                .placeholder(R.drawable.user_bg)
                .centerCrop().into(ivGambar);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etJumlahBeli.getText().toString().isEmpty()){
                   Toast.makeText(DetailProdukActivity.this,"Jumlah pembelian wajib diisi",
                           Toast.LENGTH_SHORT)
                           .show();
                } else {

                    SQLiteDatabase db       = cartHelper.getWritableDatabase();
                    ContentValues values    = new ContentValues();
                    values.put("nama_barang",i.getStringExtra("namaBarang"));
                    values.put("id_produk",i.getStringExtra("idBarang"));
                    values.put("gambar",i.getStringExtra("gambarBarang"));
                    values.put("jumlah",Integer.parseInt(etJumlahBeli.getText().toString()));
                    values.put("harga",Integer.parseInt(i.getStringExtra("hargaBarang")));
                    values.put("keterangan",etKeterangan.getText().toString());
                    db.insert("table_cart", null, values);

                    Toast.makeText(DetailProdukActivity.this,"Berhasil memasukkan ke keranjang",
                            Toast.LENGTH_SHORT).show();
                    cekDatabase();
                }
            }
        });
    }

    private void cekDatabase(){
        SQLiteDatabase db = cartHelper.getReadableDatabase();
        String[] projection = {
                "id",
                "nama_barang",
                "jumlah",
                "harga",
                "keterangan",
                "gambar"
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
            Log.i("Nama Barang",cursor.getString(cursor.getColumnIndex("nama_barang")));
            Log.i("Jumlah",cursor.getString(cursor.getColumnIndex("jumlah")));
        }
        cursor.close();
    }
}
