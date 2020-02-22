package com.android.myapplication.coldpod.ui.podcast_entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.persistence.PodcastEntry;
import com.android.myapplication.coldpod.databinding.ActivityPodcastEntryBinding;
import com.android.myapplication.coldpod.persistence.Item;
import com.android.myapplication.coldpod.service.PodcastService;
import com.android.myapplication.coldpod.ui.playing.PlayingActivity;
import com.google.android.material.appbar.AppBarLayout;

import javax.inject.Inject;

import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_ID;
import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_NAME;

public class PodCastEntryActivity extends AppCompatActivity implements PodCastEntryAdapter.Listener {
    private ActivityPodcastEntryBinding mBinding;
    private PodCastEntryAdapter mAdapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    DiffUtil.ItemCallback<Item> mItemItemCallback;

    private String podCastId;
    private String podCastName;
    private PodCastEntryViewModel mViewModel;
    private String podcastImage;
    private String podcastTitle;


    public static Intent getInstance(Context context, String podcastId, String podCastName) {
        Intent intent = new Intent(context, PodCastEntryActivity.class);
        intent.putExtra(EXTRA_PODCAST_ID, podcastId);
        intent.putExtra(EXTRA_PODCAST_NAME, podCastName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
        podCastId = getIntent().getStringExtra(EXTRA_PODCAST_ID);
        podCastName = getIntent().getStringExtra(EXTRA_PODCAST_NAME);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_podcast_entry);
        mViewModel = new ViewModelProvider(this, providerFactory).get(PodCastEntryViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        initRv();
        initToolbar();
        initCollapsingToolbar();
        mViewModel.setPodCastId(podCastId);
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBinding.rvDetail.setLayoutManager(layoutManager);
        mBinding.rvDetail.setHasFixedSize(true);
        mAdapter = new PodCastEntryAdapter(mItemItemCallback, this);
        mBinding.rvDetail.setAdapter(mAdapter);
    }

    private void initDagger() {
        ((BaseApplication) getApplication()).getAppComponent().getMainComponent().injectPodCastEntryActivity(this);
    }

    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initCollapsingToolbar() {
        mBinding.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.black_color));
        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset == 0) {
                    // When a CollapsingToolbarLayout is expanded, hide the title
                    mBinding.collapsingToolbar.setTitle(" ");
                } else if (Math.abs(verticalOffset) >= scrollRange) {
                    // When a CollapsingToolbarLayout is fully collapsed, show the title
                    if (mBinding != null) {
                        mBinding.collapsingToolbar.setTitle(podCastName);
                    }
                } else {
                    // Otherwise, hide the title
                    mBinding.collapsingToolbar.setTitle(" ");
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

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.dbPodcastEntry.observe(this, new Observer<PodcastEntry>() {
            @Override
            public void onChanged(PodcastEntry podcastEntry) {
                if(podcastEntry!=null){
                    podcastImage = podcastEntry.getArtworkImageUrl();
                    podcastTitle = podcastEntry.getTitle();
                }
            }
        });
    }

    @Override
    public void onItemClick(Item item) {
        startActivity(PlayingActivity.getInstance(this,item,podCastId,podcastImage,podCastName));

        Log.d("test", "onItemClick: "+ mViewModel.dbPodcastEntry.getValue().getTitle());

        startService(PodcastService.getInstance(this,item,podCastId,podcastImage,podcastTitle));
    }
}
