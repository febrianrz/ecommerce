package com.example.febrian.ecomerce;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.febrian.ecomerce.Adapter.KatergoriAdapter;
import com.example.febrian.ecomerce.Adapter.KeranjangAdapter;
import com.example.febrian.ecomerce.Libraries.Cart;
import com.example.febrian.ecomerce.Response.Produk;

import java.util.ArrayList;
import java.util.List;

public class KeranjangActivity extends AppCompatActivity {
    private RecyclerView rvKeranjang;
    private RecyclerView.Adapter recylerAdapter;
    private RecyclerView.LayoutManager recylerLayoutManager;
    List<Produk> arrData = new ArrayList<>();
    Cart cartHelper;
    Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Keranjang Belanja");


        cartHelper      = new Cart(this);
        rvKeranjang     = (RecyclerView) findViewById(R.id.rvKeranjang);
        btnCheckout     = (Button) findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrData.size() == 0){
                    Toast.makeText(KeranjangActivity.this,"Cart Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(KeranjangActivity.this,CheckoutActivity.class);
                    startActivity(i);
                }
            }
        });

        setList();
    }

    private void setList(){
        loadData();
        recylerLayoutManager =  new LinearLayoutManager(this);
        rvKeranjang.setHasFixedSize(true);
        rvKeranjang.setLayoutManager(recylerLayoutManager);
        recylerAdapter =  new KeranjangAdapter(this,arrData);

        ItemTouchHelper swipeToRemove = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                        ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                deleteData(viewHolder.getAdapterPosition());
            }
        });
        swipeToRemove.attachToRecyclerView(rvKeranjang);
        rvKeranjang.setAdapter(recylerAdapter);
    }
    @Override
    public void onResume(){
        super.onResume();
        setList();
    }

    private void loadData(){
        arrData.clear();
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
            Produk p = new Produk();
            p.setId(cursor.getString(cursor.getColumnIndex("id")));
            p.setNamaBarang(cursor.getString(cursor.getColumnIndex("nama_barang")));
            p.setJumlah(cursor.getString(cursor.getColumnIndex("jumlah")));
            p.setHargaJual(cursor.getString(cursor.getColumnIndex("harga")));
            p.setGambar(cursor.getString(cursor.getColumnIndex("gambar")));
//            Log.i("Nama Barang",cursor.getString(cursor.getColumnIndex("nama_barang")));
//            Log.i("Jumlah",cursor.getString(cursor.getColumnIndex("jumlah")));
            arrData.add(p);
        }
        Toast.makeText(this,"total "+arrData.size(),Toast.LENGTH_SHORT).show();
        cursor.close();
    }

    private void deleteData(int position){
        String id = arrData.get(position).getId();
        SQLiteDatabase db = cartHelper.getWritableDatabase();
//        db.execSQL("DELETE FROM table_cart WHERE id = '"+id+"'");
        db.delete("table_cart","id = "+id,null);
        db.close();
        Toast.makeText(this,"Berhasil dihapus",Toast.LENGTH_SHORT).show();
        setList();
    }
}
