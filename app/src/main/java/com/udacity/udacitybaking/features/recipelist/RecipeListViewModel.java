package com.udacity.udacitybaking.features.recipelist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.udacity.udacitybaking.model.Recipe;
import com.udacity.udacitybaking.network.BakingClient;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

  public LiveData<Resource<List<Recipe>>> listRecipes() {
    return BakingClient.getInstance().getBakingService().getResponse();
  }
}
