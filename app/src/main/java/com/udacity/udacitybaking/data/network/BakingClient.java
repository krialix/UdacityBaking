package com.udacity.udacitybaking.data.network;

import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory;
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class BakingClient {

  private static BakingClient instance = null;

  private BakingService bakingService;

  private BakingClient() {
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
            .addConverterFactory(LiveDataResponseBodyConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build();

    bakingService = retrofit.create(BakingService.class);
  }

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static BakingClient getInstance() {
    if (instance == null) {
      instance = new BakingClient();
    }
    return instance;
  }

  /**
   * Gets movie db service.
   *
   * @return the movie db service
   */
  public BakingService getBakingService() {
    return bakingService;
  }
}
