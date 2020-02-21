package com.android.myapplication.coldpod.ui.podcasts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.databinding.PodcastListItemBinding;
import com.android.myapplication.coldpod.network.data.Podcasts;

public class PodCastListAdapter extends ListAdapter<Podcasts, PodCastListAdapter.PodcastListViewHolder
        > {

    private static final String TAG = "PodCastListAdapter";
    interface Listener {
        void onItemClick(Podcasts podcasts);
    }

    private final PodCastListAdapter.Listener mListener;


    public PodCastListAdapter(@NonNull DiffUtil.ItemCallback<Podcasts> diffCallback, Listener listener) {
        super(diffCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public PodcastListViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PodcastListItemBinding binding = DataBindingUtil
                .inflate(layoutInflater, R.layout.podcast_list_item, parent, false);
        return new PodcastListViewHolder
                (binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastListViewHolder
                                             holder, int position) {
        Podcasts podcasts = getItem(position);
        holder.bind(podcasts);
    }

    public class PodcastListViewHolder
            extends RecyclerView.ViewHolder {
        PodcastListItemBinding binding;

        PodcastListViewHolder
                (@NonNull PodcastListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Podcasts podcasts) {
            binding.setPodCast(podcasts);
            binding.setViewHolder(this);
            binding.executePendingBindings();
        }

        public void onItemClick(Podcasts podcasts) {
            Log.d(TAG, "onItemClick: ");
            mListener.onItemClick(podcasts);
        }


    }
}
