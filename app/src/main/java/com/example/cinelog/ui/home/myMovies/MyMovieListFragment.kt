package com.example.cinelog.ui.home.myMovies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.databinding.FragmentFavoritesBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.movieList.MovieListView
import com.example.cinelog.ui.home.movieList.adapters.MovieAdapter
import com.google.gson.Gson

class MyMovieListFragment : Fragment(R.layout.fragment_favorites), MovieListView {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefHelper = SharedPrefHelper(requireContext())
        movieAdapter = MovieAdapter(this, true)

        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRecyclerView.adapter = movieAdapter

        //binding

        loadMyMovies()
    }

    private fun loadMyMovies() {
        val myMovies = sharedPrefHelper.getMyMoviesList()
        movieAdapter.submitList(myMovies)
    }

    override fun onFabButtonClicked(movie: Movie, fabIcon: ImageView) {

    }


    override fun onSharedClicked(movie: Movie) {
        shareMovie(movie)
    }

    private fun shareMovie(movie: Movie) {
        val gson = Gson()
        val movieJson = gson.toJson(movie)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, movieJson)
            putExtra(Intent.EXTRA_SUBJECT, "Check out this movie!")
        }

        startActivity(Intent.createChooser(shareIntent, "Share Movie via"))
    }
}