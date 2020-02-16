package com.android.myapplication.coldpod.ui.add;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.databinding.AddPodcastListItemBinding;
import com.android.myapplication.coldpod.model.Podcasts;

import javax.inject.Inject;

public class AddAdapter extends ListAdapter<Podcasts, AddAdapter.AddViewHolder> {

    private static final String TAG = "AddAdapter";
    interface Listener {
        void onItemClick(Podcasts podcasts);
    }

    private final AddAdapter.Listener mListener;


    public AddAdapter(@NonNull DiffUtil.ItemCallback<Podcasts> diffCallback, Listener listener) {
        super(diffCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public AddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AddPodcastListItemBinding binding = DataBindingUtil
                .inflate(layoutInflater, R.layout.add_podcast_list_item, parent, false);
        return new AddViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddViewHolder holder, int position) {
        Podcasts podcasts = getItem(position);
        holder.bind(podcasts);
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {
        AddPodcastListItemBinding binding;

        AddViewHolder(@NonNull AddPodcastListItemBinding binding) {
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
