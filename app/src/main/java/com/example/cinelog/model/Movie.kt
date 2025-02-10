package com.example.cinelog.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @SerializedName("Title")
    val title: String,

    @SerializedName("Poster")
    val poster: String?,

    @SerializedName("imdbID")
    val imdbID: String?,

    @SerializedName("Type")
    val type: String?,

    @SerializedName("Year")
    val year: String?,

    @ColumnInfo(name = "search_query")
    val query: String
)
