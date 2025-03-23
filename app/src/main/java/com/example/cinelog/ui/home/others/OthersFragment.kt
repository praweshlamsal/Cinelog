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
import com.example.cinelog.databinding.FragmentOthersBinding
import com.example.cinelog.ui.history.HistoryActivity
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class OthersFragment : Fragment(R.layout.fragment_others) {
    private lateinit var binding: FragmentOthersBinding
    private lateinit var historyViewModel: MovieViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOthersBinding.inflate(inflater, container, false)


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