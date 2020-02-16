package com.android.myapplication.coldpod.ui.bindingadapters;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.ui.add.AddAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import javax.inject.Inject;

public class AddBindingAdapters {

    @BindingAdapter("artwork")
    public static void bindArtWork(ImageView imageView, String artWorkUrl) {
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(RequestOptions
                        .placeholderOf(R.drawable.white_background)
                        .error(R.drawable.white_background)
                )
                .load(artWorkUrl)
                .into(imageView);
    }

    @BindingAdapter("podcasts")
    public static void bindPodcasts(RecyclerView recyclerView, List<Podcasts> podCasts) {
        AddAdapter addAdapter = (AddAdapter) recyclerView.getAdapter();
        if (podCasts != null) {
            addAdapter.submitList(podCasts);
        }
    }
}
