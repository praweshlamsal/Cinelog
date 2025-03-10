package com.example.cinelog.ui.home.saveMovie

import android.content.ContentValues.TAG
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
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class SaveMovieActivity : AppCompatActivity() {

    private var isEditMode: Boolean = false
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: ActivitySaveMovieBinding
    private lateinit var  id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isEditMode = intent.getBooleanExtra("isEditMode", false)

        if (isEditMode) {
            // Retrieve existing movie data
             id = intent.getStringExtra("id") ?: ""
            val title = intent.getStringExtra("title") ?: ""
            val poster = intent.getStringExtra("poster") ?: ""
            val imdbID = intent.getStringExtra("imdbID") ?: ""
            val type = intent.getStringExtra("type") ?: ""
            val year = intent.getStringExtra("year") ?: ""

            // Assign values to the EditText fields
            binding.etTitle.setText(title)
            binding.etPoster.setText(poster)
            binding.etImdbID.setText(imdbID)
            binding.etType.setText(type)
            binding.etYear.setText(year)

            // Update button text to "Update Movie"
            binding.btnSaveMovie.text = getString(R.string.update_movie)

        } else {
            Log.d("SaveMovieActivity", "Creating a New Movie")
        }

        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]


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

        if (isEditMode){
            Log.d(TAG, "EditMovie: $id")
            val movie = Movie( id = id, title = title, poster = poster, imdbID = imdbID, type = type, year = year, query = "null")
            movieViewModel.editMyMovie(movie)
            showToast("Movie edited successfully")
        }else{
            val movie = Movie( id = UUID.randomUUID().toString(),title = title, poster = poster, imdbID = imdbID, type = type, year = year, query = "null")
            movieViewModel.saveMyMovie(movie)
            showToast("Movie saved successfully")
        }

        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}


