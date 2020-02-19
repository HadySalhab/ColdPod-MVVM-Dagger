package com.android.myapplication.coldpod.ui.podcasts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivityPodcastListBinding;
import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.ui.details.PodCastDetailActivity;
import com.android.myapplication.coldpod.utils.GridAutofitLayoutManager;

import javax.inject.Inject;

import static com.android.myapplication.coldpod.utils.Constants.GRID_AUTO_FIT_COLUMN_WIDTH;

public class PodCastListActivity extends AppCompatActivity implements PodCastListAdapter.Listener {
    private static final String TAG = "PodCastListActivity";
    private ActivityPodcastListBinding mBinding;
    private PodCastListViewModel mPodCastListViewModel;
    Toolbar mToolbar;
    SpannableString mSpannableString;
    ForegroundColorSpan mForegroundColorSpan;

    private PodCastListAdapter mPodCastListAdapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    DiffUtil.ItemCallback<Podcasts> mPodcastsItemCallback;


    public static Intent getInstance(Context context){
        return new Intent(context,PodCastListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_podcast_list);
        ((BaseApplication) getApplication()).getAppComponent()
                .getMainComponent()
                .injectPodCastListActivity(this);
        mPodCastListViewModel = new ViewModelProvider(this, providerFactory).get(PodCastListViewModel.class);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(mPodCastListViewModel);
        initRV();
        initToolbar();
        mPodCastListViewModel.getPodCasts("us");
    }

    private void initToolbar(){
        mToolbar = mBinding.toolbar;
        mSpannableString = new SpannableString(getString(R.string.app_name));
        mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, (R.color.primary_color)));
        mSpannableString.setSpan(mForegroundColorSpan, 4, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        this.setSupportActionBar(mToolbar);
        ((AppCompatActivity) this).getSupportActionBar().setTitle(mSpannableString);
        ((AppCompatActivity) this).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRV() {
        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(this, GRID_AUTO_FIT_COLUMN_WIDTH);
        mBinding.rvAddPodcast.setLayoutManager(layoutManager);
        mBinding.rvAddPodcast.setHasFixedSize(true);
        mPodCastListAdapter = new PodCastListAdapter(mPodcastsItemCallback, this);
        mBinding.rvAddPodcast.setAdapter(mPodCastListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(Podcasts podcasts) {
        Log.d(TAG, "onItemClick: " + podcasts.getId());
        startActivity(PodCastDetailActivity.getInstance(this,podcasts.getId(),podcasts.getName()));
    }


}
