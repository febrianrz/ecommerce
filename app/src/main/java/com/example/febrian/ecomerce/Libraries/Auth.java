package com.example.febrian.ecomerce.Libraries;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.febrian.ecomerce.Response.UserModel;
import com.google.gson.Gson;

/**
 * Created by febrian on 02/02/18.
 */

public class Auth {
    // ngecek user sudah login atau belum
    public static boolean isLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info",
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("status_login", false);
    }

    //untuk menymimpan kedalam sharedpref
    public static void setSession(Context context, UserModel userModel){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        editor.putBoolean("status_login",true);
        editor.putString("user_data",json);
        editor.commit();
    }

    //mengambil data user yang sedang login
    public static UserModel getUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info",
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user_data","");
        UserModel userModel = gson.fromJson(json,UserModel.class);
        return userModel;
    }

    public static void logout(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
