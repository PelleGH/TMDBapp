package com.example.tmdbapp.dataclasses

data class MovieDetailsResponse(
    val id: Int,
    val title: String,
    val overview: String,
    val backdrop_path: String?,
    val poster_path: String?,
    val release_date: String,
    val runtime: Int,
    val vote_average: Double,
    val vote_count: Int,
    val imdb_id: String?,
)