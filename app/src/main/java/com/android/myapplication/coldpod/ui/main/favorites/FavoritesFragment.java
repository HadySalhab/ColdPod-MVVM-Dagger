package com.android.myapplication.coldpod.ui.main.favorites;


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
import com.android.myapplication.coldpod.ui.main.BaseFragment;
import com.android.myapplication.coldpod.ui.main.subscribed.SubscribedListAdapter;
import com.android.myapplication.coldpod.utils.GridAutofitLayoutManager;

import static com.android.myapplication.coldpod.utils.Constants.GRID_AUTO_FIT_COLUMN_WIDTH;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends BaseFragment {
    private static final String TAG = "Debug";

    private FavoritesAdapter mAdapter;
    private FragmentFavoritesBinding binding;


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
        mAdapter = new FavoritesAdapter(mFavoriteEntryItemCallback);
        binding.rvFavorites.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + mMainActivityViewModel);
        binding.setViewModel(mMainActivityViewModel);
    }
}
