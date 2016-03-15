package com.nasahapps.bottomnav.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nasahapps.bottomnav.BottomNavigationBar;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    BottomNavigationBar mBottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomNav);
        mBottomNavigationBar.setOnTabSelectedListener(this);
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Recents")
                .setIcon(R.drawable.ic_na_test_history));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Favorites")
                .setIcon(R.drawable.ic_na_test_favorite));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Nearby")
                .setIcon(R.drawable.ic_na_test_location));
    }

    @Override
    public void onTabSelected(BottomNavigationBar.Tab tab, int position) {
        Log.d("BottomNav", "Tab selected: " + position);
    }

    @Override
    public void onTabReselected(BottomNavigationBar.Tab tab, int position) {
        Log.d("BottomNav", "Tab reselected: " + position);
    }

    @Override
    public void onTabUnselected(BottomNavigationBar.Tab tab, int position) {
        Log.d("BottomNav", "Tab unselected: " + position);
    }
}
