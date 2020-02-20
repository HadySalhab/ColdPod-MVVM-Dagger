/*
 * Copyright 2018 Soojeong Shin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.myapplication.coldpod.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.android.myapplication.coldpod.R;
import com.android.myapplication.coldpod.players.MediaPlayerAdapter;
import com.android.myapplication.coldpod.players.PlayerAdapter;
import com.android.myapplication.coldpod.ui.playing.PlayingActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class MediaService extends MediaBrowserServiceCompat {

    private static MediaSessionCompat mMediaSession;
    private static PlayerAdapter mPlayer;

    private static final String COLD_POD_ROOT_ID = "pod_root_id";
    private static final String COLD_POD_ROOT_EMPTY_ID = "empty_root_id";

    /**
     * Tag for a MediaSessionCompat
     */
    private static final String TAG = MediaService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the media session
        initializeMediaSession();

        // Initialize ExoPlayer
        initializePlayer();
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
        );
        //FLAG_HANDLES_MEDIA_BUTTONS for notification buttons , ui buttons...
        //FLAG_HANDLES_QUEUE_COMMANDS for managing the playlist...
        mMediaSession.setCallback(new MediaSessionCallbacks());
        setSessionToken(mMediaSession.getSessionToken());
    }
    private void initializePlayer(){
        mPlayer = new MediaPlayerAdapter(this);
    }

    @Override
    //swiping out the application from the Stack
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf(); //we want the service to stop
        mPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        //checking if the client is part of this app
        //because in this application we are allowing only clients from within this app to browse
        if(clientPackageName.equals(getApplicationContext().getPackageName())){
            //Allow browsing
        }
        return new BrowserRoot(COLD_POD_ROOT_EMPTY_ID,null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        if(TextUtils.equals(COLD_POD_ROOT_EMPTY_ID,parentId)){
            result.sendResult(null);
            return;
        }
        result.sendResult(null);
    }


    public class MediaSessionCallbacks extends MediaSessionCompat.Callback {
        private final List<MediaSessionCompat.QueueItem> mPlaylist = new ArrayList<>();
        private int mQueueIndex = -1;
        private MediaMetadataCompat mPreparedMedia;
        @Override
        public void onPrepare() {
            super.onPrepare();
            if(mQueueIndex<0 && mPlaylist.isEmpty()){
                return;
            }
            mPreparedMedia = null; //we need to retrieve the selected media here
            if(!mMediaSession.isActive()){
                mMediaSession.setActive(true);
            }
        }

        @Override
        public void onPlay() {
            if(!isReadyToPlay()){
                return;
            }
            if(mPreparedMedia==null){
                onPrepare();
            }
            mPlayer.playFromMedia(mPreparedMedia);
        }

        @Override
        //this method will target an audio from the list
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
            Log.d(TAG, "onPlayFromMediaId: Called");
        }

        @Override
        public void onPause() {
            mPlayer.pause();
        }

        @Override
        public void onSkipToNext() {
            Log.d(TAG, "onSkipToNext: SKIP TO NEXT");
            mQueueIndex = (++mQueueIndex % mPlaylist.size());
            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onSkipToPrevious() {
            Log.d(TAG, "onSkipToPrevious: SKIP TO PREV");
            mQueueIndex = mQueueIndex > 0 ? mQueueIndex -1 : mPlaylist.size()-1;
            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onStop() {
            mPlayer.stop();
            mMediaSession.setActive(false);
        }

        @Override
        public void onSeekTo(long pos) {
           mPlayer.seekTo(pos);
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            Log.d(TAG, "onAddQueueItem: called: position in list" + mPlaylist.size());
            mPlaylist.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mQueueIndex == -1) ? 0 : mQueueIndex;
            mMediaSession.setQueue(mPlaylist);
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            super.onRemoveQueueItem(description);
            Log.d(TAG, "onRemoveQueueItem: called: position in list" + mPlaylist.size());
            mPlaylist.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mPlaylist.isEmpty()) ? -1 : mQueueIndex;
            mMediaSession.setQueue(mPlaylist);
        }
        private boolean isReadyToPlay(){
            return (!mPlaylist.isEmpty());
        }
    }

}
