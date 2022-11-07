package com.isaacdelosreyes.googletvleanback.data.remote.retrofit

import com.isaacdelosreyes.googletvleanback.data.remote.retrofit.webservice.GetMoviesWs
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RemoteConnection {

    private val builder = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getMoviesWs: GetMoviesWs = builder.create()
}