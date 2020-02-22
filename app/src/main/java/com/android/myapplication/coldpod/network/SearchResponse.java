package com.android.myapplication.coldpod.network;

import com.android.myapplication.coldpod.network.data.SearchResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("results")
    private List<SearchResult> mSearchResults;

    public List<SearchResult> getSearchResults() {
        return mSearchResults;
    }

    public void setSearchResults(List<SearchResult> searchResults) {
        mSearchResults = searchResults;
    }
}
