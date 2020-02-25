package com.android.myapplication.coldpod.ui.details;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivityPodcastDetailBinding;
import com.android.myapplication.coldpod.persistence.Item;
import com.google.android.material.appbar.AppBarLayout;

import javax.inject.Inject;

import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_ID;
import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class PodCastDetailActivity extends AppCompatActivity {
    private static final String TAG = "PodCastDetailActivity";
    private String podCastsId;
    private String podCastName;
    private PodCastDetailViewModel mPodCastDetailViewModel;
    private ActivityPodcastDetailBinding mBinding;
    private PodCastDetailAdapter adapter;


    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    DiffUtil.ItemCallback<Item> mPodcastDetailDiffUtil;

    public static Intent getInstance(Context context, String podCastId, String podCastName) {
        Intent intent = new Intent(context, PodCastDetailActivity.class);
        intent.putExtra(EXTRA_PODCAST_ID, podCastId);
        intent.putExtra(EXTRA_PODCAST_NAME, podCastName);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + getIntent());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_podcast_detail);
        mBinding.setLifecycleOwner(this);
        Intent intent = getIntent();
        this.podCastsId = intent.getStringExtra(EXTRA_PODCAST_ID);
        this.podCastName = intent.getStringExtra(EXTRA_PODCAST_NAME);
        initToolbar();
        initCollapsingToolbar();
        initDagger();
        initViewModel();
        initAdapter();

    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.rvItem.setLayoutManager(layoutManager);
        mBinding.rvItem.setHasFixedSize(true);
        adapter = new PodCastDetailAdapter(mPodcastDetailDiffUtil);
        // Set adapter to the RecyclerView
        mBinding.rvItem.setAdapter(adapter);
    }


    private void initViewModel() {
        mPodCastDetailViewModel = new ViewModelProvider(this, providerFactory).get(PodCastDetailViewModel.class);
        mPodCastDetailViewModel.setPodCastId(this.podCastsId);
        mBinding.setViewModel(mPodCastDetailViewModel);
    }

    private void initDagger() {
        ((BaseApplication) getApplication()).getAppComponent()
                .getMainComponent()
                .injectPodCastDetailActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPodCastDetailViewModel.getProgress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    boolean v = integer == View.GONE;
                    Log.d(TAG, "onChanged: " + v);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void initCollapsingToolbar(){
        mBinding.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.black_color));

        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset == 0) {
                    mBinding.collapsingToolbar.setTitle(" ");
                } else if (Math.abs(verticalOffset) >= scrollRange) {
                    // When a CollapsingToolbarLayout is fully collapsed, show the title
                    if (mBinding != null) {
                        mBinding.collapsingToolbar.setTitle(podCastName);
                    }
                } else {
                    mBinding.collapsingToolbar.setTitle(" ");
                }
            }
        });
    }
}
