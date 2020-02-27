package com.android.myapplication.coldpod.ui.playing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myapplication.coldpod.BaseApplication;
import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ViewModelProviderFactory;
import com.android.myapplication.coldpod.databinding.ActivityPlayingBinding;
import com.android.myapplication.coldpod.persistence.FavoriteEntry;
import com.android.myapplication.coldpod.persistence.Item;
import com.android.myapplication.coldpod.service.PodcastService;
import com.android.myapplication.coldpod.ui.details.PodCastDetailActivity;
import com.android.myapplication.coldpod.ui.main.MainActivity;
import com.android.myapplication.coldpod.utils.Constants;
import com.google.android.material.snackbar.Snackbar;


import java.nio.file.attribute.FileAttributeView;

import javax.inject.Inject;

import timber.log.Timber;

import static com.android.myapplication.coldpod.utils.Constants.SHARE_INTENT_TYPE_TEXT;
import static com.android.myapplication.coldpod.utils.Constants.TYPE_AUDIO;

public class PlayingActivity extends AppCompatActivity {
    private static final String TAG = "PlayingActivity";


    private Item mItem;

    /**
     * The MediaBrowser connects to a MediaBrowserService, and upon connecting it creates the
     * MediaController for the UI
     */
    private MediaBrowserCompat mMediaBrowser;

    /**
     * This field is used for data binding
     */
    private ActivityPlayingBinding mBinding;

    private String mPodCastId;

    private PlayingViewModel mViewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    private FavoriteEntry mFavoriteEntry;
    private String mPodcastName;
    private String mPodcastImage;
    private String mPodcastId;

