package com.example.tmdbapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,

    // List/grid fields
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String?,

    // Detail fields
    val runtime: Int?,
    val genres: String?,
    val homepage: String?,
    val imdbId: String?,

    val category: String
)