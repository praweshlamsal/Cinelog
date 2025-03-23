package com.example.cinelog.data.repository

import android.util.Log
import com.example.cinelog.data.remote.UpcomingMovieApiService
import com.example.cinelog.model.UpcomingMovie
import com.example.cinelog.util.Constant

class UpcomingMovieRepository(private val apiService: UpcomingMovieApiService,) {
     suspend fun getUpcomingMoviesList(): List<UpcomingMovie> {
        return try {
            val response = apiService.getUpcomingMovies()
            Log.d(Constant.MOVIE_REPO, "API Response.....: $response")
            if (response.isSuccessful) {
                Log.d("adfsdfasdfds", response.body()?.results.toString())
                response.body()?.results ?: emptyList()
            } else {
                Log.e(Constant.MOVIE_REPO, "API Error: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(Constant.MOVIE_REPO, "Exception: ${e.localizedMessage}")
            emptyList()
        }
    }
}