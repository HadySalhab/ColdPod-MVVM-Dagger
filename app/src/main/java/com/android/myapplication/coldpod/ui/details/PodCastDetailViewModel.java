package com.android.myapplication.coldpod.ui.details;

import android.util.Log;
import android.view.View;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.myapplication.coldpod.persistence.PodcastEntry;
import com.android.myapplication.coldpod.network.data.ArtworkImage;
import com.android.myapplication.coldpod.network.data.Channel;
import com.android.myapplication.coldpod.repository.PodCastDetailRepository;
import com.android.myapplication.coldpod.utils.AbsentLiveData;
import com.android.myapplication.coldpod.utils.Constants;
import com.android.myapplication.coldpod.utils.Resource;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;


public class PodCastDetailViewModel extends ViewModel {


    private boolean isSubscribed = false; //default value
    private static final String TAG = "PodCastDetailViewModel";
    private final PodCastDetailRepository repository;
    private LiveData<PodcastEntry> dbPodCast;
    public LiveData<String> subscriptionButtonText;

    /*
     * when changed will fire the requests...
     * */
    private final MutableLiveData<String> _podcastId = new MutableLiveData<>();


    /*
     * will handle the progress bar visibility
     * */
    private MediatorLiveData<Integer> progress = new MediatorLiveData<>();

    public LiveData<Integer> getProgress() {
        return progress;
    }

    private MediatorLiveData<Integer> networkError = new MediatorLiveData<>();

    public LiveData<Integer> getNetworkError() {
        return networkError;
    }

    /*
     * get rssFeed Url when id is given...
     * */
    private final LiveData<Resource<String>> feedURL = Transformations.switchMap(_podcastId, new Function<String, LiveData<Resource<String>>>() {
        @Override
        public LiveData<Resource<String>> apply(String input) {
            Log.d(TAG, "apply: " + input);
            return repository.getLookUp(Constants.I_TUNES_LOOKUP, input);
        }
    });

    /*
     * get podCast when rssFeed Url is ready...
     * */
    public final LiveData<Resource<Channel>> mResourceChannel = Transformations.switchMap(feedURL, new Function<Resource<String>, LiveData<Resource<Channel>>>() {
        @Override
        public LiveData<Resource<Channel>> apply(Resource<String> input) {
            if(input!=null) {
                if (input.status == Resource.Status.SUCCESS && input.data != null) {
                    Log.d(TAG, "apply: " + input.data);
                    return repository.getRssfeed(input.data);
                }
            }
            return AbsentLiveData.create();
        }
    });

