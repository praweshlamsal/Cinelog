package com.example.cinelog.ui.home.others

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cinelog.R
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.data.repository.UpcomingMovieRepository
import com.example.cinelog.databinding.FragmentOthersBinding
import com.example.cinelog.model.UpcomingMovie
import com.example.cinelog.ui.history.HistoryActivity
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.example.cinelog.viewModel.UpcomingMovieViewModel
import com.example.cinelog.viewModel.UpcomingMovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class OthersFragment : Fragment(R.layout.fragment_others) {
    private lateinit var binding: FragmentOthersBinding
    private lateinit var historyViewModel: MovieViewModel
    private lateinit var upcomingViewModel: UpcomingMovieViewModel


    private val _movieList = MutableLiveData<List<UpcomingMovie>>()
    val movieList: LiveData<List<UpcomingMovie>> get() = _movieList

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOthersBinding.inflate(inflater, container, false)
        val upcomingMovieRepo = UpcomingMovieRepository(RetrofitClient.upcomingApiService)
        val upcomingFactory = UpcomingMovieViewModelFactory.UpcomingMovieViewModelFactory(upcomingMovieRepo)
        upcomingViewModel = ViewModelProvider(this, upcomingFactory)[UpcomingMovieViewModel::class.java]


        lifecycleScope.launch {
            val movieResponse = upcomingMovieRepo.getUpcomingMoviesList()
//            val currentList = _movieList.value ?: emptyList()
            _movieList.postValue( movieResponse)
            Log.d("UpcomingMovieViewModel", "Fetched ${movieResponse.size} movies successfully.")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel (keep existing data flow)
        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )
        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        historyViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]


        setupNavigationIcon()
    }

    private fun setupNavigationIcon() {
        // Set click listener on the toolbar's history icon
        binding.toolbar.findViewById<View>(R.id.history_icon).setOnClickListener {
            navigateToHistory()
        }
    }

    private fun navigateToHistory() {
        val intent = Intent(requireContext(), HistoryActivity::class.java)
        startActivity(intent)
    }
}