package com.android.myapplication.coldpod.ui.podcast_entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivityPodcastEntryBinding;

import javax.inject.Inject;

import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_ID;

public class PodCastEntryActivity extends AppCompatActivity {
    private ActivityPodcastEntryBinding mBinding;
    @Inject
    ViewModelProviderFactory providerFactory;

    private String podCastId;
    private PodCastEntryViewModel mViewModel;

    public static Intent getInstance(Context context, String podcastId) {
        Intent intent = new Intent(context, PodCastEntryActivity.class);
        intent.putExtra(EXTRA_PODCAST_ID, podcastId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
        podCastId = getIntent().getStringExtra(EXTRA_PODCAST_ID);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_podcast_entry);
        mViewModel = new ViewModelProvider(this, providerFactory).get(PodCastEntryViewModel.class);
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(this);
        mViewModel.setPodCastId(podCastId);
    }

    private void initDagger() {
        ((BaseApplication) getApplication()).getAppComponent().getMainComponent().injectPodCastEntryActivity(this);
    }
}
