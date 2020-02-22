package com.android.myapplication.coldpod.ui.main.favorites;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.persistence.FavoriteEntry;

import javax.inject.Inject;

public class FavoritesDiffUtil extends DiffUtil.ItemCallback<FavoriteEntry> {

    @Inject
    public FavoritesDiffUtil() {
    }

    @Override
    public boolean areItemsTheSame(@NonNull FavoriteEntry oldItem, @NonNull FavoriteEntry newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull FavoriteEntry oldItem, @NonNull FavoriteEntry newItem) {
        return oldItem.equals(newItem);
    }
}
