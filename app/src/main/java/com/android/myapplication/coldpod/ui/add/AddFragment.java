package com.android.myapplication.coldpod.ui.add;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.FragmentAddBinding;
import com.android.myapplication.coldpod.model.Podcasts;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment implements AddAdapter.Listener {
    private FragmentAddBinding mBinding;
    private AddViewModel mAddViewModel;


    private AddAdapter mAddAdapter;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    DiffUtil.ItemCallback<Podcasts> mPodcastsItemCallback;

    private Listener mListener;

    public interface Listener {
        void onItemClick(Podcasts podcasts);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getActivity().getApplication()).getAppComponent()
                .getMainComponent()
                .injectAddFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddBinding.inflate(inflater, container, false);
        mAddViewModel = new ViewModelProvider(this, providerFactory).get(AddViewModel.class);
        mBinding.setLifecycleOwner(this.getViewLifecycleOwner());
        mBinding.setViewModel(mAddViewModel);
        initRV();
        return mBinding.getRoot();
    }

    private void initRV() {
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 3);
        mBinding.rvAddPodcast.setLayoutManager(layoutManager);
        mBinding.rvAddPodcast.setHasFixedSize(true);
        mAddAdapter = new AddAdapter(mPodcastsItemCallback, this);
        mBinding.rvAddPodcast.setAdapter(mAddAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAddViewModel.getPodCasts("us");
    }

    @Override
    public void onItemClick(Podcasts podcasts) {
        if(mListener!=null){
            mListener.onItemClick(podcasts);
        }
    }
}
