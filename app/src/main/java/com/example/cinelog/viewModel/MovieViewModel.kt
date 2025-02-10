package com.example.cinelog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.model.Category
import com.example.cinelog.model.Movie

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categoryList



    suspend fun fetchMovies(searchQuery: String, page: Int) {

        val movieResponse = movieRepository.getMovies(searchQuery, page)
        val currentList = _movieList.value ?: emptyList()
        _movieList.postValue(currentList + movieResponse)
    }

    fun fetchCategories() {
        val categories = listOf(
            Category("Action", "https://cdn-icons-png.flaticon.com/512/16391/16391182.png"),
            Category("Comedy", "https://cdn-icons-png.flaticon.com/512/2162/2162831.png"),
            Category("Drama", "https://cdn-icons-png.flaticon.com/512/1882/1882639.png"),
            Category("Horror", "https://cdn-icons-png.flaticon.com/512/218/218151.png"),
            Category("Thriller", "https://cdn-icons-png.flaticon.com/512/1055/1055643.png"),
            Category("Sci-Fi", "https://imgcdn.stablediffusionweb.com/2024/3/16/852fa9a7-c0f1-43c3-bf2f-f3b70f3f2553.jpg")
        )
        _categoryList.value = categories
    }
}