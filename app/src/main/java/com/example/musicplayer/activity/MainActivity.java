package com.example.musicplayer.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.transformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager2 = findViewById(R.id.view_pager2);

        viewPager2.setPageTransformer(new ZoomOutPageTransformer());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_songs).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_favorites).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_search).setChecked(true);
                        break;
                }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_songs:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.menu_favorites:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.menu_search:
                        viewPager2.setCurrentItem(3);
                        break;
                    default:
                        viewPager2.setCurrentItem(0);
                        break;
                }
                return true;
            }
        });
    }
}