package com.android.myapplication.coldpod.ui.main;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivityMainBinding;
import com.android.myapplication.coldpod.ui.podcasts.PodCastListActivity;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ActivityMainBinding mBinding;
    DrawerLayout drawerLayout;
    Toolbar mToolbar;
    SpannableString mSpannableString;
    ForegroundColorSpan mForegroundColorSpan;
    NavigationView mNavigationView;
    MainActivityViewModel mViewModel;


    @Inject
    ViewModelProviderFactory providerFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDagger();
        initializeMemberVariables();
        setupSpannableTitle();
        setupToolbarAndNavDrawer(savedInstanceState);
    }

    private void setUpDagger() {
        ((BaseApplication) getApplication()).getAppComponent().getMainComponent()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribeObserver();
    }

    private void subscribeObserver() {
        mViewModel.getNavToPodcastsActivity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(PodCastListActivity.getInstance(MainActivity.this));
                   // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    //reseting
                    mViewModel.setNavToPodcastsActivity(false);
                }
            }
        });
    }

    private void initializeMemberVariables() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);
        drawerLayout = mBinding.drawerLayout;
        mToolbar = mBinding.toolbar;
        mNavigationView = mBinding.navView;
        mSpannableString = new SpannableString(getString(R.string.app_name));
        mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, (R.color.primary_color)));
        mViewModel = new ViewModelProvider(this, providerFactory).get(MainActivityViewModel.class);
        mBinding.setViewModel(mViewModel);
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
        mNavigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            performTransaction(new SubscribedFragment());
            mNavigationView.setCheckedItem(R.id.drawer_nav_podcasts);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_nav_podcasts:
                performTransaction(new SubscribedFragment());
                break;
            case R.id.drawer_nav_favorites:
                performTransaction(new FavoritesFragment());
                break;
            case R.id.drawer_nav_downloads:
                performTransaction(new DownloadsFragment());
                break;
            default:
                performTransaction(new SubscribedFragment());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void performTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, fragment).commit();
        if (fragment instanceof SubscribedFragment) {
            mViewModel.setIfFabVisible(true);
        } else {
            mViewModel.setIfFabVisible(false);
        }
    }



    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragments_container) instanceof SubscribedFragment) {
            mViewModel.setIfFabVisible(true);
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

