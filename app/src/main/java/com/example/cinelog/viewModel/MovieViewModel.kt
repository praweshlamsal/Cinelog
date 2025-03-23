package com.example.cinelog.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.model.BarChartData
import com.example.cinelog.model.Category
import com.example.cinelog.model.HistoryEvent
import com.example.cinelog.model.LineChartData
import com.example.cinelog.model.Movie
import com.example.cinelog.model.PieChartData
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList

    private val _randomMovie = MutableLiveData<Movie>()
    val randomMovie: LiveData<Movie> get() = _randomMovie

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categoryList

    private val _pieChartDataList = MutableLiveData<List<PieChartData>>()
    val pieChartDataList: LiveData<List<PieChartData>> = _pieChartDataList

    private val _barChartDataList = MutableLiveData<List<BarChartData>>()
    val barChartDataList: LiveData<List<BarChartData>> = _barChartDataList

    private val _lineChartDataList = MutableLiveData<List<LineChartData>>()
    val lineChartDataList: LiveData<List<LineChartData>> = _lineChartDataList

    private val _historyEvents = MutableLiveData<List<HistoryEvent>>()
    val historyEvents: LiveData<List<HistoryEvent>> get() = _historyEvents

    // Fetching Movies from the repository (API)
    suspend fun fetchMovies(searchQuery: String, page: Int) {
        val movieResponse = movieRepository.getMovies(searchQuery, page)
        val currentList = _movieList.value ?: emptyList()
        _movieList.postValue(movieResponse)
    }

    // Fetching PieChart data from FireStore
    fun fetchPieChartData(graphId: String) {
        movieRepository.getPieChartData(graphId) { data ->
            _pieChartDataList.postValue(data)
        }
    }

    // Fetching BarChart data from FireStore
    fun fetchBarChartData(graphId: String) {
        movieRepository.getBarChartData(graphId) { data ->
            _barChartDataList.postValue(data)
        }
    }

    // Fetching LineChart data from FireStore
    fun fetchLineChartData(graphId: String) {
        movieRepository.getLineChartData(graphId) { data ->
            _lineChartDataList.postValue(data)
        }
    }

    // Save a favourite movie to FireStore (Favourite Movies collection)
    fun saveMovie(movie: Movie) {
        movieRepository.saveMovie(movie)
    }

    // Fetching all favourite movies from FireStore (Favourite Movies collection)
    fun fetchMoviesFromFireStore() {
        movieRepository.getMoviesList { movieList ->
            _movieList.postValue(movieList)
        }
    }

    // Saving  my_movie to FireStore (My Movies collection)
    fun saveMyMovie(movie: Movie) {
        movieRepository.saveMyMovie(movie)
    }

    // Fetching all my_movies from FireStore (My Movies collection)
    fun fetchMyMoviesFromFireStore() {
        movieRepository.getMyMoviesList { myMoviesList ->
            _movieList.postValue(myMoviesList)
        }
    }

    // Fetching predefined categories
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


    fun fetchRandomMovie() {
        viewModelScope.launch {
            val movie = movieRepository.getRandomMovie()
            _randomMovie.postValue(movie)
        }
    }



    fun fetchHistory() {
        movieRepository.getHistory { data ->
            _historyEvents.postValue(data)
        }
    }

    fun deleteMyMovie(movie: Movie) {
        movieRepository.deleteMyMovieFirebase(movie)
        fetchMyMoviesFromFireStore()
    }

    fun editMyMovie(movie: Movie) {
        movieRepository.editMyMoviesFirebase(movie)
        fetchMyMoviesFromFireStore()
    }
}
