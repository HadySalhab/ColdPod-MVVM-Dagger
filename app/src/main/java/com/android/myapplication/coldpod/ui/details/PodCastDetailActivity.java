package com.android.myapplication.coldpod.ui.details;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivityPodcastDetailBinding;
import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.network.Category;
import com.android.myapplication.coldpod.network.Channel;
import com.android.myapplication.coldpod.network.Enclosure;
import com.android.myapplication.coldpod.network.Item;
import com.android.myapplication.coldpod.network.RssFeed;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class PodCastDetailActivity extends AppCompatActivity {
    private static final String TAG = "PodCastDetailActivity";
    private String podCastsId;
    private PodCastDetailViewModel mPodCastDetailViewModel;
    private ActivityPodcastDetailBinding mBinding;
    private PodCastDetailAdapter adapter;


    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    DiffUtil.ItemCallback<Item> mPodcastDetailDiffUtil;

    public static Intent getInstance(Context context, String podCastId) {
        Intent intent = new Intent(context,PodCastDetailActivity.class);
        intent.putExtra(EXTRA_PODCAST_ID, podCastId);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + getIntent());
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_podcast_detail);
        mBinding.setLifecycleOwner(this);
        Intent intent = getIntent();
        this.podCastsId = intent.getStringExtra(EXTRA_PODCAST_ID);
        initDagger();
        initViewModel();
        initAdapter();

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
        ((BaseApplication)getApplication()).getAppComponent()
                .getMainComponent()
                .injectPodCastDetailActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPodCastDetailViewModel.getProgress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=null) {
                    boolean v = integer==View.GONE;
                    Log.d(TAG, "onChanged: " + v);
                }
            }
        });
    }
}
