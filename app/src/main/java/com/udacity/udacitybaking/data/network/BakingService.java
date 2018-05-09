package com.udacity.udacitybaking.data.network;

import android.arch.lifecycle.LiveData;

import com.github.leonardoxh.livedatacalladapter.Resource;
import com.udacity.udacitybaking.data.model.Recipe;

import java.util.List;

import retrofit2.http.GET;

public interface BakingService {

  @GET("baking.json")
  LiveData<Resource<List<Recipe>>> getResponse();
}
