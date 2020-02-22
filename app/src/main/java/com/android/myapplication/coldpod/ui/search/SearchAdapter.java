package com.android.myapplication.coldpod.ui.search;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.databinding.SearchListItemBinding;
import com.android.myapplication.coldpod.network.data.SearchResult;

public class SearchAdapter extends ListAdapter<SearchResult, SearchAdapter.SearchViewHolder> {
    private static final String TAG = "SearchAdapter";

    public interface Listener {
        void onItemClick(SearchResult item);
    }

    private final Listener mListener;

    public SearchAdapter(@NonNull DiffUtil.ItemCallback<SearchResult> diffCallback, Listener listener) {
        super(diffCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SearchListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.search_list_item, parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchResult item = getItem(position);
        holder.bind(item);
    }

    public class SearchViewHolder
            extends RecyclerView.ViewHolder {
        SearchListItemBinding binding;

        SearchViewHolder
                (@NonNull SearchListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SearchResult item) {
            //bind item with view
            // binding.setItem(item);
            binding.setSearchResult(item);
            binding.setViewHolder(this);
            binding.executePendingBindings();
        }

        public void onItemClick(SearchResult item) {
            Log.d(TAG, "onItemClick: ");
            mListener.onItemClick(item);
        }


    }
}