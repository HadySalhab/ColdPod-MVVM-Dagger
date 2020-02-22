package com.android.myapplication.coldpod.ui.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.network.data.Podcasts;
import com.android.myapplication.coldpod.network.data.SearchResult;

import javax.inject.Inject;

public class SearchResultDiffUtil extends DiffUtil.ItemCallback<SearchResult> {
    @Inject
    public SearchResultDiffUtil() {
    }

    @Override
    public boolean areItemsTheSame(@NonNull SearchResult oldItem, @NonNull SearchResult newItem) {
        return oldItem.getFeedUrl().equals(newItem.getFeedUrl());
    }

    @Override
    public boolean areContentsTheSame(@NonNull SearchResult oldItem, @NonNull SearchResult newItem) {
        return oldItem.equals(newItem);
    }
}
