package com.android.myapplication.coldpod.players;

import android.content.Context;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MediaPlayerAdapter extends PlayerAdapter {

    private Context mContext;

    //ExoPlayer Object and its dependencies
    private SimpleExoPlayer mExoPlayer;
    private TrackSelector mTrackSelector; //responsible for selecting tracks
    private DefaultRenderersFactory mRenderersFactory; //responsible for rendering the media (in this case the audio)
    private DataSource.Factory mDataSourceFactory; //is what actually played by exoplayer
    private LoadControl mLoadControl;


    private MediaMetadataCompat mCurrentMedia; //represent the current media
    private boolean mCurrentMediaPlayedToCompletion; //check whether the current media was played to completion
    private static final String TAG = "MediaPlayerAdapter";
    private int mState; //state of the exoplayer at any given time
    private long mStartTime;

    public MediaPlayerAdapter(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    //this is not called inside the constructor because we want to reinitialize the exo after every time it is released
    private void initializeExoPlayer() {
        if (mExoPlayer == null) {
            mTrackSelector = new DefaultTrackSelector();
            mRenderersFactory = new DefaultRenderersFactory(mContext);
            mDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(
                    mContext,
                    "AudioStreamer"
            ));
            mLoadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext,
                    mRenderersFactory,
                    mTrackSelector,
                    mLoadControl);
        }
    }

    private void release() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }



    @Override
    protected void onPlay() {
        if (mExoPlayer != null && !mExoPlayer.getPlayWhenReady()) {
            mExoPlayer.setPlayWhenReady(true); //TELLING EXO TO PLAY
            setNewState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    @Override
    protected void onPause() {
        if (mExoPlayer != null && !mExoPlayer.getPlayWhenReady()) {
            mExoPlayer.setPlayWhenReady(false); //TELLING EXO TO PAUSE
            setNewState(PlaybackStateCompat.STATE_PAUSED);
        }
    }

    @Override
    //called when the file is playing
    public void playFromMedia(MediaMetadataCompat metadata) {
        startTrackingPlayBack(); //playback update to the ui, seekbar for example is updated...
        playFile(metadata);
    }


    @Override
    public MediaMetadataCompat getCurrentMedia() {
        return mCurrentMedia;
    }

    @Override
    public boolean isPlaying() {
        if (mExoPlayer != null) {
            return mExoPlayer.getPlayWhenReady();
        } else {
            return false;
        }
    }

    @Override
    protected void onStop() {
        setNewState(PlaybackStateCompat.STATE_STOPPED);
        release();
    }

    @Override
    public void seekTo(long position) {
        if (mExoPlayer != null) {
            mExoPlayer.seekTo(position);
            setNewState(mState);
        }
    }

    @Override
    public void setVolume(float volume) {
        if (mExoPlayer != null) {
            mExoPlayer.setVolume(volume);
        }
    }




    //metadata is the object that holds all the media infos (url,author,...)
    private void playFile(MediaMetadataCompat metadata) {
        //every media has a unique Id associated with it
        String mediaId = metadata.getDescription().getMediaId();
        boolean mediaChanged = (mCurrentMedia == null || !mediaId.equals(mCurrentMedia.getDescription().getMediaId()));
        if (mCurrentMediaPlayedToCompletion) {
            // Last audio file was played to completion, the resourceId hasn't changed, but the
            // player was released, so force a reload of the media file for playback.
            mediaChanged = true;
            mCurrentMediaPlayedToCompletion = false;
        }
        //if the media is not changed
        if (!mediaChanged) {
            //if the media is not changed and not playing we want to play it
            if (!isPlaying()) {
                play();
            }
            //if th user selected an already playing media, we dont want to do anything
            return;
        } else {
            //every time we select a new file we have to release the exoplayer
            release();
        }

        mCurrentMedia = metadata; //update current media

        initializeExoPlayer(); // because we have release the exo we have to reinitializeExoplayer

        try {
            MediaSource audioSource =
                    new ExtractorMediaSource.Factory(mDataSourceFactory)
                            .createMediaSource(Uri.parse(metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)));

            //prepare is actually get ready to play
            mExoPlayer.prepare(audioSource);
            Log.d(TAG, "onPlayerStateChanged: PREPARE");

        } catch (Exception e) {
            throw new RuntimeException("Failed to play media uri: "
                    + metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI), e);
        }

        play();

    }

    private void startTrackingPlayBack() {
    }

    // This is the main reducer for the player state machine.
    private void setNewState(@PlaybackStateCompat.State int newPlayerState) {
        mState = newPlayerState;

        // Whether playback goes to completion, or whether it is stopped, the
        // mCurrentMediaPlayedToCompletion is set to true.
        if (mState == PlaybackStateCompat.STATE_STOPPED) {
            mCurrentMediaPlayedToCompletion = true;
        }

        final long reportPosition = mExoPlayer == null ? 0 : mExoPlayer.getCurrentPosition();

        // Send playback state information to service
    }

    /**
     * Set the current capabilities available on this session. Note: If a capability is not
     * listed in the bitmask of capabilities then the MediaSession will not handle it. For
     * example, if you don't want ACTION_STOP to be handled by the MediaSession, then don't
     * included it in the bitmask that's returned.
     */
    @PlaybackStateCompat.Actions
    private long getAvailableActions() {
        long actions = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                | PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
        switch (mState) {
            case PlaybackStateCompat.STATE_STOPPED:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE;
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                actions |= PlaybackStateCompat.ACTION_STOP
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_SEEK_TO;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_STOP;
                break;
            default:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_STOP
                        | PlaybackStateCompat.ACTION_PAUSE;
        }
        return actions;
    }



    //this class will help us detect when the exo player is finished
    private class ExoPlayerEventListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_ENDED: {
                    setNewState(PlaybackStateCompat.STATE_PAUSED);
                    break;
                }

                //prepare will make the exo in buffer state
                case Player.STATE_BUFFERING: {
                    Log.d(TAG, "onPlayerStateChanged: BUFFERING");
                    mStartTime = System.currentTimeMillis();
                    break;
                }
                case Player.STATE_IDLE: {
                    break;
                }

                //as soon as the buffer is finished, we are in the ready state
                case Player.STATE_READY: {
                    Log.d(TAG, "onPlayerStateChanged: READY");
                    //we are just trying to find the time it took to buffer a certain file
                    Log.d(TAG, "onPlayerStateChanged: TIME ELAPSED: " + (System.currentTimeMillis() - mStartTime));
                    break;
                }
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    }

}
