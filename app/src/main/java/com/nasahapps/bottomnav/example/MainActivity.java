package com.nasahapps.bottomnav.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        setBackgroundColors(3);

        Button addButton = (Button) findViewById(R.id.addTabButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomNavigationBar.getTabCount() == 3) {
                    mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Movies")
                            .setIcon(R.drawable.ic_movie));
                    setBackgroundColors(4);
                } else if (mBottomNavigationBar.getTabCount() == 4) {
                    mBottomNavigationBar.addTab(mBottomNavigationBar.newTab().setText("Music")
                            .setIcon(R.drawable.ic_music));
                    setBackgroundColors(5);
                } else {
                    Toast.makeText(MainActivity.this, "Don't make more than 5 tabs!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button removeButton = (Button) findViewById(R.id.removeTabButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomNavigationBar.getTabCount() == 5) {
                    mBottomNavigationBar.removeTabAt(4);
                    setBackgroundColors(4);
                } else if (mBottomNavigationBar.getTabCount() == 4) {
                    mBottomNavigationBar.removeTabAt(3);
                    setBackgroundColors(3);
                } else {
                    Toast.makeText(MainActivity.this, "Don't have less than 3 tabs!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBackgroundColors(int size) {
        int[] colors = new int[size];
        colors[0] = Color.BLUE;
        colors[1] = Color.GRAY;
        colors[2] = Color.RED;
        if (size >= 4) {
            colors[3] = Color.MAGENTA;
        }
        if (size == 5) {
            colors[4] = Color.GREEN;
        }
        mBottomNavigationBar.setBackgroundColors(colors);
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
