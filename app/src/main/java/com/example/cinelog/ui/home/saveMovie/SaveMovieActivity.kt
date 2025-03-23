package com.example.cinelog.ui.home.saveMovie

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.ActivitySaveMovieBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore

class SaveMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveMovieBinding
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var movieviewmodel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefHelper = SharedPrefHelper(this)

        val movieRepository = MovieRepository(apiService = RetrofitClient.apiService, db = FirebaseFirestore.getInstance())
        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieviewmodel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        setupGenreChips()

        binding.btnSaveMovie.setOnClickListener {
            saveMovie()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        supportActionBar?.title = "Save Movie"
    }

    private fun setupGenreChips() {
        val genres = listOf("Action", "Drama", "Comedy", "Horror", "Sci-Fi", "Romance", "Adventure", "Thriller")
        val chipGroup = binding.chipGroupGenres

        for (genre in genres) {
            val chip = Chip(this)
            chip.text = genre
            chip.isCheckable = true
            chipGroup.addView(chip)
        }
    }


    private fun saveMovie() {
        val title = binding.etTitle.text.toString().trim()
        val poster = binding.etPoster.text.toString().trim()
        val imdbID = binding.etImdbID.text.toString().trim()
        val type = binding.etType.text.toString().trim()
        val year = binding.etYear.text.toString().trim()

        if (title.isEmpty()) {
            showToast("Title is required")
            return
        }
        if (title.length < 2) {
            showToast("Title must be at least 2 characters long")
            return
        }

        if (type.isEmpty()) {
            showToast("Type is required")
            return
        }
        if (type.length < 2) {
            showToast("Type must be at least 2 characters long")
            return
        }
        if (year.isEmpty()) {
            showToast("Year is required")
            return
        }

        if (!year.matches(Regex("\\d{4}"))) {
            showToast("Please enter a valid 4-digit year")
            return
        }

        // Get selected genres
        val selectedGenres = getSelectedGenres()
        if (selectedGenres.isEmpty()) {
            showToast("Please select at least one genre")
            return
        }

        val movie = Movie(title = title, poster = poster, imdbID = imdbID, type = type, year = year, query = "null",genres = selectedGenres)
//        sharedPrefHelper.saveMyMovie(movie)
        movieviewmodel.saveMyMovie(movie)

        showToast("Movie saved successfully")
        finish()
    }

    private fun getSelectedGenres(): List<String> {
        val selectedGenres = mutableListOf<String>()
        for (i in 0 until binding.chipGroupGenres.childCount) {
            val chip = binding.chipGroupGenres.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedGenres.add(chip.text.toString())
                val TAG = "SaveMovieActivity"
                Log.d(TAG, "getSelectedGenres: " + selectedGenres)
            }
        }
        return selectedGenres
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}


