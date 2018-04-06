package com.example.febrian.ecomerce;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.febrian.ecomerce.Config.Config;
import com.example.febrian.ecomerce.Response.Register;
import com.example.febrian.ecomerce.Response.UserModel;
import com.example.febrian.ecomerce.Service.Authentication;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView etEmail, etTelepon, etNama;
    private EditText etPassword, etRepassword;
    private Button btnSubmit, btnSumbitGoogle;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private final int RC_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Menunggu...");
        progressDialog.setCanceledOnTouchOutside(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Pendaftaran");

        // Set up the login form.
        etEmail         = (AutoCompleteTextView) findViewById(R.id.etEmail);
        etNama          = (AutoCompleteTextView) findViewById(R.id.etNama);
        etTelepon       = (AutoCompleteTextView) findViewById(R.id.etTelepon);
        etPassword      = (EditText) findViewById(R.id.etPassword);
        etRepassword    = (EditText) findViewById(R.id.etRePassword);
        btnSubmit       = (Button) findViewById(R.id.btnSubmit);
        btnSumbitGoogle = (Button) findViewById(R.id.btnSubmitGoogle);

        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister(etEmail.getText().toString(),etPassword.getText().toString(),
                        etNama.getText().toString(), true);
//                Toast.makeText(RegisterActivity.this,"tes",Toast.LENGTH_LONG).show();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Config.FIREBASE_KEY)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        btnSumbitGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Toast.makeText(RegisterActivity.this,"Registrasi Berhasil",Toast.LENGTH_LONG).show();
                doRegister(account.getEmail(), "asdfasdf", account.getDisplayName(), false);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Firebase Message: ", "Google sign in failed", e);
                Toast.makeText(RegisterActivity.this,"Gagal Login",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doRegister(String email, String password, final String nama, boolean useValidation){
        boolean validation = useValidation ? validation() : true;

        if(!validation){
            //jika gagal validasi
            Toast.makeText(this,"Inputan Tidak Sesuai",Toast.LENGTH_LONG).show();
        } else {
            //jika berhasil validasi
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Firebase Message: ", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserModel userModel = new UserModel();
                                userModel.setNama(user.getDisplayName());
                                userModel.setEmail(user.getEmail());
                                userModel.setGuid(user.getUid());
                                FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("users")
                                        .push()
                                        .setValue(userModel);
                                doRegisterServerApp(user.getDisplayName(),user.getEmail(),user.getUid());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Firebase Message: ", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

    //Register ke server aplikasi bukan firebase
    private void doRegisterServerApp(String nama, String email, String guid){
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

        Call<Register> registerCall = service.doRegister(
                Config.APP_KEY, email,
                nama, etTelepon.getText().toString(),
                etPassword.getText().toString(), guid
        );

        registerCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                progressDialog.dismiss();
                Register registerResponse = response.body();

                Toast.makeText(RegisterActivity.this,registerResponse.getMsg(),
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,"Terjadi kesalahan, silahkan coba kembali",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean validation(){
        boolean isValid = true;

        if(isValid){
            if(etNama.getText().toString().isEmpty()){
                isValid = false;
                etNama.setError("Wajib Diisi");
            }
        }

        if(isValid){
            if(etTelepon.getText().toString().isEmpty()){
                isValid = false;
                etTelepon.setError("Wajib Diisi");
            }
        }

        if(isValid){
            if(etEmail.getText().toString().isEmpty()){
                isValid = false;
                etEmail.setError("Wajib Diisi");
            }
        }

        if(isValid){
            if(!isValidEmail(etEmail.getText().toString())){
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

        if(isValid){
            if(etPassword.getText().toString().length() <= 6){
                isValid = false;
                etPassword.setError("Password Minimal 6 Karakter");
            }
        }

        if(isValid){
            if(etRepassword.getText().toString().isEmpty()){
                isValid = false;
                etRepassword.setError("Wajib Diisi");
            }
        }


        if(isValid){
            if(!etPassword.getText().toString().equals(etRepassword.getText().toString())){
                isValid = false;
                etRepassword.setError("Password Tidak Sesuai");
            }
        }

        return isValid;
    }

    public static boolean isValidEmail(String email){
        if(email.trim().matches("^[a-zA-Z0-9]+@[a-zA-Z0-9]+(.[a-zA-Z]{2,})$")) return true;
        return false;
    }
}

