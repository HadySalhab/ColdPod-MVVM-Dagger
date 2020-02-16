package com.android.myapplication.coldpod.ui.add;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.FragmentAddBinding;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
 private FragmentAddBinding mBinding;
 private AddViewModel mAddViewModel;

 @Inject
 AddAdapter mAddAdapter;

 @Inject
 ViewModelProviderFactory providerFactory;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication)getActivity().getApplication()).getAppComponent()
                .getMainComponent()
                .injectAddFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddBinding.inflate(inflater,container,false);
        mAddViewModel = new ViewModelProvider(this,providerFactory).get(AddViewModel.class);
        mBinding.setLifecycleOwner(this.getViewLifecycleOwner());
        mBinding.setViewModel(mAddViewModel);
        initRV();
        return mBinding.getRoot();
    }
    private void initRV(){
        GridLayoutManager layoutManager = new GridLayoutManager(this.getActivity(), 3);
        mBinding.rvAddPodcast.setLayoutManager(layoutManager);
        mBinding.rvAddPodcast.setHasFixedSize(true);
        mBinding.rvAddPodcast.setAdapter(mAddAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAddViewModel.getPodCasts("us");
    }
}
