package com.android.myapplication.coldpod.ui.details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.databinding.PodcastDetailItemBinding;
import com.android.myapplication.coldpod.persistence.Item;

public class PodCastDetailAdapter extends ListAdapter<Item, PodCastDetailAdapter.PodCastDetailViewHolder> {


    public PodCastDetailAdapter(@NonNull DiffUtil.ItemCallback<Item> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PodCastDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        PodcastDetailItemBinding binding = PodcastDetailItemBinding.inflate(layoutInflater,parent,false);
        return new PodCastDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PodCastDetailViewHolder holder, int position) {
        Item item = getItem(position);
        holder.bind(item);

    }

    public class PodCastDetailViewHolder
            extends RecyclerView.ViewHolder {
        private final PodcastDetailItemBinding mBinding;

        public PodCastDetailViewHolder(@NonNull PodcastDetailItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
        void bind(Item item) {
            mBinding.setItem(item);
            mBinding.setViewHolder(this);
            mBinding.executePendingBindings();
        }
    }
}
