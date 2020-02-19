package com.android.myapplication.coldpod.ui.bindingadapters;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Html;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.database.PodcastEntry;
import com.android.myapplication.coldpod.model.Podcasts;
import com.android.myapplication.coldpod.network.ArtworkImage;
import com.android.myapplication.coldpod.network.Category;
import com.android.myapplication.coldpod.network.Channel;
import com.android.myapplication.coldpod.network.Item;
import com.android.myapplication.coldpod.network.ItemImage;
import com.android.myapplication.coldpod.ui.details.PodCastDetailAdapter;
import com.android.myapplication.coldpod.ui.main.subscribed.SubscribedListAdapter;
import com.android.myapplication.coldpod.ui.podcast_entry.PodCastEntryAdapter;
import com.android.myapplication.coldpod.ui.podcasts.PodCastListAdapter;
import com.android.myapplication.coldpod.utils.Resource;
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
            String artworkImageUrl = artworkImage.getImageHref();
            if (artworkImageUrl == null) {
                artworkImageUrl = artworkImages.get(1).getImageHref();
                if (artworkImageUrl == null) {
                    artworkImageUrl = artworkImage.getImageUrl();
                }
            }
            // Use Glide library to upload the artwork
            Glide.with(imageView.getContext())
                    .setDefaultRequestOptions(RequestOptions
                            .placeholderOf(R.drawable.white_background)
                            .error(R.drawable.white_background)
                    )
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

    @BindingAdapter("network_error_screen")
    public static void bindNetworkErrorScreenVisibility(View view, Resource.Status status){
        if(status == Resource.Status.ERROR){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("empty_data_screen")
    public static void bindEmptyDataScreenVisibility(View view, Resource resource){
        if(resource.status == Resource.Status.SUCCESS && resource.data==null){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility(View.GONE);
        }
    }

  @BindingAdapter("podcast_entries")
    public static void bindPodCastEntriesToRv(RecyclerView recyclerView, List<PodcastEntry> podcastEntries){
        SubscribedListAdapter subscribedListAdapter = (SubscribedListAdapter) recyclerView.getAdapter();
        if(podcastEntries!=null){
            subscribedListAdapter.submitList(podcastEntries);
        }
  }

  @BindingAdapter("item_image")
    public static void bindItemImage(ImageView imageView, ItemImage itemImage){
      if (itemImage != null) {
          String itemImageUrl = itemImage.getItemImageHref();
          // Use Glide library to upload the artwork
          Glide.with(imageView.getContext())
                  .setDefaultRequestOptions(RequestOptions
                          .placeholderOf(R.drawable.white_background)
                          .error(R.drawable.logo)
                  )
                  .load(itemImageUrl)
                  .into(imageView);
      }else{
          Drawable drawable = imageView.getContext().getDrawable(R.drawable.logo);
          Glide.with(imageView.getContext())
                  .load(drawable)
                  .into(imageView);
      }
  }

    @BindingAdapter("items")
    public static void bindItemsToRv(RecyclerView recyclerView, List<Item> items){
        PodCastEntryAdapter podCastEntryAdapter = (PodCastEntryAdapter) recyclerView.getAdapter();
        if(items!=null){
            podCastEntryAdapter.submitList(items);
        }
    }

    @BindingAdapter("empty_subscription_anim")
    public static void bindEmptySubscriptionAnimation(TextView textView ,List<PodcastEntry> podCastEntries){
        if(podCastEntries==null || podCastEntries.size() == 0){
            textView.setVisibility(View.VISIBLE);
            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(1500);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            textView.startAnimation(anim);
        }else{
            textView.setVisibility(View.GONE);
            textView.clearAnimation();
        }
    }

}
