package com.example.cinelog.data.local.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.example.cinelog.model.MovieV2

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveMovie(movie: MovieV2) {
        val movies = getMoviesList().toMutableList()
        if (movies.none { it.title == movie.title }) {
            movies.add(movie)
            saveMoviesList(movies)
        }
    }

    fun getMovie(): MovieV2? {
        val json = sharedPreferences.getString(KEY_MOVIE, null)
        return json?.let {
            gson.fromJson(it, MovieV2::class.java)
        }
    }

    private fun saveMoviesList(movies: List<MovieV2>) {
        val json = gson.toJson(movies)
        sharedPreferences.edit().putString(KEY_MOVIES_LIST, json).apply()
    }

    fun getMoviesList(): List<MovieV2> {
        val json = sharedPreferences.getString(KEY_MOVIES_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<MovieV2>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveMyMovie(movie: MovieV2) {
        val myMovies = getMyMoviesList().toMutableList()
        if (myMovies.none { it.title == movie.title }) {
            myMovies.add(movie)
            saveMyMoviesList(myMovies)
        }
    }

    fun getMyMoviesList(): List<MovieV2> {
        val json = sharedPreferences.getString(KEY_MY_MOVIES_LIST, null) ?: return emptyList()
        val type = object : TypeToken<List<MovieV2>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveMyMoviesList(movies: List<MovieV2>) {
        val json = gson.toJson(movies)
        sharedPreferences.edit().putString(KEY_MY_MOVIES_LIST, json).apply()
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    var isOnboardingCompleted: Boolean
        get() = sharedPreferences.getBoolean(ONBOARDING_KEY, false)
        set(value) = sharedPreferences.edit().putBoolean(ONBOARDING_KEY, value).apply()

    companion object {
        private const val PREF_NAME = "movies_pref"
        private const val KEY_MOVIE = "key_movie"
        private const val KEY_MOVIES_LIST = "key_movies_list"
        private const val KEY_MY_MOVIES_LIST = "key_my_movies_list"
        private const val ONBOARDING_KEY = "onboarding_completed"
    }

    fun getNotificationCount(): Int {
        return sharedPreferences.getInt("notification_count", 0)
    }

    fun resetNotificationCount() {
        sharedPreferences.edit().putInt("notification_count", 0).apply()
    }

    fun setNotification(num:Int){
        sharedPreferences.edit().putInt("notification_count",num).apply()
    }
}
