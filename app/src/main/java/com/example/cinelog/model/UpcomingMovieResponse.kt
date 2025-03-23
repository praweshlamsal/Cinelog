package com.example.cinelog.model

import com.google.gson.annotations.SerializedName

data class UpcomingMovieResponse(
    @SerializedName("results") val results: List<UpcomingMovie>
)

