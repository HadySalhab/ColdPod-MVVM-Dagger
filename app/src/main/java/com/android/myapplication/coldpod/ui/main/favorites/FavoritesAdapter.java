package com.android.myapplication.coldpod.ui.main.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.databinding.FavoriteListItemBinding;
import com.android.myapplication.coldpod.persistence.FavoriteEntry;

import java.util.List;

public class FavoritesAdapter extends ListAdapter<FavoriteEntry, FavoritesAdapter.FavoritesViewHolder> {


    protected FavoritesAdapter(@NonNull DiffUtil.ItemCallback<FavoriteEntry> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FavoriteListItemBinding binding = FavoriteListItemBinding.inflate(layoutInflater, parent, false);
        return new FavoritesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        FavoriteEntry favoriteEntry = getItem(position);
        holder.bind(favoriteEntry);
    }

    public class FavoritesViewHolder extends RecyclerView.ViewHolder {
        private FavoriteListItemBinding mBinding;

        public FavoritesViewHolder(@NonNull FavoriteListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(FavoriteEntry favoriteEntry) {
            mBinding.setFavItem(favoriteEntry);
        }
    }
}