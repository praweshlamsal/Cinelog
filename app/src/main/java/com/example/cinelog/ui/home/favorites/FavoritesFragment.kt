package com.example.cinelog.ui.home.favorites

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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


class FavoritesFragment() : Fragment(R.layout.fragment_favorites), MovieListView {
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
        movieAdapter = MovieAdapter(this,true)

        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRecyclerView.adapter = movieAdapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val movies = sharedPrefHelper.getMoviesList()
        movieAdapter.submitList(movies)
    }

    override fun onSharedClicked(movie: Movie) {
        TODO("Not yet implemented")
    }

    override fun onFabButtonClicked(movie: Movie, fabIcon: ImageView) {
        TODO("Not yet implemented")
    }

}