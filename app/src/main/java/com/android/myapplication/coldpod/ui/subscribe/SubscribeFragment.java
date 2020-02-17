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
import com.android.myapplication.coldpod.di.main.PodCastIdModule;
import com.android.myapplication.coldpod.ui.add.AddViewModel;
import com.android.myapplication.coldpod.utils.Resource;

import javax.inject.Inject;

import static com.android.myapplication.coldpod.utils.Constants.EXTRA_PODCAST_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment {
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
                .getMainComponent(new PodCastIdModule(this.podCastsId))
                .injectSubscribeFragment(this);
        mSubscribeViewModel = new ViewModelProvider(this,providerFactory).get(SubscribeViewModel.class);
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
        mSubscribeViewModel.getFeedUrl().observe(getViewLifecycleOwner(), new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
            }
        });
    }
}
