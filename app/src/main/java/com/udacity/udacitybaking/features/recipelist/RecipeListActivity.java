package com.udacity.udacitybaking.features.recipelist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.udacitybaking.R;
import com.udacity.udacitybaking.data.model.Recipe;
import com.udacity.udacitybaking.features.recipedetail.RecipeDetailActivity;
import com.udacity.udacitybaking.idlingresource.DefaultIdlingResource;
import com.udacity.udacitybaking.util.OnItemClickListener;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements OnItemClickListener<Recipe> {

  @BindView(R.id.rv_recipe_list)
  RecyclerView rvRecipeList;

  @BindBool(R.bool.two_pane_mode)
  boolean isTwoPane;

  private RecipesAdapter adapter;

  @Nullable private DefaultIdlingResource idlingResource;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    ButterKnife.bind(this);

    setupToolbar();

    setupRecyclerView();

    RecipeListViewModel viewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
    viewModel
        .listRecipes()
        .observe(
            this,
            resource -> {
              if (resource != null) {
                if (resource.isSuccess()) {
                  adapter.submitList(resource.getResource());
                } else {
                  Toast.makeText(this, resource.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
              }
            });
  }

  private void setupToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());
  }

  private void setupRecyclerView() {
    adapter = new RecipesAdapter();
    adapter.setOnItemClickListener(this);

    int spanCount = isTwoPane ? 3 : 1;
    GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
    rvRecipeList.setLayoutManager(layoutManager);

    rvRecipeList.setAdapter(adapter);
    rvRecipeList.setItemAnimator(new DefaultItemAnimator());
  }

  @Override
  public void onItemClick(Recipe item) {
    RecipeDetailActivity.start(this, item);
  }

  @VisibleForTesting
  @NonNull
  public IdlingResource getIdlingResource() {
    if (idlingResource == null) {
      idlingResource = new DefaultIdlingResource();
    }
    return idlingResource;
  }

  static class RecipesAdapter extends ListAdapter<Recipe, RecipesAdapter.ViewHolder> {

    static final DiffUtil.ItemCallback<Recipe> DIFF_CALLBACK =
        new DiffUtil.ItemCallback<Recipe>() {
          @Override
          public boolean areItemsTheSame(Recipe oldItem, Recipe newItem) {
            return oldItem.getId() == newItem.getId();
          }

          @Override
          public boolean areContentsTheSame(Recipe oldItem, Recipe newItem) {
            return oldItem.equals(newItem);
          }
        };

    private OnItemClickListener<Recipe> onItemClickListener;

    RecipesAdapter() {
      super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.item_recipe_list, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
      holder.bindTo(getItem(position), onItemClickListener);
    }

    public void setOnItemClickListener(OnItemClickListener<Recipe> onItemClickListener) {
      this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
      @BindView(R.id.iv_recipe_image)
      ImageView ivRecipe;

      @BindView(R.id.tv_recipe_name)
      TextView tvName;

      @BindView(R.id.tv_recipe_ingredient_count)
      TextView tvIngredientCount;

      @BindView(R.id.tv_recipe_serving_count)
      TextView tvServingCount;

      private View view;

      ViewHolder(View view) {
        super(view);
        this.view = itemView;
        ButterKnife.bind(this, view);
      }

      void bindTo(Recipe recipe, OnItemClickListener<Recipe> onItemClickListener) {
        view.setOnClickListener(v -> onItemClickListener.onItemClick(recipe));

        Glide.with(view.getContext())
            .load(recipe.getImage())
            .apply(RequestOptions.placeholderOf(R.drawable.chef))
            .into(ivRecipe);

        Resources resources = tvName.getResources();
        tvName.setText(recipe.getName());
        tvIngredientCount.setText(
            resources.getString(R.string.ingredient_count, recipe.getIngredients().size()));
        tvServingCount.setText(resources.getString(R.string.serving_count, recipe.getServings()));
      }
    }
  }
}
