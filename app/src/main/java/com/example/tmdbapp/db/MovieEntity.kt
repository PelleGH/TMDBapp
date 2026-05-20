package com.example.tmdbapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val poster_path: String?,
    val overview: String,
    val vote_average: Double,
    val category: String
)