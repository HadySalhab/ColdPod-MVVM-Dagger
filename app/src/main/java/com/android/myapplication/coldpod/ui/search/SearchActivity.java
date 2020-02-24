package com.android.myapplication.coldpod.ui.search;

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
import android.view.MenuItem;
import android.widget.Toast;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivitySearchBinding;
import com.android.myapplication.coldpod.network.data.Podcasts;
import com.android.myapplication.coldpod.network.data.SearchResult;
import com.android.myapplication.coldpod.ui.details.PodCastDetailActivity;
import com.android.myapplication.coldpod.ui.podcasts.PodCastListActivity;
import com.android.myapplication.coldpod.utils.GridAutofitLayoutManager;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.android.myapplication.coldpod.utils.Constants.GRID_AUTO_FIT_COLUMN_WIDTH;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.Listener {
    private ActivitySearchBinding mSearchBinding;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    DiffUtil.ItemCallback<SearchResult> mSearchResultItemCallback;

    Toolbar mToolbar;
    SpannableString mSpannableString;
    ForegroundColorSpan mForegroundColorSpan;



    private SearchViewModel mSearchViewModel;
    private String mQuery;
    private SearchAdapter mSearchAdapter;

    public static Intent getInstance(Context context, String query) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.setAction(Intent.ACTION_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDagger();
        mSearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        mSearchBinding.setLifecycleOwner(this);
        initToolbar();
        initRV();
        handleIntent(getIntent());
        setupViewModel();
    }
    private void initToolbar() {
        mToolbar = mSearchBinding.toolbar;
        mSpannableString = new SpannableString(getString(R.string.app_name));
        mForegroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, (R.color.primary_color)));
        mSpannableString.setSpan(mForegroundColorSpan, 4, 7, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        this.setSupportActionBar(mToolbar);
        ((AppCompatActivity) this).getSupportActionBar().setTitle(mSpannableString);
        ((AppCompatActivity) this).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initDagger() {
        ((BaseApplication) getApplication()).getAppComponent().getMainComponent().injectSearchActivity(this);
    }

    /**
     * Create a LinearLayoutManager and SearchAdapter, and set them to the RecyclerView.
     */
    private void initRV() {

        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(
                this, GRID_AUTO_FIT_COLUMN_WIDTH);
        mSearchBinding.rvSearchResults.setLayoutManager(layoutManager);
        mSearchBinding.rvSearchResults.setHasFixedSize(true);
        mSearchAdapter = new SearchAdapter(mSearchResultItemCallback, this);
        mSearchBinding.rvSearchResults.setAdapter(mSearchAdapter);
    }

    private void setupViewModel() {
        mSearchViewModel = new ViewModelProvider(this, providerFactory).get(SearchViewModel.class);
        mSearchViewModel.setQueries(mQuery);
        mSearchBinding.setViewModel(mSearchViewModel);
    }


    /**
     * : If your searchable activity launches in single top mode (android:launchMode="singleTop"),
     * also handle the ACTION_SEARCH intent in the onNewIntent() method.
     * In single top mode, only one instance of your activity is created and subsequent calls to start your activity do not create a new activity on the stack.
     * This launch mode is useful so users can perform searches from the same activity without creating a new activity instance every time.
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            mQuery = query;
        }
    }

    @Override
    public void onItemClick(SearchResult item) {
        //we should navigate to the podcast details (where the user can see all the episodes and
        // be provided with the option to subscribe

        Intent podcastDetailIntent = PodCastDetailActivity.getInstance(this,String.valueOf(item.getCollectionId()),item.getCollectionName());
        startActivity(podcastDetailIntent);
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
}
