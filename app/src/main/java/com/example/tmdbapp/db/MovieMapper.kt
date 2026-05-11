package com.example.tmdbapp.db

import com.example.tmdbapp.dataclasses.Movie
import com.example.tmdbapp.dataclasses.MovieDetailsResponse

fun Movie.toEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = poster_path,
        backdropPath = null,
        voteAverage = vote_average,
        releaseDate = null,
        runtime = null,
        genres = null,
        homepage = null,
        imdbId = null,
        category = category
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster_path = posterPath,
        overview = overview,
        vote_average = voteAverage
    )
}

fun MovieDetailsResponse.toEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = poster_path,
        backdropPath = backdrop_path,
        voteAverage = vote_average,
        releaseDate = release_date,
        runtime = runtime,
        genres = null,
        homepage = null,
        imdbId = imdb_id,
        category = category
    )
}

fun MovieEntity.toDetails(): MovieDetailsResponse {
    return MovieDetailsResponse(
        id = id,
        title = title,
        overview = overview,
        backdrop_path = backdropPath,
        poster_path = posterPath,
        release_date = releaseDate ?: "",
        runtime = runtime ?: 0,
        vote_average = voteAverage,
        vote_count = 0,
        imdb_id = imdbId
    )
}