package com.example.cinelog.ui.home.myMovies

import android.widget.ImageView
import com.example.cinelog.model.Movie

interface MyMoviesView {

    fun onSharedClicked(movie: Movie)
    fun onFabButtonClicked(movie: Movie, fabIcon: ImageView)
    fun editMovie(movie: Movie)
    fun deleteMovie(movie: Movie)
    fun navigateToDetails(movie: Movie)
}