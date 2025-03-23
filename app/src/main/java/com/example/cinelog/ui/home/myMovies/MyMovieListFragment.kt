package com.example.cinelog.ui.home.myMovies

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.FragmentFavoritesBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.movieList.MovieListView
import com.example.cinelog.ui.home.movieList.adapters.MovieAdapter
import com.example.cinelog.ui.home.myMovies.adapters.MyMovieListAdapter
import com.example.cinelog.ui.home.saveMovie.SaveMovieActivity
import com.example.cinelog.ui.movieDetails.MovieDetailsActivity
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class MyMovieListFragment : Fragment(R.layout.fragment_favorites), MyMoviesView {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var movieAdapter: MyMovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieAdapter = MyMovieListAdapter(this, true)

        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRecyclerView.adapter = movieAdapter

        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        movieViewModel.movieList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()){
                binding.clEmpty.visibility =View.VISIBLE
            }
            else{
                binding.clEmpty.visibility = View.GONE
            }
            movieAdapter.submitList(it)
            movieAdapter.notifyDataSetChanged()
        })

        movieViewModel.fetchMyMoviesFromFireStore()



    }


    override fun onFabButtonClicked(movie: Movie, fabIcon: ImageView) {

    }

    override fun editMovie(movie: Movie) {
        val intent = Intent(requireContext(), SaveMovieActivity::class.java).apply {
            putExtra("id",movie.id)
            putExtra("title", movie.title)
            putExtra("poster", movie.poster)
            putExtra("imdbID", movie.imdbID)
            putExtra("type", movie.type)
            putExtra("year", movie.year)
            putExtra("isEditMode", true)
        }
        startActivity(intent)
    }


    override fun deleteMovie(movie: Movie) {
        movieViewModel.deleteMyMovie(movie)
    }

    override fun navigateToDetails(movie: Movie) {
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java).apply {
            putExtra("id",movie.id)
            putExtra("title", movie.title)
            putExtra("poster", movie.poster)
            putExtra("imdbID", movie.imdbID)
            putExtra("type", movie.type)
            putExtra("year", movie.year)

            Log.d(TAG, "navigateToDetails: " + movie.genres)
            putStringArrayListExtra("genres", ArrayList(movie.genres))
        }
        startActivity(intent)
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

