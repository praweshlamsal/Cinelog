package com.example.cinelog.data.repository

import android.util.Log
import com.example.cinelog.data.remote.ApiService
import com.example.cinelog.model.Movie

class MovieRepository(private val apiService: ApiService) {
    suspend fun getMovies(searchQuery: String, page: Int): List<Movie> {
        return try {
            val response = apiService.getMovieList(searchQuery, "e9061878", page)
            Log.d("MovieRepository", "API Response: $response")

            if (response.isSuccessful) {
                response.body()?.Search?.also {
                    Log.d("MovieRepository", "Movies Retrieved: ${it.size}")
                } ?: emptyList()
            } else {
                Log.e("MovieRepository", "API Error: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("MovieRepository", "Exception: ${e.localizedMessage}")
            emptyList()
        }
    }
}