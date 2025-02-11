package com.example.cinelog.ui.home.favorites

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.cinelog.R
import com.example.cinelog.data.local.sharedPref.SharedPrefHelper
import com.example.cinelog.databinding.FragmentFavoritesBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.movieList.MovieListView
import com.example.cinelog.ui.home.movieList.adapters.MovieAdapter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class FavoritesFragment() : Fragment(R.layout.fragment_favorites), MovieListView {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefHelper = SharedPrefHelper(requireContext())
        movieAdapter = MovieAdapter(this,true)

        binding.moviesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.moviesRecyclerView.adapter = movieAdapter

        loadFavorites()
    }

    private fun loadFavorites() {
        val movies = sharedPrefHelper.getMoviesList()
        movieAdapter.submitList(movies)
    }

    private fun shareMovie(movie: Movie) {
        val context = requireContext() // Get Fragment's context

        // Prepare text message with movie details
        val shareText = """
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
        TODO("Not yet implemented")
    }

}