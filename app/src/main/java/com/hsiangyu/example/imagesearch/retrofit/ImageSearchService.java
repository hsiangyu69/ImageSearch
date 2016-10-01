package com.hsiangyu.example.imagesearch.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by HsiangYu on 2016/9/30.
 */
public interface ImageSearchService {

    @GET("api")
    Call<ResponseBody> getImageSearchResult(@Query("key") String key, @Query("q") String keyword,
                                             @Query("image_type") String imageType, @Query("pretty") String pretty);
}

