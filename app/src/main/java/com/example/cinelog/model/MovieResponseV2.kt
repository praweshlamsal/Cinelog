package com.example.cinelog.model

data class MovieResponseV2(
    val page: Int,
    val results: List<MovieV2>,
    val total_results: Int,
    val total_pages: Int
)