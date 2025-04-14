package com.example.cinelog.ui.home.movieList

import android.content.Context
import android.widget.ImageView
import com.example.cinelog.model.Movie
import com.example.cinelog.model.MovieV2

interface MovieListView {
    fun onSharedClicked(movie: MovieV2)
    fun onFabButtonClicked(movie: MovieV2,fabIcon: ImageView)

}