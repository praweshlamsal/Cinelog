package com.example.cinelog.data.remote.network

import com.example.cinelog.data.remote.ApiService
import com.example.cinelog.data.remote.UpcomingMovieApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL = "http://www.omdbapi.com/"
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val upcomingApiService:UpcomingMovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UpcomingMovieApiService::class.java)
    }
}