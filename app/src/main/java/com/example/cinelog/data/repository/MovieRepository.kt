package com.example.cinelog.data.repository

import android.util.Log
import com.example.cinelog.data.remote.ApiService
import com.example.cinelog.model.BarChartData
import com.example.cinelog.model.LineChartData
import com.example.cinelog.model.Movie
import com.example.cinelog.model.PieChartData
import com.example.cinelog.util.Constant
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class MovieRepository(private val apiService: ApiService,private val db: FirebaseFirestore) {
    
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
        db.collection("graph")
            .document(graphId)
            .collection("piechart")
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
        db.collection("graph")
            .document(graphId)
            .collection("barchart")
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
        db.collection("graph")
            .document(graphId)
            .collection("linechart")
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






}