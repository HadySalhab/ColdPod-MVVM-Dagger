package com.android.myapplication.coldpod.ui.podcasts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivityPodcastListBinding;
import com.android.myapplication.coldpod.network.data.Podcasts;
import com.android.myapplication.coldpod.ui.details.PodCastDetailActivity;
import com.android.myapplication.coldpod.ui.search.SearchActivity;
import com.android.myapplication.coldpod.utils.GridAutofitLayoutManager;

import javax.inject.Inject;

import static com.android.myapplication.coldpod.utils.Constants.GRID_AUTO_FIT_COLUMN_WIDTH;
import static com.android.myapplication.coldpod.utils.Constants.STATE_SEARCH_QUERY;

public class PodCastListActivity extends AppCompatActivity implements PodCastListAdapter.Listener {
    private static final String TAG = "PodCastListActivity";
    private ActivityPodcastListBinding mBinding;
    private PodCastListViewModel mPodCastListViewModel;
    Toolbar mToolbar;
    SpannableString mSpannableString;
    ForegroundColorSpan mForegroundColorSpan;
    private String mSearchQuery;
    private SearchView mSearchView;

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
        if (savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(STATE_SEARCH_QUERY); //get previous query
        }
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_podcast_list);
        ((BaseApplication) getApplication()).getAppComponent()
                .getMainComponent()
                .injectPodCastListActivity(this);
        mPodCastListViewModel = new ViewModelProvider(this, providerFactory).get(PodCastListViewModel.class);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(mPodCastListViewModel);
        initRV();
        initToolbar();
        mPodCastListViewModel.getPodCasts(getString(R.string.country));
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.podcast_search_menu, menu);

        // Associate searchable configuration with the SearchView
        // Reference: @see "https://developer.android.com/training/search/setup#create-sc"
        // "https://www.youtube.com/watch?v=9OWmnYPX1uc"
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // If the search query exists, set the query manually.
        if (mSearchQuery != null) {
            mSearchView.setIconified(true);
            mSearchView.onActionViewExpanded();
            mSearchView.setQuery(mSearchQuery,false);
            mSearchView.setFocusable(true);
        }
        // Display a hint text in the search text field.
        // android:hint attribute in the searchable.xml is not working.
        // Reference: @see "https://stackoverflow.com/questions/37919328/searchview-hint-not-showing"
        mSearchView.setQueryHint(getString(R.string.podcast_search_hint));

        // Set onQueryTextListener
        // Reference: @see "https://www.youtube.com/watch?v=9OWmnYPX1uc"
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Called when the user submits the query
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Perform the final search
                startActivity(SearchActivity.getInstance(PodCastListActivity.this,s));
                return true;
            }

            // Called when the query text is changed by the user
            @Override
            public boolean onQueryTextChange(String s) {
                // Called every time the text has changed, even if the user didn't submit
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Get the query string and store it to our bundle
        mSearchQuery = mSearchView.getQuery().toString();
        outState.putString(STATE_SEARCH_QUERY, mSearchQuery);
        super.onSaveInstanceState(outState);
    }


}
