package com.example.cinelog.model

data class PieChartData(
    val percentage: Float = 0f,
    val category: String = ""
)

data class BarChartData(
    val movieName: String = "",
    val watchHours: Float = 0f
)

data class LineChartData(
    val day: String = "",
    val watchHours: Float = 0f
)