    public static Intent getInstance(Context context, Item item, String podId, String podImage, String podName) {
        Intent intent = new Intent(context, PlayingActivity.class);
        intent.putExtra(Constants.EXTRA_ITEM, item);
        intent.putExtra(Constants.EXTRA_PODCAST_ID, podId);
        intent.putExtra(Constants.EXTRA_PODCAST_IMAGE, podImage);
        intent.putExtra(Constants.EXTRA_PODCAST_NAME, podName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_playing);
        initFields();
        mBinding.setItem(mItem);
        Log.d(TAG, "playing activity: onCreate...");

        initFavoriteEntry();
        initDagger();

        initViewModel();

        // Create MediaBrowserCompat
        createMediaBrowserCompat();


        initToolbar();
        handleEnclosureType();
    }
    private void handleEnclosureType() {
        String enclosureType = mItem.getEnclosures().get(0).getType();
        if (!enclosureType.equals(TYPE_AUDIO)) {
            String snackMessage = getString(R.string.snackbar_support_audio);
            Snackbar snackbar = Snackbar.make(mBinding.coordinator, snackMessage, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    private void initFields() {
        mItem = getIntent().getParcelableExtra(Constants.EXTRA_ITEM);
        mPodCastId = getIntent().getStringExtra(Constants.EXTRA_PODCAST_ID);
        mPodcastImage = getIntent().getStringExtra(Constants.EXTRA_PODCAST_IMAGE);
        mPodcastName = getIntent().getStringExtra(Constants.EXTRA_PODCAST_NAME);
    }

    private void initFavoriteEntry() {
        mFavoriteEntry = new FavoriteEntry(mPodcastId,mPodcastName,mPodcastImage,
                mItem.getTitle(),
                mItem.getDescription(),
                mItem.getPubDate(),
                mItem.getITunesDuration(),
                mItem.getEnclosures().get(0).getUrl(),
                mItem.getEnclosures().get(0).getType(),
                mItem.getEnclosures().get(0).getLength(),
                mItem.getItemImages()==null?"":mItem.getItemImages().get(0).getItemImageHref());
    }

    private void initDagger() {
        ((BaseApplication) getApplication()).getAppComponent().getMainComponent().injectPlayingActivity(this);
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this, providerFactory).get(PlayingViewModel.class);
        mViewModel.setEnclosureUrl(mItem.getEnclosures().get(0).getUrl());
    }


    private void initToolbar() {
        // Set the toolbar as the app bar
        setSupportActionBar(mBinding.playingToolbar);


        ActionBar actionBar = getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.playing_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // When the user press the up button, finishes this NowPlayingActivity
                onBackPressed();
                return true;
            case R.id.action_favorite:
                mViewModel.updateFavorite(mFavoriteEntry);
                return true;
            case R.id.action_share:
                shareItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void shareItem(){
        String shareText = "Checkout: " + mPodcastName + " " +
                mItem.getTitle() + " " +
                mItem.getEnclosures().get(0).getUrl();
        // Create a share intent
        Intent shareIntent = ShareCompat.IntentBuilder.from(PlayingActivity.this)
                .setType(SHARE_INTENT_TYPE_TEXT)
                .setText(shareText)
                .setChooserTitle(getString(R.string.share_episode))
                .createChooserIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        startActivity(shareIntent);
    }

    /**
     * Constructs a MediaBrowserCompat.
     */
    private void createMediaBrowserCompat() {
        Log.d(TAG, "PlayingActivity: createMediaBrowserCompat: ");
        mMediaBrowser = new MediaBrowserCompat(this,
                //the media browser needs the name of the service it is trying to connect to
                new ComponentName(this, PodcastService.class),
                //this callback will be received when we try to connect
                mConnectionCallbacks,
                null);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "PlayinActivity onStart: ");
        super.onStart();
        // Connect to the MediaBrowserService
        mMediaBrowser.connect();
        initObservers();
    }


    private void initObservers() {
        mViewModel.getFavoriteEntry().observe(this, new Observer<FavoriteEntry>() {
            @Override
            public void onChanged(FavoriteEntry favoriteEntry) {
                invalidateOptionsMenu();
                if(favoriteEntry!=null){
                    //the reason for this is to get the auto-generated id,so we can delete it if we want to
                    mFavoriteEntry = favoriteEntry;
                }
                Log.d(TAG, "onChanged: "+favoriteEntry);
            }
        });
        mViewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!TextUtils.isEmpty(s)){
                   Toast t =  Toast.makeText(PlayingActivity.this,s,Toast.LENGTH_SHORT);
                   t.setGravity(Gravity.CENTER,0,0);
                   t.show();
                    mViewModel.resetToast(); //reset the channel so we dont get the snackbar again if we rotate
                }
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(0);
        if (mViewModel.getFavoriteEntry().getValue() != null) {
            item.setIcon(R.drawable.ic_favorite_full);
        } else {
            item.setIcon(R.drawable.ic_favorite_void);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Playing Activity: onResume: ");
        super.onResume();
        // Set the audio stream so the app responds to the volume control on the device
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Playing Activity: onStop: ");
        super.onStop();

        // Disconnect the MediaBrowser and unregister the MediaController.Callback when the
        // activity stops
        if (MediaControllerCompat.getMediaController(this) != null) {
            MediaControllerCompat.getMediaController(this).unregisterCallback(controllerCallback);
        }
        mMediaBrowser.disconnect();
    }


    //these are the callbacks that we receive from the media session
    private final MediaBrowserCompat.ConnectionCallback mConnectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                //if connection is successful
                @Override
                public void onConnected() {
                    Log.d(TAG, "Playing Activity: MediaBrowser onConnected: ");
                    // Get the token for the MediaSession
                    MediaSessionCompat.Token token = mMediaBrowser.getSessionToken();

                    // Create a MediaControllerCompat based on the token passed
                    MediaControllerCompat mediaController = null;
                    try {
                        mediaController = new MediaControllerCompat(PlayingActivity.this, token);
                    } catch (RemoteException e) {
                        Timber.e("Error creating media controller");
                    }

                    // Save the controller for this specific Client (playing Activity)
                    //think of it as a store of key-value pair,
                    //each client has its own controller
                    MediaControllerCompat.setMediaController(PlayingActivity.this,
                            mediaController);

                    // Finish building the UI
                    buildTransportControls();
                }

                @Override
                public void onConnectionSuspended() {
                    super.onConnectionSuspended();
                    // The Service has crashed. Disable transport controls until it automatically
                    // reconnects.
                }

                @Override
                public void onConnectionFailed() {
                    super.onConnectionFailed();
                    // The Service has refused our connection
                }
            };

    void buildTransportControls() {
        // Attach a listener to the play/pause button
        mBinding.ibPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "PlayingActivity: playPause ");
                int pbState = MediaControllerCompat.getMediaController(PlayingActivity.this)
                        .getPlaybackState().getState();

                if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                    MediaControllerCompat.getMediaController(PlayingActivity.this)
                            .getTransportControls().pause();
                    Log.d(TAG, "PlayingActivity: pause ");

                } else {
                    MediaControllerCompat.getMediaController(PlayingActivity.this)
                            .getTransportControls().play();
                    Log.d(TAG, "PlayingActivity: play ");

                }
            }
        });

        // Attach a listener to the fast forward button
        mBinding.ibFastforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaControllerCompat.getMediaController(PlayingActivity.this)
                        .getTransportControls().fastForward();
            }
        });

        // Attach a listener to the rewind button
        mBinding.ibRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaControllerCompat.getMediaController(PlayingActivity.this)
                        .getTransportControls().rewind();
            }
        });


        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(PlayingActivity.this);

        // Display the initial state
        MediaMetadataCompat metadata = mediaController.getMetadata();
        PlaybackStateCompat pbState = mediaController.getPlaybackState();

        if (pbState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_pause);
        } else {
            mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_play);
        }


        // Register a Callback to stay in sync
        mediaController.registerCallback(controllerCallback);
    }


    /**
     * Callback for receiving updates from the session.
     */
    MediaControllerCompat.Callback controllerCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            Log.d(TAG, "PlayActivity onPlaybackStateChanged: ");
            if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_pause);
            } else {
                mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_play);
            }
        }
    };
}
