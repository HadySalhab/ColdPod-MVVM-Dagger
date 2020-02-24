package com.android.myapplication.coldpod.ui.bindingadapters;

import android.annotation.SuppressLint;
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
import com.android.myapplication.coldpod.network.SearchResponse;
import com.android.myapplication.coldpod.network.data.SearchResult;
import com.android.myapplication.coldpod.persistence.FavoriteEntry;
import com.android.myapplication.coldpod.persistence.PodcastEntry;
import com.android.myapplication.coldpod.network.data.Podcasts;
import com.android.myapplication.coldpod.network.data.ArtworkImage;
import com.android.myapplication.coldpod.network.data.Category;
import com.android.myapplication.coldpod.network.data.Channel;
import com.android.myapplication.coldpod.persistence.Item;
import com.android.myapplication.coldpod.network.data.ItemImage;
import com.android.myapplication.coldpod.ui.details.PodCastDetailAdapter;
import com.android.myapplication.coldpod.ui.main.favorites.FavoritesAdapter;
import com.android.myapplication.coldpod.ui.main.subscribed.SubscribedListAdapter;
import com.android.myapplication.coldpod.ui.podcast_entry.PodCastEntryAdapter;
import com.android.myapplication.coldpod.ui.podcasts.PodCastListAdapter;
import com.android.myapplication.coldpod.ui.search.SearchAdapter;
import com.android.myapplication.coldpod.utils.Constants;
import com.android.myapplication.coldpod.utils.Resource;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.android.myapplication.coldpod.utils.Constants.FORMATTED_PATTERN;
import static com.android.myapplication.coldpod.utils.Constants.IMG_HTML_TAG;
import static com.android.myapplication.coldpod.utils.Constants.PUB_DATE_PATTERN;
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
    @BindingAdapter("search_results")
    public static void bindSearchResults(RecyclerView recyclerView, List<SearchResult> searchResults) {
        SearchAdapter searchAdapter = (SearchAdapter) recyclerView.getAdapter();
        if (searchResults != null) {
            searchAdapter.submitList(searchResults);
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
    public static void bindItemImage(ImageView imageView, List<ItemImage> itemImages){

      if (itemImages != null) {
          String itemImageUrl = itemImages.get(0).getItemImageHref();
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
  @BindingAdapter("item_image_url")
  public static void bindItemImageUrl(ImageView imageView , String itemImageUrl){
      Glide.with(imageView.getContext())
              .setDefaultRequestOptions(RequestOptions
                      .placeholderOf(R.drawable.white_background)
                      .error(R.drawable.logo)
              )
              .load(itemImageUrl)
              .into(imageView);
  }

    @BindingAdapter("items")
    public static void bindItemsToRv(RecyclerView recyclerView, List<Item> items){
        PodCastEntryAdapter podCastEntryAdapter = (PodCastEntryAdapter) recyclerView.getAdapter();
        if(items!=null){
            podCastEntryAdapter.submitList(items);
        }
    }
    @BindingAdapter("favItems")
    public static void bindFavItemsToRv(RecyclerView recyclerView, List<FavoriteEntry> favItems){
        FavoritesAdapter podCastEntryAdapter = (FavoritesAdapter) recyclerView.getAdapter();
        if(favItems!=null){
            podCastEntryAdapter.submitList(favItems);
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

    @BindingAdapter("pubDate")
    public static void bindPubDate(TextView textView , String date){
        SimpleDateFormat stringToDateFormatter = new SimpleDateFormat(PUB_DATE_PATTERN, Locale.US);


        Date currentTime = null;
        try {
            currentTime = stringToDateFormatter.parse(date); //converting string to date using the PUB_DATE_PATTERN
        } catch (ParseException e) { //CATCH in case date format is not the same as PUB_DATE_PATTERN
            Timber.e("Error formatting date: " + e.getMessage());
        }
        if(currentTime!=null){
            SimpleDateFormat  dateToStringFormatter = new SimpleDateFormat(FORMATTED_PATTERN, Locale.US);
            String newDateFormatString = dateToStringFormatter.format(currentTime);
            textView.setText(newDateFormatString);
        }else{
            textView.setText(date);
        }


    }
}
