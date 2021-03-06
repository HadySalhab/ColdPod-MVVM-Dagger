package com.android.myapplication.coldpod.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.persistence.FavoriteEntry;
import com.android.myapplication.coldpod.persistence.PodcastEntry;

import javax.inject.Inject;

public class BaseFragment extends Fragment {
    private static final String TAG = "Debug";

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    protected DiffUtil.ItemCallback<PodcastEntry> mPodcastEntryItemCallback;

    @Inject
    protected DiffUtil.ItemCallback<FavoriteEntry> mFavoriteEntryItemCallback;

    protected MainActivityViewModel mMainActivityViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity()
                .getApplication())
                .getAppComponent()
                .getMainComponent()
                .injectBaseFragment(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            mMainActivityViewModel = new ViewModelProvider(getActivity(), providerFactory).get(MainActivityViewModel.class);
        } else {
            try {
                throw new Exception("Activity Not Available");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
