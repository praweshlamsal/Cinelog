package com.example.cinelog.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.View
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
import com.example.cinelog.databinding.ActivityNotificationBinding
import com.example.cinelog.ui.history.adapters.HistoryAdapter
import com.example.cinelog.ui.home.others.LanguageHelper
import com.example.cinelog.ui.notifications.adapters.NotificationAdapter
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var notificationAdapter: NotificationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using View Binding

        binding = ActivityNotificationBinding.inflate(layoutInflater)
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
        binding.recyclerViewNotification.layoutManager = LinearLayoutManager(this)

        // Initialize ViewModel

        // Observe LiveData from ViewModel
        movieViewModel.notificationList.observe(this, Observer { notificationEvents ->
            if (notificationEvents.isEmpty()){
                binding.clEmpty.visibility = View.VISIBLE
            }
            else{
                binding.clEmpty.visibility = View.GONE
                // Set the adapter with the list of history events
                notificationAdapter = NotificationAdapter(notificationEvents)
                binding.recyclerViewNotification.adapter = notificationAdapter
            }

        })

        movieViewModel.fetchNotification()
    }

    override fun attachBaseContext(newBase: Context?) {
        val context = newBase?.let { LanguageHelper.applySavedLocale(it) }
        super.attachBaseContext(context)
    }
}