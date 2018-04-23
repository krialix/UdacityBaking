package com.udacity.udacitybaking.network;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class BakingClient {

  private static BakingClient instance = null;

  private BakingService bakingService;

  private BakingClient() {
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
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
