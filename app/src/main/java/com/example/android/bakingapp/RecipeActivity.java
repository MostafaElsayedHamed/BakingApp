package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.model.s;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class RecipeActivity extends AppCompatActivity implements Player.EventListener{
    ImageView thumbnail;
    Button previous;
    Button next;
    TextView recipeInstruction;
    SimpleExoPlayerView recipeVideo;
    SimpleExoPlayer player;
    int reposition;
    s step;
    int position;
    Long videoPosition;
    boolean playerState;
    int size_of_recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        thumbnail = findViewById(R.id.thumbnail);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        recipeInstruction = findViewById(R.id.recipeInstruction);
        recipeVideo = findViewById(R.id.exoPlayer_recipeVideo);
        if (savedInstanceState == null) {
            videoPosition = 0L;
            position = getIntent().getIntExtra("position", 0);
            reposition = getIntent().getIntExtra("rePosition", 0);
            playerState = false;
        } else {
            videoPosition = savedInstanceState.getLong("videoPosition");
            position = savedInstanceState.getInt("position");
            reposition = savedInstanceState.getInt("rePosition");
            playerState = savedInstanceState.getBoolean("playerState");
        }
        setTitle(MainActivity.examplesList.get(reposition).getName());
        size_of_recipes = MainActivity.examplesList.get(reposition).getSteps().size();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                initialize(position);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                initialize(position);
            }
        });
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            previous.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
            recipeInstruction.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().show();
            }
            previous.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
            recipeInstruction.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong("videoPosition", player.getCurrentPosition());
            outState.putBoolean("playState", player.getPlayWhenReady());
        }
        outState.putInt("position", position);
        outState.putInt("rePosition", reposition);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize(position);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
        }
    }

    private void initialize(int position) {

        if (position >= 0 && position < MainActivity.examplesList.get(reposition).getSteps().size()) {
            if (position == 0) {
                previous.setVisibility(View.INVISIBLE);
                next.setVisibility(View.VISIBLE);
            } else if (position == size_of_recipes - 1) {
                next.setVisibility(View.INVISIBLE);
                previous.setVisibility(View.VISIBLE);
            } else if (position > 0 && position < size_of_recipes) {
                next.setVisibility(View.VISIBLE);
                previous.setVisibility(View.VISIBLE);
            }
            step = null;
            step = MainActivity.examplesList.get(reposition).getSteps().get(position);
            if (!(step.getVideoURL().equals("") && step.getVideoURL().isEmpty())) {
                if (player != null) {
                    player.release();
                    player = null;
                }
                recipeVideo.setVisibility(View.VISIBLE);
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
                player.setPlayWhenReady(playerState);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                        this, Util.getUserAgent(this, getString(R.string.app_name)), null);
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(step.getVideoURL()));
                player.prepare(videoSource);
                player.seekTo(videoPosition);
                recipeVideo.setPlayer(player);
            } else {
                recipeVideo.setVisibility(View.GONE);
            }
            if (!(step.getThumbnailURL().equals("") && step.getThumbnailURL().isEmpty())) {
                thumbnail.setVisibility(View.VISIBLE);
                Picasso.with(this).load(step.getThumbnailURL()).error(R.drawable.vp_placeholder).into(thumbnail);
            } else {
                thumbnail.setVisibility(View.GONE);
            }
            recipeInstruction.setText(step.getDescription());



        }

    }

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
