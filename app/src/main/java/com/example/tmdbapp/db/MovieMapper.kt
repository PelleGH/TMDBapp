package com.example.tmdbapp.db

import com.example.tmdbapp.dataclasses.Movie

fun Movie.toEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        poster_path = poster_path,
        overview = overview,
        vote_average = vote_average,
        category = category
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster_path = poster_path,
        overview = overview,
        vote_average = vote_average
    )
}