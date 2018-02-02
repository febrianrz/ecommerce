package com.example.febrian.ecomerce.Service;

import com.example.febrian.ecomerce.Response.Login;
import com.example.febrian.ecomerce.Response.Register;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by febrian on 26/01/18.
 */

public interface Authentication {

    @FormUrlEncoded
    @POST("auth/register")
    Call<Register> doRegister(@Field("app_key") String app_key, @Field("email") String email,
                              @Field("nama") String nama, @Field("telepon") String telepon,
                              @Field("password") String password, @Field("guid") String guid);

    @FormUrlEncoded
    @POST("auth/login")
    Call<Login> doLogin(@Field("app_key") String app_key, @Field("email") String email,
                        @Field("password") String password);
}
