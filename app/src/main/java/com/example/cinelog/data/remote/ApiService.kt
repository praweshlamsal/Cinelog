package com.example.cinelog.data.remote

import com.example.cinelog.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("/")
    suspend fun getMovieList(
        @Query("s") searchQuery: String,
        @Query("apikey") apiKey: String,
        @Query("page") page: Int
    ): Response<MovieResponse>
}