    /*
     * Progress will be displayed to getFeedUrl request
     * when feedUrl is given , register to getPodCast request...
     * */
    private void checkLoadingStatus() {
        progress.addSource(feedURL, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                if (resource != null) {
                    Log.d(TAG, "feedUrl !=NULL");
                    //in networkBoundResource status is always loading until we reach success or error state
                    progress.setValue(View.VISIBLE);
                    //When its not loading anymore, it means either success or error,
                    //if error, it means that we weren't able to retrieve the feedURl
                    //which will lead to absent livedata value for channelResource-> which will hide the loading status
                    //if success, means we retrieved the feedURl, but we still need to track the channel request
                    if (resource.status != Resource.Status.LOADING) {
                        Log.d(TAG, "feedUrl NOT LOADING");
                        progress.removeSource(feedURL);
                        keepCheckingLoadingStatus();
                    }
                } else {
                    Log.d(TAG, "feedUrl == LOADING");

                    progress.removeSource(feedURL);
                    progress.setValue(View.GONE);
                }
            }
        });
    }




    /*
     * FeedUrl is ready at this moment...
     * keep loading until we get the podCasts from the api...
     * */
    private void keepCheckingLoadingStatus() {
        progress.addSource(mResourceChannel, new Observer<Resource<Channel>>() {
            @Override
            public void onChanged(Resource<Channel> channelResource) {
                //channel can be absent (null)  if input == null || input.status != Resource.Status.SUCCESS || input.data == null
                if (channelResource != null) {
                    //at this stage, we have the feedUrl , so we can track the request of channel
                    Log.d(TAG, "channelResource != NULL");
                    //progress.setValue(View.VISIBLE);  <-- no need, since at the moment progress bar will be visible when we tracked feedUrl
                    //if Success or Error is reached ->hide the loading status
                    if (channelResource.status != Resource.Status.LOADING) {
                        Log.d(TAG, "channelResource IS  LOADING");
                        progress.removeSource(mResourceChannel); // no need the source anymore since we reached success or loading state
                        progress.setValue(View.GONE);
                    }
                }
            }
        });
    }



    private void checkNetworkState(){
        networkError.addSource(feedURL, new Observer<Resource<String>>() {
            @Override
            public void onChanged(Resource<String> resource) {
                if (resource != null) {
                    networkError.setValue(View.GONE); //when we start the request we want it to be gone
                    if(resource.status == Resource.Status.ERROR){
                        networkError.removeSource(feedURL);
                        networkError.setValue(View.VISIBLE);
                        Log.d(TAG, "feedUrl : status error");
                    }else{
                        // loading or success
                        //for loading we dont want to do anything , it would  have visibility gone anyway
                        //but when we reach success we have to track channel request
                        if(resource.status == Resource.Status.SUCCESS){
                            networkError.removeSource(feedURL);
                            Log.d(TAG, "feedUrl : status success");

                            trackChannelRequestNetworkState();
                        }
                    }
                } else {
                    networkError.removeSource(feedURL);
                    networkError.setValue(View.VISIBLE);
                    Log.d(TAG, "feedUrl : = null");
                }
            }
        });
    }

    private void trackChannelRequestNetworkState(){
        networkError.addSource(mResourceChannel, new Observer<Resource<Channel>>() {
            @Override
            public void onChanged(Resource<Channel> channelResource) {
                if(channelResource !=null){
                    if(channelResource.status == Resource.Status.ERROR){
                        networkError.removeSource(mResourceChannel);
                        networkError.setValue(View.VISIBLE);
                        Log.d(TAG, "mResourceChannel : status error"+channelResource.message);
                    }else if (channelResource.status == Resource.Status.SUCCESS){
                        networkError.removeSource(mResourceChannel); //visibility is Gone by default
                    }
                }
            }
        });
    }



    /*
     * Constructor...
     * */
    @Inject
    public PodCastDetailViewModel(PodCastDetailRepository repository) {
        this.repository = repository;
    }


    /*
     *The id will be given from the activity...
     * it will trigger required events...
     * will check loading status
     *
     * we check if the podCast is available in db (available = subscribed podCast , Not availabe = podCast is not subscribed)
     * we update the btn text
     *
     * */
    public void setPodCastId(String podCastId) {
        Log.d(TAG, "setPodCastId: " + podCastId);
        _podcastId.setValue(podCastId);
        checkLoadingStatus();
        checkNetworkState();

        dbPodCast = repository.getPodcastByPodcastId(podCastId);
        checkSubscription();

    }

    public void checkSubscription() {
        subscriptionButtonText = Transformations.map(dbPodCast, new Function<PodcastEntry, String>() {
            @Override
            public String apply(PodcastEntry input) {
                if (input == null) {
                    isSubscribed = false;
                    return "Subscribe";
                } else {
                    isSubscribed = true;
                    return "Unsubscribe";
                }
            }
        });

    }


    public void onSubscribeClicked() {
        Log.d(TAG, "onSubscribeClicked: ");
        PodcastEntry podcastEntry = getPodCastFromNetworkSource();
        Log.d(TAG, "onSubscribeClicked: " + podcastEntry);
        //network data is the source of truth
        //if the network data is unavailable, we dont want to allow the user to even click on the button
        if (podcastEntry != null) {
            if (!isSubscribed) {
                repository.insertPodcast(podcastEntry);
                Log.d(TAG, "onSubscribeClicked: " + podcastEntry);
            } else {
                podcastEntry = dbPodCast.getValue();
                repository.remove(podcastEntry);
            }
        }
    }


    private PodcastEntry getPodCastFromNetworkSource() {
        if (mResourceChannel.getValue() != null && mResourceChannel.getValue().status == Resource.Status.SUCCESS && mResourceChannel.getValue().data != null) {
            Channel receivedChannel = mResourceChannel.getValue().data;
            List<ArtworkImage> artworkImage = receivedChannel.getArtworkImages();
            ArtworkImage image = artworkImage.get(0);
            String artworkImageUrl = image.getImageUrl();
            if (artworkImageUrl == null) {
                artworkImageUrl = image.getImageHref();
            }
            return new PodcastEntry(
                    _podcastId.getValue(),
                    receivedChannel.getTitle(),
                    receivedChannel.getDescription(),
                    receivedChannel.getITunesAuthor(),
                    artworkImageUrl,
                    receivedChannel.getItemList(),
                    new Date()
            );

        } else {
            return null;
        }
    }
}
