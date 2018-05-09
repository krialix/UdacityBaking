package com.udacity.udacitybaking.features.recipestep;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.udacitybaking.R;

import java.util.Objects;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment {

  private static final String TAG = "RecipeStepFragment";

  private static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
  private static final String EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL";
  private static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

  private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

  @BindView(R.id.recipe_step_desc_card)
  CardView descriptionCard;

  @BindView(R.id.recipe_step_image)
  ImageView stepThumbnail;

  @BindView(R.id.recipe_step_desc)
  TextView descTextView;

  @BindView(R.id.recipe_step_video)
  PlayerView exoPlayerView;

  @BindBool(R.bool.two_pane_mode)
  boolean isTwoPane;

  private SimpleExoPlayer exoPlayer;
  private MediaSessionCompat mediaSession;
  private PlaybackStateCompat.Builder stateBuilder;

  private Unbinder unbinder;

  public RecipeStepFragment() {}

  public static RecipeStepFragment newInstance(
      String description, String videoUrl, String imageUrl) {
    Bundle arguments = new Bundle();
    arguments.putString(EXTRA_DESCRIPTION, description);
    arguments.putString(EXTRA_VIDEO_URL, videoUrl);
    arguments.putString(EXTRA_IMAGE_URL, imageUrl);
    RecipeStepFragment fragment = new RecipeStepFragment();
    fragment.setArguments(arguments);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recipestep, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Bundle args = getArguments();

    String description = args.getString(EXTRA_DESCRIPTION);
    descTextView.setText(description);

    String imageUrl = args.getString(EXTRA_IMAGE_URL);

    if (!TextUtils.isEmpty(imageUrl)) {
      Glide.with(this).load(imageUrl).into(stepThumbnail);
      stepThumbnail.setVisibility(View.VISIBLE);
    } else {
      stepThumbnail.setVisibility(View.GONE);
    }

    final int orientation = getResources().getConfiguration().orientation;
    String video = args.getString(EXTRA_VIDEO_URL);

    if (!TextUtils.isEmpty(video)) {
      exoPlayerView.setVisibility(View.VISIBLE);
      initializeMediaSession();
      initializePlayer(Uri.parse(video));

      if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane) {
        expandVideoView(exoPlayerView);
        descriptionCard.setVisibility(View.GONE);
        hideSystemUI();
      }
    } else {
      exoPlayerView.setVisibility(View.GONE);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    releasePlayer();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void expandVideoView(PlayerView exoPlayer) {
    exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
    exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
  }

  private void hideSystemUI() {
    Objects.requireNonNull(getActivity())
        .getWindow()
        .getDecorView()
        .setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
  }

  private void initializeMediaSession() {
    mediaSession = new MediaSessionCompat(getContext(), TAG);

    mediaSession.setFlags(
        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
            | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    mediaSession.setMediaButtonReceiver(null);
    stateBuilder =
        new PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE);

    mediaSession.setPlaybackState(stateBuilder.build());
    mediaSession.setCallback(
        new MediaSessionCompat.Callback() {
          @Override
          public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
          }

          @Override
          public void onPause() {
            exoPlayer.setPlayWhenReady(false);
          }

          @Override
          public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
          }
        });
    mediaSession.setActive(true);
  }

  private void initializePlayer(Uri mediaUri) {
    boolean needNewPlayer = exoPlayer == null;
    if (needNewPlayer) {
      TrackSelection.Factory adaptiveTrackSelectionFactory =
          new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
      TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);

      exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
      exoPlayerView.setPlayer(exoPlayer);
      exoPlayer.addListener(
          new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
              if ((playbackState == Player.STATE_READY) && playWhenReady) {
                stateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING, exoPlayer.getCurrentPosition(), 1f);
              } else if (playbackState == Player.STATE_READY) {
                stateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED, exoPlayer.getCurrentPosition(), 1f);
              }
              mediaSession.setPlaybackState(stateBuilder.build());
            }
          });

      String userAgent = Util.getUserAgent(getContext(), "StepVideo");

      MediaSource mediaSource =
          new ExtractorMediaSource(
              mediaUri,
              new DefaultDataSourceFactory(getContext(), userAgent),
              new DefaultExtractorsFactory(),
              null,
              null);
      exoPlayer.prepare(mediaSource);
      exoPlayer.setPlayWhenReady(true);
    }
  }

  private void releasePlayer() {
    if (exoPlayer != null) {
      exoPlayer.stop();
      exoPlayer.release();
      exoPlayer = null;
    }

    if (mediaSession != null) {
      mediaSession.setActive(false);
    }
  }
}
