package com.example.cinelog.ui.history

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinelog.R
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.ActivityHistoryBinding
import com.example.cinelog.ui.history.adapters.HistoryAdapter
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var historyAdapter: HistoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener{
            this.finish()
        }

        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory =  MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]


        // Initialize RecyclerView with LayoutManager
        binding.recyclerViewHistoryDetails.layoutManager = LinearLayoutManager(this)

        // Initialize ViewModel

        // Observe LiveData from ViewModel
        movieViewModel.historyEvents.observe(this, Observer { historyEvents ->
            // Set the adapter with the list of history events
            historyAdapter = HistoryAdapter(historyEvents)
            binding.recyclerViewHistoryDetails.adapter = historyAdapter
        })

        movieViewModel.fetchHistory()
    }
}