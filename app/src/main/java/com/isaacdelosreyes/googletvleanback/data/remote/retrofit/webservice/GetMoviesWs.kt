package com.isaacdelosreyes.googletvleanback.data.remote.retrofit.webservice

import com.isaacdelosreyes.googletvleanback.data.remote.model.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GetMoviesWs {

    @GET("discover/movie")
    suspend fun getListMovies(
        @Query("api_key") apiKey: String,
        @Query("sort_by") sortBy: String
    ): MovieListDto
}