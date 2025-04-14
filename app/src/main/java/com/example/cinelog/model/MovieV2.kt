package com.example.cinelog.model

data class MovieV2(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val vote_average: Double,
    val vote_count: Int,
    val popularity: Double,
    val adult: Boolean,
    val original_language: String,
    val original_title: String,
    val video: Boolean
)