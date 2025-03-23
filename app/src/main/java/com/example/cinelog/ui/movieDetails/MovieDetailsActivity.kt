package com.example.cinelog.ui.movieDetails

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.cinelog.R
import com.example.cinelog.databinding.ActivityMovieDetailsBinding

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding

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
        val title = intent.getStringExtra("title") ?: ""
        val poster = intent.getStringExtra("poster") ?: ""
        val imdbID = intent.getStringExtra("imdbID") ?: ""
        val type = intent.getStringExtra("type") ?: ""
        val year = intent.getStringExtra("year") ?: ""
        val rating = intent.getFloatExtra("rating", 0f)

        val genres = intent.getStringArrayListExtra("genres") ?: arrayListOf()
        Log.d(TAG, "movieDetailActivity: " + genres)


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
            ratingBar.rating = rating

            // Save rating when user updates it
            btnSaveRating.setOnClickListener {
                val newRating = ratingBar.rating
                Toast.makeText(this@MovieDetailsActivity, "Rating saved: $newRating ⭐", Toast.LENGTH_SHORT).show()
            }
        }
    }
}