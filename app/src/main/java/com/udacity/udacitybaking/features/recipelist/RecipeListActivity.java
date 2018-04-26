package com.udacity.udacitybaking.features.recipelist;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.udacitybaking.OnItemClickListener;
import com.udacity.udacitybaking.R;
import com.udacity.udacitybaking.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements OnItemClickListener<Recipe> {

  private static final String TAG = "RecipeListActivity";

  @BindView(R.id.rv_recipe_list)
  RecyclerView rvRecipeList;

  private RecipesAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    ButterKnife.bind(this);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    toolbar.setTitle(getTitle());

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
                  Log.d(TAG, "onCreate: " + resource.getError());
                  Toast.makeText(this, resource.getError().getMessage(), Toast.LENGTH_SHORT).show();
                }
              }
            });
  }

  private void setupRecyclerView() {
    adapter = new RecipesAdapter();
    adapter.setOnItemClickListener(this);

    rvRecipeList.setAdapter(adapter);
    rvRecipeList.setItemAnimator(new DefaultItemAnimator());
    rvRecipeList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    rvRecipeList.setLayoutManager(new LinearLayoutManager(this));
  }

  @Override
  public void onItemClick(Recipe item) {

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
              .inflate(R.layout.recipe_list_content, parent, false);
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

        Resources resources = tvName.getResources();
        tvName.setText(recipe.getName());
        tvIngredientCount.setText(
            resources.getString(R.string.ingredient_count, recipe.getIngredients().size()));
        tvServingCount.setText(resources.getString(R.string.serving_count, recipe.getServings()));
      }
    }
  }
}
