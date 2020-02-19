package com.android.myapplication.coldpod.ui.main.subscribed;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.android.myapplication.coldpod.database.PodcastEntry;
import com.android.myapplication.coldpod.databinding.FragmentSubscribedBinding;
import com.android.myapplication.coldpod.ui.main.BaseFragment;
import com.android.myapplication.coldpod.utils.GridAutofitLayoutManager;

import java.util.List;

import static com.android.myapplication.coldpod.utils.Constants.GRID_AUTO_FIT_COLUMN_WIDTH;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribedFragment extends BaseFragment implements SubscribedListAdapter.Listener {
    private static final String TAG = "Debug";
    private SubscribedListAdapter mAdapter;
    private FragmentSubscribedBinding binding;

    private Listener mListener;

    public interface Listener {
        public void onPodCastEntryClicked(PodcastEntry podcastEntry);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = ((Listener) context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSubscribedBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        initRV();
        return binding.getRoot();
    }


    public void initRV() {
        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(
                getContext(), GRID_AUTO_FIT_COLUMN_WIDTH);
        binding.rvSubscribedPodcasts.setLayoutManager(layoutManager);
        binding.rvSubscribedPodcasts.setHasFixedSize(true);

        mAdapter = new SubscribedListAdapter(mPodcastEntryItemCallback, this);

        binding.rvSubscribedPodcasts.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //because at this callback the viewModel is initialized in the BaseFragment Class
        binding.setViewModel(mMainActivityViewModel);
    }

    @Override
    public void onItemClick(PodcastEntry podcastEntry) {
        if (mListener != null) {
            mListener.onPodCastEntryClicked(podcastEntry);
        }
    }


}
