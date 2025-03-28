package com.example.cinelog.data.repository

import Notification
import android.util.Log
import com.example.cinelog.data.remote.ApiService
import com.example.cinelog.model.BarChartData
import com.example.cinelog.model.HistoryEvent
import com.example.cinelog.model.LineChartData
import com.example.cinelog.model.Movie
import com.example.cinelog.model.PieChartData
import com.example.cinelog.util.Constant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class MovieRepository(private val apiService: ApiService, private val db: FirebaseFirestore) {

    suspend fun getMovies(searchQuery: String, page: Int): List<Movie> {
        return try {
            val response = apiService.getMovieList(searchQuery, Constant.API_KEY, page)
            Log.d(Constant.MOVIE_REPO, "API Response: $response")

            if (response.isSuccessful) {
                response.body()?.Search?.also {
                    Log.d(Constant.MOVIE_REPO, "Movies Retrieved: ${it.size}")
                } ?: emptyList()
            } else {
                Log.e(Constant.MOVIE_REPO, "API Error: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(Constant.MOVIE_REPO, "Exception: ${e.localizedMessage}")
            emptyList()
        }
    }

    fun getPieChartData(graphId: String, callback: (List<PieChartData>) -> Unit) {
        db.collection(Constant.GRAPH)
            .document(graphId)
            .collection(Constant.PIE_CHART)
            .get()
            .addOnSuccessListener { result ->
                val pieChartData = result.mapNotNull { document ->
                    document.toObject(PieChartData::class.java)
                }
                callback(pieChartData)
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error fetching pie chart data", e)
                callback(emptyList()) // Return empty list if error
            }
    }

    fun getBarChartData(graphId: String, callback: (List<BarChartData>) -> Unit) {
        db.collection(Constant.GRAPH)
            .document(graphId)
            .collection(Constant.BAR_CHART)
            .get()
            .addOnSuccessListener { result ->
                val barChartData = result.mapNotNull { document ->
                    document.toObject(BarChartData::class.java)
                }
                callback(barChartData)
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error fetching bar chart data", e)
                callback(emptyList())
            }
    }

    fun getLineChartData(graphId: String, callback: (List<LineChartData>) -> Unit) {
        db.collection(Constant.GRAPH)
            .document(graphId)
            .collection(Constant.LINE_CHART)
            .get()
            .addOnSuccessListener { result ->
                val lineChartData = result.mapNotNull { document ->
                    document.toObject(LineChartData::class.java)
                }
                callback(lineChartData)
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error fetching line chart data", e)
                callback(emptyList())
            }
    }

    // Save favorites movie to FireStore
    fun saveMovie(movie: Movie) {
        val moviesRef = db.collection(Constant.FAVORITES_MOVIE_COLLECTION)

        moviesRef.whereEqualTo("title", movie.title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // If movie does not exist, add it
                    val movieData = mapOf(
                        "id" to movie.id,
                        "imdbID" to movie.imdbID,
                        "poster" to movie.poster,
                        "query" to movie.query,
                        "title" to movie.title,
                        "type" to movie.type,
                        "year" to movie.year,
                        "genres" to movie.genres // Add genres field
                    )

                    moviesRef.add(movieData)
                        .addOnSuccessListener { documentReference ->
                            addHistoryEvent(
                                action = "Saved Movie",
                                movie = movie.title
                            )
                            Log.d(Constant.MOVIE_REPO, "Movie added: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.e(Constant.MOVIE_REPO, "Error adding movie", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error checking for movie", e)
            }
    }

    // Fetch all favorites movies from FireStore
    fun getMoviesList(callback: (List<Movie>) -> Unit) {
        val moviesRef = db.collection(Constant.FAVORITES_MOVIE_COLLECTION)

        moviesRef.get()
            .addOnSuccessListener { result ->
                val movieList = result.mapNotNull { document ->
                    document.toObject(Movie::class.java)
                }
                callback(movieList)
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error fetching movies", e)
                callback(emptyList()) // Return empty list in case of error
            }
    }

    // Save a movie to "my_movies" collection
    fun saveMyMovie(movie: Movie) {
             val myMoviesRef = db.collection(Constant.MY_MOVIES_COLLECTION)

             myMoviesRef.whereEqualTo("id", movie.id)
                 .get()
                 .addOnSuccessListener { querySnapshot ->
                     if (querySnapshot.isEmpty) {
                         // If movie does not exist in "myMovies", add it
                         val movieData = mapOf(
                             "id" to movie.id,
                             "imdbID" to movie.imdbID,
                             "poster" to movie.poster,
                             "query" to movie.query,
                             "title" to movie.title,
                             "type" to movie.type,
                             "year" to movie.year,
                             "genres" to movie.genres
                         )

                         myMoviesRef.add(movieData)
                             .addOnSuccessListener { documentReference ->
                                 Log.d(Constant.MOVIE_REPO, "My movie added: ${documentReference.id}")
                                 addHistoryEvent(
                                     action = "Added New movie",
                                     movie = movie.title
                                 )
                             }
                             .addOnFailureListener { e ->
                                 Log.e(Constant.MOVIE_REPO, "Error adding my movie", e)
                             }
                     }
                 }
                 .addOnFailureListener { e ->
                     Log.e(Constant.MOVIE_REPO, "Error checking for my movie", e)

                 }
     }

    // Fetch all "my_movies" from FireStore
    fun getMyMoviesList(callback: (List<Movie>) -> Unit) {
        val myMoviesRef = db.collection(Constant.MY_MOVIES_COLLECTION)

        myMoviesRef.get()
            .addOnSuccessListener { result ->
                val myMovieList = result.mapNotNull { document ->
                    document.toObject(Movie::class.java)
                }
                callback(myMovieList)
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error fetching my movies", e)
                callback(emptyList()) // Return empty list in case of error
            }
    }

    suspend fun getRandomMovie(): Movie {
        return getMovies("movie" , 1).random()

    }
    fun deleteMyMovieFirebase(movie: Movie) {
        val myMoviesRef = db.collection(Constant.MY_MOVIES_COLLECTION)

        myMoviesRef.whereEqualTo("title", movie.title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    myMoviesRef.document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            addHistoryEvent(
                                action = "Deleted Movie",
                                movie = movie.title
                            )
                            Log.d(Constant.MOVIE_REPO, "Movie deleted: ${document.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.e(Constant.MOVIE_REPO, "Error deleting movie", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error finding movie to delete", e)
            }

    }

    fun editMyMoviesFirebase(movie: Movie) {
        val myMoviesRef = db.collection(Constant.MY_MOVIES_COLLECTION)
        Log.d(Constant.MOVIE_REPO, "Movie updated: }")

        myMoviesRef.whereEqualTo("id", movie.id)
            .limit(1)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()

                    myMoviesRef.document(document.id)
                        .set(movie)
                        .addOnSuccessListener {
                            addHistoryEvent(
                                action = "Edited Movie",
                                movie = movie.title
                            )
                            Log.d(Constant.MOVIE_REPO, "Movie updated: ${document.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.e(Constant.MOVIE_REPO, "Error updating movie", e)
                        }
                } else {
                    Log.e(Constant.MOVIE_REPO, "Movie not found with ID: ${movie.id}")
                }
            }
            .addOnFailureListener { e ->
                Log.e(Constant.MOVIE_REPO, "Error finding movie to update", e)
            }
    }


    fun getHistory(callback: (List<HistoryEvent>) -> Unit){
        val myMoviesRef = db.collection(Constant.HISTORY)
        myMoviesRef.get().addOnSuccessListener { result ->
              val historyList = result.mapNotNull { document ->
                  document.toObject(HistoryEvent::class.java)
              }
            callback(historyList)
        }.addOnFailureListener{
            e -> Log.e(Constant.HISTORY,"This is my history", e)
            callback(emptyList())
        }
    }


    private fun addHistoryEvent(/*context: Context,*/ action: String, movie: String) {
        val event = HistoryEvent(
            id = db.collection("history").document().id, // Generate Firestore ID
            actionPerformed = action,
            movieName = movie,
            timeStamp = System.currentTimeMillis().toString()
        )

        db.collection("history")
            .document(event.id)
            .set(event, SetOptions.merge()) // Merge to avoid overwriting existing data
            .addOnSuccessListener {
                Log.d(Constant.MOVIE_REPO, "addHistoryEvent: History event added successfully!")
                //Toast.makeText(context, "History event added successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.d(Constant.MOVIE_REPO, "addHistoryEvent: failure: ${exception.message}")
                //  Toast.makeText(context, "Failed to add history: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun getNotificaation(callback:(List<Notification>)->Unit){
        val notificationRef = db.collection((Constant.NOTIFICATION))
        notificationRef.get().addOnSuccessListener { result ->
            val notificationList = result.mapNotNull { document ->
                document.toObject(Notification::class.java)
            }
            callback(notificationList)
        }.addOnFailureListener { e -> Log.e(Constant.NOTIFICATION,"Here is notification error", e)
        callback(emptyList())
        }
    }

}





