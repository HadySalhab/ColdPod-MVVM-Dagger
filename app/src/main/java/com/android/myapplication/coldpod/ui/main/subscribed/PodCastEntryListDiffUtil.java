package com.android.myapplication.coldpod.ui.main.subscribed;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.database.PodcastEntry;

import javax.inject.Inject;

public class PodCastEntryListDiffUtil extends DiffUtil.ItemCallback<PodcastEntry> {

    @Inject
    public PodCastEntryListDiffUtil() {
    }

    @Override
    public boolean areItemsTheSame(@NonNull PodcastEntry oldItem, @NonNull PodcastEntry newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull PodcastEntry oldItem, @NonNull PodcastEntry newItem) {
        return oldItem.equals(newItem);
    }
}
