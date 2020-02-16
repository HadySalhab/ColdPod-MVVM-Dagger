package com.android.myapplication.coldpod.ui.diffUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.model.Podcasts;

import javax.inject.Inject;

public class PodCastsDiffUtil extends DiffUtil.ItemCallback<Podcasts> {
    @Inject
    public PodCastsDiffUtil() {
    }

    @Override
    public boolean areItemsTheSame(@NonNull Podcasts oldItem, @NonNull Podcasts newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Podcasts oldItem, @NonNull Podcasts newItem) {
        return oldItem.equals(newItem);
    }
}
