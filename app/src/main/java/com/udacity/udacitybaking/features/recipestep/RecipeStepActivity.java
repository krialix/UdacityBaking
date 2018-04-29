package com.udacity.udacitybaking.features.recipestep;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.udacity.udacitybaking.R;
import com.udacity.udacitybaking.features.recipedetail.RecipeDetailActivity;
import com.udacity.udacitybaking.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity {

  private static final String KEY_TITLE = "TITLE";
  private static final String KEY_STEP = "STEP";

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  public static void start(Context context, String title, Step step) {
    Intent starter = new Intent(context, RecipeStepActivity.class);
    starter.putExtra(KEY_TITLE, title);
    starter.putExtra(KEY_STEP, step);
    context.startActivity(starter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipestep);
    ButterKnife.bind(this);

    Step step = getIntent().getParcelableExtra(KEY_STEP);

    setupToolbar();

    if (savedInstanceState == null) {
      RecipeStepFragment fragment =
          RecipeStepFragment.newInstance(
              step.getDescription(), step.getVideoURL(), step.getThumbnailURL());

      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.item_detail_container, fragment)
          .commit();
    }
  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(getIntent().getStringExtra(KEY_TITLE));
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      navigateUpTo(new Intent(this, RecipeDetailActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
