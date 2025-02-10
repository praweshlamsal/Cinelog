package com.example.cinelog.ui.home.movieList

import android.widget.ImageView
import com.example.cinelog.model.Movie

interface MovieListView {
    fun onSharedClicked(movie: Movie)
}