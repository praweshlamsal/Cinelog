package com.example.cinelog.ui.movieDetails

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cinelog.R
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.ActivityMovieDetailsBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        supportActionBar?.title = "Movie Details"

        // Get data from Intent
        val id = intent.getStringExtra("id") ?: ""
        val title = intent.getStringExtra("title") ?: ""
        val poster = intent.getStringExtra("poster") ?: ""
        val imdbID = intent.getStringExtra("imdbID") ?: ""
        val type = intent.getStringExtra("type") ?: ""
        val year = intent.getStringExtra("year") ?: ""
        val rating = intent.getFloatExtra("rating", 0f) // Default to 0f if not passed
        Log.d(TAG, "movieDetailActivity: Rating " + rating)

        binding.ratingBar.rating = rating

        val genres = intent.getStringArrayListExtra("genres") ?: arrayListOf()
        Log.d(TAG, "movieDetailActivity: " + genres)


        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]



        // Load movie details into UI
        binding.apply {
            Glide.with(this@MovieDetailsActivity)
                .load(poster)
                .placeholder(com.example.cinelog.R.drawable.poster)
                .into(imgMoviePoster)

            tvMovieTitle.text = title
            tvMovieYear.text = year
            tvImdbId.text = "IMDB ID: $imdbID"
            tvMovieType.text = "Type: $type"
            tvGenres.text = "Genres: ${genres.joinToString(", ")}"
            ratingBar.rating = rating.toFloat()

            // Save rating when user updates it
            btnSaveRating.setOnClickListener {
                val newRating = ratingBar.rating
                val movie = Movie( id = id, title = title, poster = poster, imdbID = imdbID, type = type, year = year, query = "null" , genres = genres, rating = newRating.toString())
                movieViewModel.editMyMovie(movie)
                Toast.makeText(this@MovieDetailsActivity, "Rating saved: $newRating ⭐", Toast.LENGTH_SHORT).show()

            }

        }


    }
}