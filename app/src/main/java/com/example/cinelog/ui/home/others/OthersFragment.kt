package com.example.cinelog.ui.home.others

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cinelog.R
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.FragmentOthersBinding
import com.example.cinelog.ui.history.HistoryActivity
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class OthersFragment : Fragment(R.layout.fragment_others) {
    private lateinit var binding: FragmentOthersBinding
    private lateinit var listView: ListView
    private lateinit var historyViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using View Binding
        binding = FragmentOthersBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory =  MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        historyViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]


        // Initialize ListView from View Binding
        listView = binding.listViewOthers

        // Simple list with just "History" item
        val historyItems = arrayOf("History")

        // Simple ArrayAdapter to show the list
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, historyItems)
        listView.adapter = adapter

        // Item click listener for navigating to HistoryActivity
        listView.setOnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                // Open HistoryActivity when "History" is clicked
                val intent = Intent(requireContext(), HistoryActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }

}