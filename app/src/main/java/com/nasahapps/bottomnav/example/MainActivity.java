package com.nasahapps.bottomnav.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nasahapps.bottomnav.BottomNavigationBar;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabClickedListener {

    BottomNavigationBar mBottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomNav);
        mBottomNavigationBar.setOnTabClickedListener(this);
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Recents")
                .setIcon(R.drawable.ic_history));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Favorites")
                .setIcon(R.drawable.ic_favorite));
        mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Nearby")
                .setIcon(R.drawable.ic_location));
    }

    @Override
    public void onTabClicked(BottomNavigationBar.Tab tab, int position) {
        Log.d("BottomNav", "Tab clicked: " + position);
    }
}
