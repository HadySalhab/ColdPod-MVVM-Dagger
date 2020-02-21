package com.android.myapplication.coldpod.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.ui.playing.PlayingActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class PodcastService extends MediaBrowserServiceCompat implements Player.EventListener {
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    public static final String COLD_POD_ROOT_ID = "pod_root_id";
    public static final String COLD_POD_EMPTY_ROOT_ID = "empty_root_id";
    private SimpleExoPlayer mExoPlayer;
    private static final String TAG = PodcastService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the media session
        initializeMediaSession();

        // Initialize ExoPlayer
        initializePlayer();
    }

    /**
     * Initialize the media session.
     */
    private void initializeMediaSession() {
        // Create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(PodcastService.this, TAG);

        // Enable callbacks from MediaButtons and TransportControls
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_REWIND |
                                PlaybackStateCompat.ACTION_FAST_FORWARD |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback() has methods that handle callbacks from a media controller
        mMediaSession.setCallback(new MySessionCallback());

        // Set the session's token so that client activities can communicate with it.
        setSessionToken(mMediaSession.getSessionToken());

        mMediaSession.setSessionActivity(PendingIntent.getActivity(this,
                11,
                new Intent(this, PlayingActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT));
    }
    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer
            DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(this);
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, defaultRenderersFactory,
                    trackSelector, loadControl);

            // Set the Player.EventListener
            mExoPlayer.addListener(this);

            // Prepare the MediaSource
            String testUrl = "http://feeds.soundcloud.com/stream/525464154-risepodcast-181105-rachel-hollis-rise-podcast-edmylett.m4a";
//          Uri mediaUri = Uri.parse(mItem.getEnclosure().getUrl());
            Uri mediaUri = Uri.parse(testUrl);
            MediaSource mediaSource = buildMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(true);
        }
    }
    /**
     * Create a MediaSource.
     * @param mediaUri
     */
    private MediaSource buildMediaSource(Uri mediaUri) {
        String userAgent = Util.getUserAgent(this, getString(R.string.app_name));
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                this, userAgent);
        return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        mExoPlayer.stop(true);
        stopSelf();
    }


    @Override
    public void onDestroy() {
        mMediaSession.release();
        releasePlayer();

        super.onDestroy();
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        if(clientPackageName.equals(getApplication().getPackageName())){
            return new BrowserRoot(COLD_POD_ROOT_ID,null);
        }
        return new BrowserRoot(COLD_POD_EMPTY_ROOT_ID,null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        // Browsing not allowed
        if (TextUtils.equals(COLD_POD_EMPTY_ROOT_ID, parentId)) {
            result.sendResult(null);
            return;
        }

        List<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();

        // Check if this is the root menu:
        if (COLD_POD_ROOT_ID.equals(parentId)) {
            // Build the MediaItem objects for the top level,
            // and put them in the mediaItems list...

        } else {
            // Examine the passed parentMediaId to see which submenu we're at,
            // and put the children of that menu in the mediaItems list...
        }
        result.sendResult(mediaItems);
    }
    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            // onPlay() callback should include code that calls startService().
            startService(new Intent(getApplicationContext(), PodcastService.class));

            // Set the session active
            mMediaSession.setActive(true);

            // Start the player
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);

            stopForeground(false);
        }

        @Override
        public void onRewind() {
            mExoPlayer.seekTo(Math.max(mExoPlayer.getCurrentPosition() - 10000, 0));
            Timber.e("onRewind:");
        }

        @Override
        public void onFastForward() {
            long duration = mExoPlayer.getDuration();
            mExoPlayer.seekTo(Math.min(mExoPlayer.getCurrentPosition() + 30000, duration));
        }

        @Override
        public void onStop() {
            // onStop() callback should call stopSelf().
            stopSelf();

            // Set the session inactive
            mMediaSession.setActive(false);

            // Stop the player
            mExoPlayer.stop();

            // Take the service out of the foreground
            stopForeground(true);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            // When ExoPlayer is playing, update the PlaybackState
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);

            Timber.d("onPlayerStateChanged: we are playing");
        } else if (playbackState == Player.STATE_READY) {
            // When ExoPlayer is paused, update the PlaybackState
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);

            Timber.d("onPlayerStateChanged: we are paused");
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }
}
