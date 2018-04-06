package com.example.febrian.ecomerce.Libraries;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.febrian.ecomerce.Config.Config;

/**
 * Created by febrian on 06/03/18.
 */

public class Cart  extends SQLiteOpenHelper {
    public static String TABLE_CART_NAME = "table_cart";
    private String CREATE_TABLE_CART = "CREATE TABLE "+TABLE_CART_NAME+" (id INTEGER PRIMARY KEY, " +
            "nama_barang TEXT, jumlah INTEGER, harga INTEGER, keterangan TEXT, gambar TEXT, id_produk TEXT)";
    private String DELETE_TABLE_CART = "DROP TABLE IF EXISTS table_cart";
    private String ALTER_TABLE_CART_ADD_ID_PRODUK = "ALTER TABLE "+TABLE_CART_NAME+" ADD id_produk TEXT";

    public Cart(Context context) {
        super(context, Config.DATABASE_NAME,null,Config.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE_CART);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

}
