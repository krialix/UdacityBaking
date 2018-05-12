package com.udacity.udacitybaking.features.recipedetail;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.udacitybaking.BakingApp;
import com.udacity.udacitybaking.R;
import com.udacity.udacitybaking.data.model.Ingredient;
import com.udacity.udacitybaking.data.model.Recipe;
import com.udacity.udacitybaking.data.model.Step;
import com.udacity.udacitybaking.features.recipelist.RecipeListActivity;
import com.udacity.udacitybaking.features.recipestep.RecipeStepActivity;
import com.udacity.udacitybaking.features.recipestep.RecipeStepFragment;
import com.udacity.udacitybaking.features.widget.IngredientListWidgetProvider;
import com.udacity.udacitybaking.util.OnItemClickListener;
import com.udacity.udacitybaking.util.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements OnItemClickListener<Step> {

  public static final String KEY_RECIPE = "RECIPE";

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.item_list)
  RecyclerView recyclerView;

  @Nullable
  @BindView(R.id.item_detail_container)
  ViewGroup container;

  @BindView(R.id.tv_ingredients_content)
  TextView tvIngredients;

  private boolean twoPane;
  private StepAdapter adapter;

  private Recipe recipe;

  public static void start(Context context, Recipe recipe) {
    Intent intent = new Intent(context, RecipeDetailActivity.class);
    intent.putExtra(KEY_RECIPE, recipe);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);

    twoPane = container != null;

    setupRecyclerView();

    recipe = getIntent().getParcelableExtra(KEY_RECIPE);

    saveRecipe(recipe);

    setupToolbar(recipe.getName());

    setIngredients(recipe.getIngredients());

    adapter.submitList(recipe.getSteps());
  }

  private void setIngredients(List<Ingredient> ingredients) {
    StringBuilder sb = new StringBuilder();
    for (Ingredient ingredient : ingredients) {
      sb.append("- ").append(StringUtil.capitalize(ingredient.getIngredient())).append("\n");
    }

    tvIngredients.setText(sb.toString());
  }

  private void setupToolbar(String name) {
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();

    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setTitle(name);
    }
  }

  @Override
  public void onItemClick(Step item) {
    if (twoPane) {
      RecipeStepFragment fragment =
          RecipeStepFragment.newInstance(
              item.getDescription(), item.getVideoURL(), item.getThumbnailURL());

      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.item_detail_container, fragment)
          .commit();
    } else {
      RecipeStepActivity.start(this, recipe.getName(), item);
    }
  }

  private void setupRecyclerView() {
    adapter = new StepAdapter();
    adapter.setOnItemClickListener(this);

    recyclerView.setAdapter(adapter);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      navigateUpTo(new Intent(this, RecipeListActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void saveRecipe(Recipe recipe) {
    BakingApp.LOCAL_RECIPE_STORE.setLastAccessedRecipe(recipe);

    updateWidget();
  }

  private void updateWidget() {
    int[] ids =
        AppWidgetManager.getInstance(this)
            .getAppWidgetIds(new ComponentName(this, IngredientListWidgetProvider.class));
    IngredientListWidgetProvider bakingAppWidget = new IngredientListWidgetProvider();
    bakingAppWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
  }

  static class StepAdapter extends ListAdapter<Step, StepAdapter.ViewHolder> {

    static final DiffUtil.ItemCallback<Step> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Step>() {
          @Override
          public boolean areItemsTheSame(Step oldItem, Step newItem) {
            return oldItem.getId() == newItem.getId();
          }

          @Override
          public boolean areContentsTheSame(Step oldItem, Step newItem) {
            return oldItem.equals(newItem);
          }
        };

    private OnItemClickListener<Step> onItemClickListener;

    StepAdapter() {
      super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_recipe_step, parent, false);
      return new StepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepAdapter.ViewHolder holder, int position) {
      holder.bindTo(getItem(position), onItemClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener<Step> onItemClickListener) {
      this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
      @BindView(R.id.tv_recipe_step)
      TextView tvName;

      ViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
      }

      void bindTo(Step step, OnItemClickListener<Step> onItemClickListener) {
        tvName.setOnClickListener(v -> onItemClickListener.onItemClick(step));

        tvName.setText(step.getShortDescription());
      }
    }
  }
}
