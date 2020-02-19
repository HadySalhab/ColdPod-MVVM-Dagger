package com.android.myapplication.coldpod.ui.main.subscribed;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.database.PodcastEntry;
import com.android.myapplication.coldpod.databinding.SubscribedPodcastsListItemBinding;
import com.android.myapplication.coldpod.model.Podcasts;

public class SubscribedListAdapter extends ListAdapter<PodcastEntry,SubscribedListAdapter.SubscribedListViewHolder>{
    private static final String TAG = "SubscribedListAdapter";

    interface Listener {
        void onItemClick(PodcastEntry podcastEntry);
    }

    private final SubscribedListAdapter.Listener mListener;


    protected SubscribedListAdapter(@NonNull DiffUtil.ItemCallback<PodcastEntry> diffCallback, Listener listener) {
        super(diffCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public SubscribedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SubscribedPodcastsListItemBinding binding = SubscribedPodcastsListItemBinding.inflate(layoutInflater,parent,false);
        return new SubscribedListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribedListViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        PodcastEntry podcastEntry = getItem(position);
        holder.bind(podcastEntry);
    }

    public  class SubscribedListViewHolder extends RecyclerView.ViewHolder {
        private final SubscribedPodcastsListItemBinding binding;
        public SubscribedListViewHolder(@NonNull SubscribedPodcastsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(PodcastEntry podcastEntry){
            Log.d(TAG, "bind: "+podcastEntry);
            binding.setPodcastEntry(podcastEntry);
            binding.setViewHolder(this);
            binding.executePendingBindings();
        }
        public void onItemClick(PodcastEntry podcastEntry){
            mListener.onItemClick(podcastEntry);
        }
    }
}

