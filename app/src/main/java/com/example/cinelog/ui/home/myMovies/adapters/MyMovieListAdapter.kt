package com.example.cinelog.ui.home.myMovies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinelog.R
import com.example.cinelog.databinding.ItemMyMoviesBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.movieList.MovieListView
import com.example.cinelog.ui.home.myMovies.MyMoviesView


class MyMovieListAdapter(private val movieListView: MyMoviesView, val isFab: Boolean) :
    RecyclerView.Adapter<MyMovieListAdapter.MovieViewHolder>() {

    private var movies = listOf<Movie>()

    fun submitList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMyMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(private val binding: ItemMyMoviesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            binding.movieOverview.text = movie.imdbID
            binding.movieReleaseDate.text = movie.year

            // Join genres with a comma and display them
            val genresText = movie.genres.joinToString(", ")
            binding.movieGenre.text = "Genres: $genresText"

            Glide.with(binding.root.context)
                .load(movie.poster)
                .placeholder(R.drawable.ic_my_movies)
                .into(binding.movieImage)

            binding.ivShare.setOnClickListener {
                movieListView.onSharedClicked(movie)
            }

            binding.ivEdit.setOnClickListener{
                movieListView.editMovie(movie)
            }

            binding.ivDelete.setOnClickListener {
                movieListView.deleteMovie(movie)
            }

            binding.root.setOnClickListener{
                movieListView.navigateToDetails(movie)
            }

            if (!isFab) {
                binding.fabButton.setOnClickListener {
                    movieListView.onFabButtonClicked(movie, binding.fabButton)

                }
            } else {
                binding.fabButton.setImageResource(R.drawable.ic_fab_fill)
            }
        }
    }
}