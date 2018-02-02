package com.example.febrian.ecomerce;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.febrian.ecomerce.Config.Config;
import com.example.febrian.ecomerce.Libraries.Auth;
import com.example.febrian.ecomerce.Response.Login;
import com.example.febrian.ecomerce.Response.UserModel;
import com.example.febrian.ecomerce.Service.Authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView etEmail;
    private EditText etPassword;
    private Button btnSubmit;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Menunggu...");
        progressDialog.setCanceledOnTouchOutside(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Masuk Applikasi");
        // Set up the login form.
        etEmail     = (AutoCompleteTextView) findViewById(R.id.email);
        etPassword  = (EditText) findViewById(R.id.password);
        btnSubmit   = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void doLogin(){
        boolean validation = validation();
        if(!validation){
            Toast.makeText(this,"Periksa kembali inputan Anda",Toast.LENGTH_LONG).show();
        } else {
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Firebase Message: ", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                doLoginServerApp(user.getEmail());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Firebase Message: ", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private void doLoginServerApp(String email){
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

        Authentication service = retrofit.create(Authentication.class);

        Call<Login> requestCall = service.doLogin(Config.APP_KEY,
                email,
                etPassword.getText().toString());
        requestCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(response.body().getMsg());
                builder.show();
                Login loginResponse = response.body();
                UserModel userModel = loginResponse.getUserModel();
                if(response.body().getStatus()){
                    Auth.setSession(LoginActivity.this, userModel);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });

    }

    private boolean validation(){
        boolean isValid = true;
        if(isValid){
            if(etEmail.getText().toString().isEmpty()){
                isValid = false;
                etEmail.setError("Wajib Diisi");
            }
        }
        if(isValid){
            if(!RegisterActivity.isValidEmail(etEmail.getText().toString())){
                isValid = false;
                etEmail.setError("Email Tidak Sesuai");
            }
        }
        if(isValid){
            if(etPassword.getText().toString().isEmpty()){
                isValid = false;
                etPassword.setError("Wajib Diisi");
            }
        }
        return isValid;
    }


}

