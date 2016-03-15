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
                .setIcon(R.drawable.ic_history));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Favorites")
                .setIcon(R.drawable.ic_favorite));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Nearby")
                .setIcon(R.drawable.ic_location));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Movies")
                .setIcon(R.drawable.ic_movie));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Music")
                .setIcon(R.drawable.ic_music));
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
