package com.android.myapplication.coldpod.ui.podcast_entry;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.databinding.PodcastEntryListItemBinding;
import com.android.myapplication.coldpod.persistence.Item;

public class PodCastEntryAdapter extends ListAdapter<Item, PodCastEntryAdapter.PodCastEntryViewHolder> {


    public interface Listener{
        void onItemClick(Item item);
    }
    private Listener mListener;


    public PodCastEntryAdapter(@NonNull DiffUtil.ItemCallback<Item> diffCallback,Listener listener) {
        super(diffCallback);
        mListener = listener;
    }

    @NonNull
    @Override
    public PodCastEntryViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PodcastEntryListItemBinding binding = PodcastEntryListItemBinding.inflate(layoutInflater,parent,false);
        return new PodCastEntryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PodCastEntryViewHolder holder, int position) {
        Item item = getItem(position);
        holder.bind(item);

    }

    public class PodCastEntryViewHolder extends RecyclerView.ViewHolder {
        private final PodcastEntryListItemBinding mBinding;

        public PodCastEntryViewHolder
                (@NonNull PodcastEntryListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
        void bind(Item item) {
            mBinding.setItem(item);
            mBinding.setViewHolder(this);
            mBinding.executePendingBindings();
        }
        public void onItemClick(Item item){
            mListener.onItemClick(item);
        }

    }
}
