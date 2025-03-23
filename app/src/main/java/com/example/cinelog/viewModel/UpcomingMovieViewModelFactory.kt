package com.example.cinelog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cinelog.data.repository.UpcomingMovieRepository

class UpcomingMovieViewModelFactory {
    @Suppress("UNCHECKED_CAST")
    class UpcomingMovieViewModelFactory(private val movieRepository: UpcomingMovieRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UpcomingMovieViewModel::class.java)) {
                return UpcomingMovieViewModel(movieRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
