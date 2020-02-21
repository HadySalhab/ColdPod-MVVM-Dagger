package com.android.myapplication.coldpod.ui.playing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.SeekBar;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.databinding.ActivityPlayingBinding;
import com.android.myapplication.coldpod.network.Item;
import com.android.myapplication.coldpod.service.PodcastService;
import com.android.myapplication.coldpod.utils.Constants;


import timber.log.Timber;

public class PlayingActivity extends AppCompatActivity {


    private Item mItem;

    /** The MediaBrowser connects to a MediaBrowserService, and upon connecting it creates the
     * MediaController for the UI*/
    private MediaBrowserCompat mMediaBrowser;

    /** This field is used for data binding */
    private ActivityPlayingBinding mBinding;

    public static Intent getInstance(Context context,Item item){
        Intent intent = new Intent(context,PlayingActivity.class);
        intent.putExtra(Constants.EXTRA_ITEM,item);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_playing);
        mItem = getIntent().getParcelableExtra(Constants.EXTRA_ITEM);


        Timber.d("enclosure url: " + mItem.getEnclosure().getUrl());

        // Create MediaBrowserCompat
        createMediaBrowserCompat();
    }


    /**
     * Constructs a MediaBrowserCompat.
     */
    private void createMediaBrowserCompat() {
        mMediaBrowser = new MediaBrowserCompat(this,
                //the media browser needs the name of the service it is trying to connect to
                new ComponentName(this, PodcastService.class),
                //this callback will be received when we try to connect
                mConnectionCallbacks,
                null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect to the MediaBrowserService
        mMediaBrowser.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the audio stream so the app responds to the volume control on the device
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStop() {
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
        //we are getting the controller of this specific client
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(PlayingActivity.this);
        MediaControllerCompat.TransportControls transportControls = mediaControllerCompat.getTransportControls();
        int pbState = mediaControllerCompat.getPlaybackState().getState();



        if (pbState == PlaybackStateCompat.STATE_PLAYING) {
            mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_pause);
        } else {
            mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_play);
        }


        // Attach a listener to the play/pause button
        mBinding.ibPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if the player is currently in playing state, we have to stop -> stop
                if (pbState == PlaybackStateCompat.STATE_PLAYING) {
                    transportControls.pause();

                } else {
                    //if the player is currently not playing any media-> play
                    transportControls.play();

                }
            }
        });

        // Attach a listener to the fast forward button
        mBinding.ibFastforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportControls.fastForward();
            }
        });

        // Attach a listener to the rewind button
        mBinding.ibRewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transportControls.rewind();
            }
        });

        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        // Display the initial state
        MediaMetadataCompat metadata = mediaControllerCompat.getMetadata();



        // Register a Callback to stay in sync
        mediaControllerCompat.registerCallback(controllerCallback);
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
            if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_pause);
            } else {
                mBinding.ibPlayPause.setImageResource(R.drawable.exo_controls_play);
            }
        }
    };
}
