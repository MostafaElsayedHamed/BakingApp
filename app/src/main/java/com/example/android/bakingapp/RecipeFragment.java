package com.example.android.bakingapp;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by mostafa on 8/15/2018.
 */

public class RecipeFragment extends Fragment implements Player.EventListener {
    TextView tv_recipeInstruction;
    SimpleExoPlayerView exoPlayer_recipeVideo;
    ImageView iv_thumbnail;
    SimpleExoPlayer player;
    s step;
    Long videoPosition;
    int recipe_position;
    int position;
    int size_of_recipes;
    View view;
    boolean playerState;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        tv_recipeInstruction = view.findViewById(R.id.recipeInstruction);
        exoPlayer_recipeVideo = view.findViewById(R.id.exoPlayer_recipeVideo);
        iv_thumbnail = view.findViewById(R.id.thumbnail);
        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getLong("videoPosition");
            playerState = savedInstanceState.getBoolean("playerState");
        } else {
            videoPosition = 0L;
            playerState = false;
        }
        this.view = view;
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        populateUI(view);
        if (player != null)
            player.setPlayWhenReady(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
            player.release();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putLong("videoPosition", player.getCurrentPosition());
            outState.putBoolean("playerState", player.getPlayWhenReady());
        }
    }

    public void populateUI(View view) {
        recipe_position = getActivity().getIntent().getIntExtra("recipePosition", 0);
        position = getArguments().getInt("step_position", 0);
        getActivity().setTitle(MainActivity.examplesList.get(recipe_position).getName());
        size_of_recipes = MainActivity.examplesList.get(recipe_position).getSteps().size();
        intializeRecipe(position, view);
    }



    private void intializeRecipe(int position, View view) {
        if (position >= 0 && position < MainActivity.examplesList.get(recipe_position).getSteps().size()) {
            step = null;
            step = MainActivity.examplesList.get(recipe_position).getSteps().get(position);
            if (!(step.getVideoURL().equals("") && step.getVideoURL().isEmpty())) {
                exoPlayer_recipeVideo.setVisibility(View.VISIBLE);
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                player = ExoPlayerFactory.newSimpleInstance(view.getContext(), trackSelector);
                player.setPlayWhenReady(playerState);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(view.getContext(),
                        Util.getUserAgent(view.getContext(), getActivity().getString(R.string.app_name)), null);
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(step.getVideoURL()));
                player.prepare(videoSource);
                if (videoPosition == null) {
                    videoPosition = 0L;
                }
                player.seekTo(videoPosition);
                exoPlayer_recipeVideo.setPlayer(player);
            } else {
                exoPlayer_recipeVideo.setVisibility(View.GONE);
            }
            if (!(step.getThumbnailURL().equals("") && step.getThumbnailURL().isEmpty())) {
                iv_thumbnail.setVisibility(View.VISIBLE);
                Picasso.with(view.getContext()).load(step.getThumbnailURL()).error(R.drawable.vp_placeholder).into(iv_thumbnail);
            } else {
                iv_thumbnail.setVisibility(View.GONE);
            }
            tv_recipeInstruction.setText(step.getDescription());
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
