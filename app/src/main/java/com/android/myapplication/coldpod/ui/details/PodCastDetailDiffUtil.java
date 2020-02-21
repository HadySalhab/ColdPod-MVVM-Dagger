package com.android.myapplication.coldpod.ui.details;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.persistence.Item;

import javax.inject.Inject;

public class PodCastDetailDiffUtil extends DiffUtil.ItemCallback<Item>{


    @Inject
    public PodCastDetailDiffUtil() {
    }

    @Override
    public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
        return oldItem.equals(newItem);
    }


}
