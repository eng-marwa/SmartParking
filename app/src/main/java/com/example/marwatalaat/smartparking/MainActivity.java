package com.example.marwatalaat.smartparking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.marwatalaat.smartparking.adapters.TabPagerAdapter;
import com.example.marwatalaat.smartparking.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager viewPager;
    private User user;
    private SharedPreferences pref;
    private DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pref = getSharedPreferences("userReg",MODE_PRIVATE);
        if(getIntent().hasExtra("user")) {
             user = getIntent().getExtras().getParcelable("user");
        }else {
            Gson gson = new Gson();
            String userSaved = pref.getString("user", null);
             user = gson.fromJson(userSaved, User.class);
        }
            TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), user, this);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager, true);
            adapter.setupTabIcons(tabLayout);





    }
    public void switchToMesFragment(boolean reserve) {
        SharedPreferences resvPref = getSharedPreferences("reservation", MODE_PRIVATE);
        resvPref.edit().putBoolean("isReserved",reserve).commit();
        viewPager.setCurrentItem(0);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.floor,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LoginActivity.class));

        return super.onOptionsItemSelected(item);
    }
}
