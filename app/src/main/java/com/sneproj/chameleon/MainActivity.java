package com.sneproj.chameleon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sneproj.chameleon.databinding.ActivityMainBinding;
import com.sneproj.chameleon.fragments.ExploreFragment;
import com.sneproj.chameleon.fragments.HamariFragment;
import com.sneproj.chameleon.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(MainActivity.this.getColor(R.color.bg_main));
            getWindow().setNavigationBarColor(MainActivity.this.getColor(R.color.white));
        }

        binding.bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new HomeFragment()).addToBackStack(null).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.explore:
                        fragment = new ExploreFragment();
                        break;
                    case R.id.profile:
                        fragment = new HamariFragment();
                        break;
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            fragment).addToBackStack(null).commit();
                }

                return true;
            };
}