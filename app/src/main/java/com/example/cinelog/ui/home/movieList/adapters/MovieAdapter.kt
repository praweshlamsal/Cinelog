package com.example.cinelog.ui.home.movieList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinelog.R
import com.example.cinelog.databinding.ItemMovieBinding
import com.example.cinelog.model.Movie
import com.example.cinelog.ui.home.movieList.MovieListView


class MovieAdapter(private val movieListView: MovieListView, val isFab: Boolean) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = listOf<Movie>()

    fun submitList(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            binding.movieOverview.text = movie.imdbID
            binding.movieReleaseDate.text = movie.year

            Glide.with(binding.root.context)
                .load(movie.poster)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.movieImage)

            binding.ivShare.setOnClickListener {
                movieListView.onSharedClicked(movie)
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
