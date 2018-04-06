package com.example.febrian.ecomerce;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.febrian.ecomerce.Fragment.BerandaFragment;
import com.example.febrian.ecomerce.Fragment.KategoriFragment;
import com.example.febrian.ecomerce.Libraries.Auth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String titleBeranda = "Beranda";
    private TextView tvNavHeaderEmail;
    private ImageView ivIcon;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header         = navigationView.getHeaderView(0);
        tvNavHeaderEmail    = (TextView) header.findViewById(R.id.tvEmail);
        ivIcon              = (ImageView) header.findViewById(R.id.ivIcon);
        setDrawer();

        BerandaFragment berandaFragment = new BerandaFragment();
        callFragment(berandaFragment, titleBeranda);
    }

    //setting nama di navigation drawer
    private void setDrawer(){
        Menu navMenu = navigationView.getMenu();
        if(Auth.isLogin(MainActivity.this)){
            tvNavHeaderEmail.setText(Auth.getUser(MainActivity.this).getEmail());
            ivIcon.setImageResource(R.drawable.icon_happy);
            navMenu.findItem(R.id.nav_login).setVisible(false);
            navMenu.findItem(R.id.nav_register).setVisible(false);
        } else {
            tvNavHeaderEmail.setText("Anda Belum Login");
            ivIcon.setImageResource(R.drawable.ic_launcher_background);
            navMenu.findItem(R.id.nav_login).setVisible(true);
            navMenu.findItem(R.id.nav_register).setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_beranda) {
            fragment = new BerandaFragment();
            callFragment(fragment,titleBeranda);
        } else if (id == R.id.nav_woman) {
            fragment = new KategoriFragment();
            Bundle bundle = new Bundle();
            bundle.putString("idPelajar","");
            fragment.setArguments(bundle);
            callFragment(fragment, "Pakaian Wanita");
        } else if (id == R.id.nav_men) {
            fragment = new KategoriFragment();
            callFragment(fragment, "Pakaian Pria");
        } else if (id == R.id.nav_child) {
            fragment = new KategoriFragment();
            callFragment(fragment, "Pakaian Anak");
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_register) {
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this,AboutActivity.class);
            startActivity(intent);
        } else if(id == R.id.nav_exit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Keluar");
            builder.setMessage("Apakah Anda yakin?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MainActivity.this.finish();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this,"Anda tidak jadi keluar",Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        } else if(id == R.id.nav_chart){
            Intent i = new Intent(getApplicationContext(),KeranjangActivity.class);
            startActivity(i);
        } else if(id == R.id.nav_chat){
            Intent i = new Intent(getApplicationContext(),ListChattingActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callFragment(Fragment fragment, String title){
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(title);
    }
}
