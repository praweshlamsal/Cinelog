package com.example.cinelog.data.remote

import com.example.cinelog.model.GenreResponse
import com.example.cinelog.model.MovieResponse
import com.example.cinelog.model.MovieResponseV2
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

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Response<GenreResponse>


        @GET("movie/top_rated")
        suspend fun getTopRatedMovies(
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "en-US",
            @Query("page") page: Int = 1
        ): Response<MovieResponseV2>

}