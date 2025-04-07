package com.example.cinelog.ui.home.search

import SearchAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.ActivitySearchBinding
import com.example.cinelog.databinding.ItemSearchResultBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.others.LanguageHelper
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var searchAdapter: SearchAdapter
    private var lastQuery: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )
        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        searchAdapter = SearchAdapter(emptyList())
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }

        movieViewModel.movieList.observe(this) { movieList ->
            updateUI(movieList, null)
        }

        // Observe error state (assuming MovieViewModel has an error LiveData)
//        movieViewModel.error.observe(this) { error ->
//            updateUI(null, error)
//        }

        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                lastQuery = query
                performSearch(query)
            }
        }

        binding.etSearch.setOnKeyListener { _, _, _ ->
            binding.ivClear.visibility = if (binding.etSearch.text.isNotEmpty()) View.VISIBLE else View.GONE
            false
        }

        binding.ivClear.setOnClickListener {
            binding.etSearch.text.clear()
            binding.ivClear.visibility = View.GONE
            resetUI()
        }

        binding.btnRetry.setOnClickListener {
            lastQuery?.let { query ->
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {

        binding.clLoading.visibility = View.VISIBLE
        binding.searchResultsRecyclerView.visibility = View.GONE
        binding.clEmpty.visibility = View.GONE
        binding.clError.visibility = View.GONE

        lifecycleScope.launch {
            movieViewModel.fetchMovies(query, 1) // Page 1 for initial search
        }
    }

    private fun updateUI(movieList: List<Movie>?, error: String?) {
        binding.clLoading.visibility = View.GONE
        when {
            error != null -> {
                binding.clError.visibility = View.VISIBLE
                binding.searchResultsRecyclerView.visibility = View.GONE
                binding.clEmpty.visibility = View.GONE
                binding.tvErrorSubtitle.text = error
            }
            movieList != null && movieList.isNotEmpty() -> {
                binding.searchResultsRecyclerView.visibility = View.VISIBLE
                binding.clEmpty.visibility = View.GONE
                binding.clError.visibility = View.GONE
                searchAdapter.updateResults(movieList)
            }
            else -> {
                binding.searchResultsRecyclerView.visibility = View.GONE
                binding.clEmpty.visibility = View.VISIBLE
                binding.clError.visibility = View.GONE
            }
        }
    }

    private fun resetUI() {
        binding.clLoading.visibility = View.GONE
        binding.searchResultsRecyclerView.visibility = View.GONE
        binding.clEmpty.visibility = View.GONE
        binding.clError.visibility = View.GONE
        searchAdapter.updateResults(emptyList())
    }

    override fun attachBaseContext(newBase: Context?) {
        val context = newBase?.let { LanguageHelper.applySavedLocale(it) }
        super.attachBaseContext(context)
    }
}