package com.android.myapplication.coldpod.ui.main.favorites;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.databinding.FragmentFavoritesBinding;
import com.android.myapplication.coldpod.databinding.FragmentSubscribedBinding;
import com.android.myapplication.coldpod.network.data.Enclosure;
import com.android.myapplication.coldpod.network.data.ItemImage;
import com.android.myapplication.coldpod.persistence.FavoriteEntry;
import com.android.myapplication.coldpod.persistence.Item;
import com.android.myapplication.coldpod.service.PodcastService;
import com.android.myapplication.coldpod.ui.main.BaseFragment;
import com.android.myapplication.coldpod.ui.main.subscribed.SubscribedListAdapter;
import com.android.myapplication.coldpod.utils.GridAutofitLayoutManager;

import java.util.List;

import static com.android.myapplication.coldpod.utils.Constants.GRID_AUTO_FIT_COLUMN_WIDTH;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends BaseFragment implements FavoritesAdapter.Listener {
    private static final String TAG = "Debug";

    private FavoritesAdapter mAdapter;
    private FragmentFavoritesBinding binding;


    public interface Listener{
        //delegate the navigation to Playing Activity and Starting the Service, to the MainActivity
        //because fragments aren't supposed to start the activity themselves.
        public void onFavoriteClick(FavoriteEntry favoriteEntry);
    }
    private Listener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (Listener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        initRV();
        return binding.getRoot();
    }


    public void initRV() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvFavorites.setLayoutManager(layoutManager);
        binding.rvFavorites.setHasFixedSize(true);
        mAdapter = new FavoritesAdapter(mFavoriteEntryItemCallback, this);
        binding.rvFavorites.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + mMainActivityViewModel);
        binding.setViewModel(mMainActivityViewModel);
    }

    @Override
    public void onFavoriteClick(FavoriteEntry favoriteEntry) {
        if(mListener!=null){
            mListener.onFavoriteClick(favoriteEntry);
        }
    }




}
