package com.example.cinelog.ui.home.movieList

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.data.remote.network.RetrofitClient
import com.example.cinelog.data.repository.MovieRepository
import com.example.cinelog.databinding.FragmentMovieListBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.movieList.adapters.CategoryAdapter
import com.example.cinelog.ui.home.movieList.adapters.MovieAdapter
import com.example.cinelog.ui.home.saveMovie.SaveMovieActivity
import com.example.cinelog.ui.notifications.NotificationActivity
import com.example.cinelog.ui.search.SearchActivity
import com.example.cinelog.ui.shakeToSuggest.ShakeToSuggestActivity
import com.example.cinelog.viewModel.MovieViewModel
import com.example.cinelog.viewModel.MovieViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MovieListFragment : Fragment(R.layout.fragment_movie_list),MovieListView {

    private lateinit var binding: FragmentMovieListBinding
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private var notificationCount = 0
    private lateinit var notificationReceiver: BroadcastReceiver

    private var currentPage = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        sharedPrefHelper = SharedPrefHelper(requireContext())
        // Initialize ViewModel
        val movieRepository = MovieRepository(
            apiService = RetrofitClient.apiService,
            db = FirebaseFirestore.getInstance()
        )

        val factory = MovieViewModelFactory.MovieViewModelFactory(movieRepository)
        movieViewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

        categoryAdapter = CategoryAdapter()
        movieAdapter = MovieAdapter(this,false)
        binding.ivSearchIcon.setOnClickListener{
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        binding.ivNotificationIcon.setOnClickListener{
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.fbAddButton.setOnClickListener{
            val intent = Intent(requireContext(), SaveMovieActivity::class.java)
            startActivity(intent)
        }

        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }

        binding.moviesRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = movieAdapter
            //this.hasFixedSize() = true
            this.isNestedScrollingEnabled = false
            //
        }


        movieViewModel.movieList.observe(viewLifecycleOwner) { movieList ->
            movieAdapter.submitList(movieList)
        }

        movieViewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
            categoryAdapter.submitList(categoryList)
        }
        binding.cvRandomMovie.setOnClickListener{
            val intent = Intent(requireContext(), ShakeToSuggestActivity::class.java)
            startActivity(intent)
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

        notificationCount = sharedPrefHelper.getNotificationCount()
        updateNotificationBadge(notificationCount)

        // Replace existing static count code with this
        binding.ivNotificationIcon.setOnClickListener {
            // Reset count when notifications are opened
            sharedPrefHelper.resetNotificationCount()
            notificationCount = 0
            updateNotificationBadge(notificationCount)

            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        Log.d("Notificationtag",sharedPrefHelper.getNotificationCount().toString())
        // Add BroadcastReceiver
        setupNotificationReceiver()
        var count = notificationCount
        binding.notificationBadge.text = count.toString()
        binding.notificationBadge.visibility = if (count > 0) View.VISIBLE else View.GONE

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupNotificationReceiver() {
        notificationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                notificationCount = sharedPrefHelper.getNotificationCount()
                val count = intent?.getIntExtra("notification_count",0) ?: 0
                updateNotificationBadge(count)
            }
        }

        requireContext().registerReceiver(
            notificationReceiver,
            IntentFilter("com.example.cinelog.UPDATE_NOTIFICATION_COUNT"),
            Context.RECEIVER_NOT_EXPORTED,
        )
    }

    private fun updateNotificationBadge(count:Int) {
        binding.notificationBadge.text = count.toString()
        binding.notificationBadge.visibility = if (count > 0) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(notificationReceiver)
    }

    private fun shareMovie(movie: Movie) {
        val context = requireContext() // Get Fragment's context

        // Prepare text message with movie details
        val shareText = """
            Hello fans have you watched this ?
        🎬 *${movie.title}* (${movie.year})
        📽️ Type: ${movie.type}
        🌟 IMDb: https://www.imdb.com/title/${movie.imdbID}
        
        🎥 Check it out! 🍿
    """.trimIndent()

        Glide.with(context)
            .asBitmap()
            .load(movie.poster)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache for faster loading
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val uri = resource.toUri(context) // Convert Bitmap to URI

                    // Create share intent with text and image
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain" // Set the MIME type to text/plain
                        putExtra(Intent.EXTRA_TEXT, shareText) // Attach the formatted text
                        putExtra(Intent.EXTRA_STREAM, uri) // Attach the image
                        putExtra(Intent.EXTRA_SUBJECT, "Check out this movie!")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant permission to read the URI
                    }

                    // Use Intent.createChooser to show options to share via different apps
                    startActivity(Intent.createChooser(shareIntent, "Share Movie via"))
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun Bitmap.toUri(context: Context): Uri {
        val imagesFolder = File(context.cacheDir, "images").apply { mkdirs() } // ✅ Create cache directory
        val file = File(imagesFolder, "shared_movie.png")

        try {
            FileOutputStream(file).use { outputStream ->
                this.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // ✅ Save image
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    override fun onSharedClicked(movie: Movie) {
        shareMovie(movie)
    }

    override fun onFabButtonClicked(movie: Movie, fabIcon: ImageView) {
        sharedPrefHelper.saveMovie(movie)
        fabIcon.setImageResource(R.drawable.ic_fab_fill)

        Toast.makeText(requireContext(), "Saved to favorites", Toast.LENGTH_SHORT).show()

    }
}
