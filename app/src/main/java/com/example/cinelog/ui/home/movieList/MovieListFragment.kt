package com.example.cinelog.ui.home.movieList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.FragmentMovieListBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.movieList.adapters.CategoryAdapter
import com.example.cinelog.ui.home.movieList.adapters.MovieAdapter
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MovieListFragment : Fragment(R.layout.fragment_movie_list),MovieListView {

    private lateinit var binding: FragmentMovieListBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sharedPrefHelper: SharedPrefHelper


    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        sharedPrefHelper = SharedPrefHelper(requireContext())
        // Initialize ViewModel
        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        categoryAdapter = CategoryAdapter()
        movieAdapter = MovieAdapter(this)

        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        binding.moviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter
        }


        movieViewModel.movieList.observe(viewLifecycleOwner) { movieList ->
            movieAdapter.submitList(movieList)
        }

        movieViewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
            categoryAdapter.submitList(categoryList)
        }


        lifecycleScope.launch {
            movieViewModel.fetchMovies("movie", currentPage)
        }
        movieViewModel.fetchCategories()

        binding.moviesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    currentPage++
                    lifecycleScope.launch {
                        movieViewModel.fetchMovies("batman", currentPage)
                    }
                }
            }
        })

        return binding.root
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

    override fun onSharedClicked(movie: Movie) {
        shareMovie(movie)
    }
}
