package com.example.sookmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentSearch fragmentSearch = new FragmentSearch();
    private FragmentMap fragmentMap = new FragmentMap();
    private FragmentFavorite fragmentFavorite = new FragmentFavorite();
    private FragmentHelp fragmentHelp = new FragmentHelp();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMap).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

}
class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.map:
                    transaction.replace(R.id.frameLayout, fragmentMap).commitAllowingStateLoss();
                    break;
                case R.id.search:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();
                    break;
                case R.id.favorites:
                    transaction.replace(R.id.frameLayout, fragmentFavorite).commitAllowingStateLoss();
                    break;
                case R.id.help:
                    transaction.replace(R.id.frameLayout, fragmentHelp).commitAllowingStateLoss();
                    break;
            }
            return true;
        }

    }
}
