package com.example.cinelog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinelog.data.repository.UpcomingMovieRepository
import com.example.cinelog.model.Movie
import com.example.cinelog.model.UpcomingMovie

class UpcomingMovieViewModel(private val movieRepository: UpcomingMovieRepository):ViewModel() {
    private val _movieList = MutableLiveData<List<UpcomingMovie>>()
    val movieList: LiveData<List<UpcomingMovie>> = _movieList

    suspend fun fetchUpcomingMovies() {
        val movieResponse = movieRepository.getUpcomingMoviesList()
        val currentList = _movieList.value ?: emptyList()
        _movieList.postValue(currentList + movieResponse)
    }
}