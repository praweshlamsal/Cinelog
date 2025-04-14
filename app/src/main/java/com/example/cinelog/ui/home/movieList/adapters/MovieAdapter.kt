package com.example.cinelog.ui.home.movieList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinelog.R
import com.example.cinelog.databinding.ItemMovieBinding
import com.example.cinelog.model.MovieV2
import com.example.cinelog.ui.home.movieList.MovieListView


class MovieAdapter(private val movieListView: MovieListView, val isFab: Boolean) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies = listOf<MovieV2>()

    fun submitList(newMovies: List<MovieV2>) {
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

        fun bind(movie: MovieV2) {
            binding.movieTitle.text = movie.title
            binding.movieOverview.text = movie.overview
            binding.movieReleaseDate.text = movie.release_date

            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500"+movie.poster_path)
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
