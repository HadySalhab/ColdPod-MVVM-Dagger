package com.android.myapplication.coldpod.ui.bindingadapters;

import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.network.ArtworkImage;
import com.android.myapplication.coldpod.network.Category;
import com.android.myapplication.coldpod.network.Channel;
import com.android.myapplication.coldpod.network.Item;
import com.android.myapplication.coldpod.ui.details.PodCastDetailAdapter;
import com.android.myapplication.coldpod.ui.podcasts.PodCastListAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

import static com.android.myapplication.coldpod.utils.Constants.IMG_HTML_TAG;
import static com.android.myapplication.coldpod.utils.Constants.REPLACEMENT_EMPTY;

public class BindingAdapters {

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

    @BindingAdapter("list_artwork")
    public static void bindArtWorkImage(ImageView imageView, Channel channel) {
        if (channel != null) {
            List<ArtworkImage> artworkImages = channel.getArtworkImages();
            ArtworkImage artworkImage = artworkImages.get(0);
            String artworkImageUrl = artworkImage.getImageUrl();
            if (artworkImageUrl == null) {
                artworkImageUrl = artworkImage.getImageHref();
            }
            // Use Glide library to upload the artwork
            Glide.with(imageView.getContext())
                    .load(artworkImageUrl)
                    .into(imageView);
        }
    }

    @BindingAdapter("categories")
    public static void bindCategories(TextView textView, List<Category> categories) {
        if (categories != null) {
            for (Category category : categories) {
                String categoryText = category.getText();
                if (categoryText != null) {
                    textView.append(categoryText + "  ");
                }
            }
        }

    }


    @BindingAdapter("podcasts")
    public static void bindPodcasts(RecyclerView recyclerView, List<Podcasts> podCasts) {
        PodCastListAdapter podCastListAdapter = (PodCastListAdapter) recyclerView.getAdapter();
        if (podCasts != null) {
            podCastListAdapter.submitList(podCasts);
        }
    }

    @BindingAdapter("podcasts_details")
    public static void bindPodcastDetails(RecyclerView recyclerView, List<Item> items) {
        PodCastDetailAdapter podCastDetailAdapter = (PodCastDetailAdapter) recyclerView.getAdapter();
        if (items != null) {
            podCastDetailAdapter.submitList(items);
        }
    }

    @BindingAdapter("item_description")
    public static void bindItemDescription(TextView textView, String description) {
        if (description != null) {
            String descriptionWithoutImageTag = description.replaceAll(IMG_HTML_TAG, REPLACEMENT_EMPTY);
            textView.setText(Html.fromHtml(Html.fromHtml(descriptionWithoutImageTag).toString()));
        }
    }
}
