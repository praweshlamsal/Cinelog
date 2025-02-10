package com.example.cinelog.model

data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
)