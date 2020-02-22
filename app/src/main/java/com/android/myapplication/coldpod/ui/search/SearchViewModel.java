package com.android.myapplication.coldpod.ui.search;

import android.view.View;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.network.SearchResponse;
import com.android.myapplication.coldpod.network.data.Podcasts;
import com.android.myapplication.coldpod.network.data.SearchResult;
import com.android.myapplication.coldpod.repository.PodCastsRepository;
import com.android.myapplication.coldpod.utils.Constants;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.List;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {
 private final PodCastsRepository mRepository;


    private MutableLiveData<String> _query = new MutableLiveData<>();
    public LiveData<Resource<List<SearchResult>>> results = Transformations.switchMap(_query, new Function<String, LiveData<Resource<List<SearchResult>>>>() {
        @Override
        public LiveData<Resource<List<SearchResult>>> apply(String input) {
            return mRepository.getResults(Constants.I_TUNES_SEARCH,"us",Constants.SEARCH_MEDIA_PODCAST,input);
        }
    });
    public LiveData<Integer> progress = Transformations.map(results, new Function<Resource<List<SearchResult>>, Integer>() {
        @Override
        public Integer apply(Resource<List<SearchResult>> input) {
            if(input!=null){
                if(input.status== Resource.Status.LOADING){
                    return View.VISIBLE;
                }else{
                    return View.GONE;
                }
            }else{
                return View.GONE;
            }
        }
    });

    @Inject
    public SearchViewModel(PodCastsRepository repository) {
        mRepository = repository;
    }

    public void setQueries(String query){
        _query.setValue(query);
    }
}
