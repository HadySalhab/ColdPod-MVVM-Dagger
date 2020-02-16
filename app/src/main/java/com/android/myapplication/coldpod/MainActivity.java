package com.android.myapplication.coldpod;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.myapplication.coldpod.databinding.ActivityMainBinding;
import com.android.myapplication.coldpod.ui.DownloadsFragment;
import com.android.myapplication.coldpod.ui.FavoritesFragment;
import com.android.myapplication.coldpod.ui.HomeFragment;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding mBinding;
    DrawerLayout drawerLayout;
    Toolbar mToolbar;
    SpannableString mSpannableString;
   ForegroundColorSpan mForegroundColorSpan;
    NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeMemberVariables();
       setupSpannableTitle();
        setupToolbarAndNavDrawer(savedInstanceState);
    }

    private void initializeMemberVariables() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        drawerLayout = mBinding.drawerLayout;
        mToolbar = mBinding.toolbar;
        mNavigationView = mBinding.navView;
        mSpannableString = new SpannableString(getString(R.string.app_name));
       mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, (R.color.primary_color)));
    }

    private void setupSpannableTitle() {
        mSpannableString.setSpan(mForegroundColorSpan, 4, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    private void setupToolbarAndNavDrawer(Bundle savedInstanceState) {
        this.setSupportActionBar(mToolbar);
        ((AppCompatActivity) this).getSupportActionBar().setTitle(mSpannableString);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener (this);
        if (savedInstanceState == null) {
            performTransaction(new HomeFragment());
            mNavigationView.setCheckedItem(R.id.drawer_nav_podcasts);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_nav_podcasts:
                performTransaction(new HomeFragment());
                break;
            case R.id.drawer_nav_favorites:
                performTransaction(new FavoritesFragment());
                break;
            case R.id.drawer_nav_downloads:
                performTransaction(new DownloadsFragment());
                break;
            default:
                performTransaction(new HomeFragment());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void performTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).commit();
    }


}

