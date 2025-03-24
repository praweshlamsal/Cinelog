package com.example.cinelog.ui.home.myMovies

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinelog.R
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.FragmentFavoritesBinding
import com.example.cinelog.model.Movie
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

        val progressBar = binding.loadingProgressBar
        val emptyStateLayout = binding.clEmpty

        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        // Show loading indicator and hide empty state initially
        progressBar.visibility = View.VISIBLE
        emptyStateLayout.visibility = View.GONE

        // Observe movie list
        movieViewModel.movieList.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = View.GONE // Hide loading indicator when data is fetched
            if (it.isEmpty()) {
                emptyStateLayout.visibility = View.VISIBLE // Show empty state if no movies
            } else {
                emptyStateLayout.visibility = View.GONE // Hide empty state if movies are present
                movieAdapter.submitList(it)
                movieAdapter.notifyDataSetChanged()
            }
        })

        // Fetch movies initially when the fragment is created
        movieViewModel.fetchMyMoviesFromFireStore()
    }


    override fun onResume() {
        super.onResume()
        movieViewModel.fetchMyMoviesFromFireStore()
        movieAdapter.submitList(emptyList())
    }

    override fun onFabButtonClicked(movie: Movie, fabIcon: ImageView) {
        // Handle FAB button click (optional)
    }

    override fun editMovie(movie: Movie) {
        val intent = Intent(requireContext(), SaveMovieActivity::class.java).apply {
            putExtra("id", movie.id)
            putExtra("title", movie.title)
            putExtra("poster", movie.poster)
            putExtra("imdbID", movie.imdbID)
            putExtra("type", movie.type)
            putExtra("year", movie.year)
            putExtra("isEditMode", true)
            putStringArrayListExtra("genres", ArrayList(movie.genres))
        }
        startActivity(intent)
    }

    override fun deleteMovie(movie: Movie) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete Movie")
            .setMessage("Are you sure you want to delete this movie?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                movieViewModel.deleteMyMovie(movie)
                movieViewModel.fetchMyMoviesFromFireStore()
                Toast.makeText(requireContext(), "Movie deleted successfully", Toast.LENGTH_SHORT).show()

                // Dismiss the dialog
                dialogInterface.dismiss()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }


    override fun navigateToDetails(movie: Movie) {
        val rating: Float = if (movie.rating.isEmpty()) {
            0.0F
        } else {
            movie.rating.toFloat()
        }
        val intent = Intent(requireContext(), MovieDetailsActivity::class.java).apply {
            putExtra("id", movie.id)
            putExtra("title", movie.title)
            putExtra("poster", movie.poster)
            putExtra("imdbID", movie.imdbID)
            putExtra("type", movie.type)
            putExtra("year", movie.year)
            putExtra("rating", rating)
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
