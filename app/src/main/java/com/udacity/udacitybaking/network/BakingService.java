package com.udacity.udacitybaking.network;

import com.udacity.udacitybaking.model.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingService {

  @GET
  Call<Recipe> getResponse();
}
