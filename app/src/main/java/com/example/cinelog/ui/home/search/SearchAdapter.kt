import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinelog.databinding.ItemSearchResultBinding
import com.example.cinelog.model.Movie

class SearchAdapter(private var movies: List<Movie>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    fun updateResults(newMovies: List<Movie>) {

        movies = emptyList()
        movies = newMovies
        notifyDataSetChanged()
    }

    class SearchViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.tvSearchTitle.text = movie.title
            binding.tvSearchYear.text = "Year: ${movie.year}"
            binding.tvSearchType.text = "Type: ${movie.type}"
            Glide.with(itemView.context).load(movie.poster).into(binding.ivSearchPoster)
        }
    }}