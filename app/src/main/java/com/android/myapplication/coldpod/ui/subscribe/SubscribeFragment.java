package com.android.myapplication.coldpod.ui.subscribe;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.network.Category;
import com.android.myapplication.coldpod.network.Channel;
import com.android.myapplication.coldpod.network.Enclosure;
import com.android.myapplication.coldpod.network.Item;
import com.android.myapplication.coldpod.network.RssFeed;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment {
    private static final String TAG = "SubscribeFragment";
    private String podCastsId;
    private SubscribeViewModel mSubscribeViewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    public static Fragment getInstance(String podCastId) {
        Bundle args = new Bundle();
        args.putString(EXTRA_PODCAST_ID, podCastId);
        Fragment fragment = new SubscribeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.podCastsId = args.getString(EXTRA_PODCAST_ID);
        ((BaseApplication) getActivity().getApplication()).getAppComponent()
                .getMainComponent()
                .injectSubscribeFragment(this);
        mSubscribeViewModel = new ViewModelProvider(this, providerFactory).get(SubscribeViewModel.class);
        mSubscribeViewModel.setPodCastId(this.podCastsId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscribe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSubscribeViewModel.rssFeed.observe(getViewLifecycleOwner(), new Observer<Resource<RssFeed>>() {
            @Override
            public void onChanged(Resource<RssFeed> rssFeedResource) {
                if (rssFeedResource != null
                        && rssFeedResource.status == Resource.Status.SUCCESS
                        && rssFeedResource.data != null) {

                    RssFeed rssFeed = rssFeedResource.data;
                    Channel channel = rssFeed.getChannel();
                    String title = channel.getTitle();
                    Timber.d("title: " + title);
                    String description = channel.getDescription();
                    Timber.d("description: " + description);
                    String author = channel.getITunesAuthor();
                    Timber.d("author: " + author);
                    String language = channel.getLanguage();
                    Timber.d("language: " + language);
                    List<Category> categories = channel.getCategories();
                    String categoryText = categories.get(0).getText();
                    Timber.d("categoryText: " + categoryText);
                    List<Item> itemList = channel.getItemList();
                    Enclosure enclosure = itemList.get(0).getEnclosure();
                    String type = enclosure.getType();
                    Timber.d("type: " + type);
                    String enclosureUrl = enclosure.getUrl();
                    Timber.d("enclosure: " + enclosureUrl);
                }
            }
        });

    }
}
