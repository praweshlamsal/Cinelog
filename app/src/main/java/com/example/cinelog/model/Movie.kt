package com.example.cinelog.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity(tableName = "movies")
data class Movie(
    val id: String ="",

    @SerializedName("Title")
    val title: String = "",

    @SerializedName("Poster")
    val poster: String = "",

    @SerializedName("imdbID")
    val imdbID: String = "",

    @SerializedName("Type")
    val type: String = "",

    @SerializedName("Year")
    val year: String = "",

    @ColumnInfo(name = "search_query")
    val query: String = "",
)
