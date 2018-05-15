package com.udacity.udacitybaking.features.recipestep;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.udacity.udacitybaking.R;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepFragment extends Fragment {

  private static final String TAG = "RecipeStepFragment";

  private static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
  private static final String EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL";
  private static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

  private static final String KEY_WINDOW = "window";
  private static final String KEY_POSITION = "position";
  private static final String KEY_AUTO_PLAY = "auto_play";

  @BindView(R.id.recipe_step_desc_card)
  CardView descriptionCard;

  @BindView(R.id.recipe_step_image)
  ImageView stepThumbnail;

  @BindView(R.id.recipe_step_desc)
  TextView descTextView;

  @BindView(R.id.recipe_step_video)
  PlayerView playerView;

  @BindBool(R.bool.two_pane_mode)
  boolean isTwoPane;

  private SimpleExoPlayer player;

  private Uri videoUri;

  private boolean startAutoPlay;
  private int startWindow;
  private long startPosition;

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

    setDescription(args);
    setStepThumbnail(args);

    final String video = args.getString(EXTRA_VIDEO_URL);

    if (!TextUtils.isEmpty(video)) {

      if (savedInstanceState != null) {
        startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
        startWindow = savedInstanceState.getInt(KEY_WINDOW);
        startPosition = savedInstanceState.getLong(KEY_POSITION);
      } else {
        clearStartPosition();
      }

      playerView.setVisibility(View.VISIBLE);

      videoUri = Uri.parse(video);

      final int orientation = getResources().getConfiguration().orientation;

      if (orientation == Configuration.ORIENTATION_LANDSCAPE && !isTwoPane) {
        hideSystemUI();
        expandVideoView(playerView);
        descriptionCard.setVisibility(View.GONE);
      }
    } else {
      playerView.setVisibility(View.GONE);
    }
  }

  private void setDescription(Bundle args) {
    final String description = args.getString(EXTRA_DESCRIPTION, null);
    descTextView.setText(description);
  }

  private void setStepThumbnail(Bundle args) {
    String imageUrl = args.getString(EXTRA_IMAGE_URL);
    if (!TextUtils.isEmpty(imageUrl)) {
      Glide.with(this)
          .load(imageUrl)
          .apply(RequestOptions.placeholderOf(R.drawable.chef))
          .into(stepThumbnail);
      stepThumbnail.setVisibility(View.VISIBLE);
    } else {
      stepThumbnail.setVisibility(View.GONE);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    updateStartPosition();

    outState.putBoolean(KEY_AUTO_PLAY, startAutoPlay);
    outState.putInt(KEY_WINDOW, startWindow);
    outState.putLong(KEY_POSITION, startPosition);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Build.VERSION.SDK_INT > 23 && videoUri != null) {
      initializePlayer(videoUri);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (Build.VERSION.SDK_INT <= 23 || player == null) {
      if (videoUri != null) {
        initializePlayer(videoUri);
      }
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Build.VERSION.SDK_INT <= 23) {
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Build.VERSION.SDK_INT > 23) {
      releasePlayer();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void updateStartPosition() {
    if (player != null) {
      startAutoPlay = player.getPlayWhenReady();
      startWindow = player.getCurrentWindowIndex();
      startPosition = Math.max(0, player.getContentPosition());
    }
  }

  private void clearStartPosition() {
    startAutoPlay = true;
    startWindow = C.INDEX_UNSET;
    startPosition = C.TIME_UNSET;
  }

  private void expandVideoView(PlayerView exoPlayer) {
    exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
    exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
  }

  private void hideSystemUI() {
    playerView.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LOW_PROFILE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
  }

  private void initializePlayer(Uri mediaUri) {
    player =
        ExoPlayerFactory.newSimpleInstance(
            new DefaultRenderersFactory(getContext()),
            new DefaultTrackSelector(),
            new DefaultLoadControl());

    playerView.setPlayer(player);

    MediaSource mediaSource = buildMediaSource(mediaUri);

    boolean haveStartPosition = startWindow != C.INDEX_UNSET;
    if (haveStartPosition) {
      player.seekTo(startWindow, startPosition);
    }

    player.prepare(mediaSource, !haveStartPosition, false);

    player.setPlayWhenReady(startAutoPlay);
  }

  private ExtractorMediaSource buildMediaSource(Uri mediaUri) {
    return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("baking-app"))
        .createMediaSource(mediaUri);
  }

  private void releasePlayer() {
    if (player != null) {
      updateStartPosition();
      startPosition = player.getCurrentPosition();
      startWindow = player.getCurrentWindowIndex();
      startAutoPlay = player.getPlayWhenReady();
      player.release();
      player = null;
    }
  }
}
