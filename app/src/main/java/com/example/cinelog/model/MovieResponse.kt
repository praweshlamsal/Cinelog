package com.example.cinelog.model

//data class MovieResponse(
//    val Search: List<Movie>,
//    val totalResults: String,
//    val Response: String
//)



data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_results: Int,
    val total_pages: Int
)
