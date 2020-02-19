package com.android.myapplication.coldpod.ui.main.favorites;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ui.main.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends BaseFragment {
    private static final String TAG = "Debug";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: "+ mMainActivityViewModel);
    }
}
