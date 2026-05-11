package com.example.tmdbapp.db

import com.example.tmdbapp.api.MovieCategory
import com.example.tmdbapp.api.TmdbApi
import com.example.tmdbapp.dataclasses.Movie
import com.example.tmdbapp.dataclasses.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val api: TmdbApi,
    private val movieDao: MovieDao
) {

    fun getMoviesFromCache(category: MovieCategory): Flow<List<Movie>> {
        return movieDao
            .getMoviesByCategory(category.routeName)
            .map { entities ->
                entities.map { it.toMovie() }
            }
    }

    suspend fun refreshCategory(category: MovieCategory) {
        val movies = when (category) {
            MovieCategory.TOP_RATED -> api.getTopRatedMovies().results
            MovieCategory.TRENDING -> api.getTrendingMovies().results
            MovieCategory.POPULAR -> api.getPopularMovies().results
        }

        movieDao.clearCategory(category.routeName)

        movieDao.insertMovies(
            movies.take(15).map { movie ->
                movie.toEntity(category.routeName)
            }
        )
    }

    suspend fun refreshAllCategories() {
        refreshCategory(MovieCategory.TOP_RATED)
        refreshCategory(MovieCategory.TRENDING)
        refreshCategory(MovieCategory.POPULAR)
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse? {
        return try {
            val details = api.getMovieDetails(movieId)

            val existingMovie = movieDao.getMovieByIdOnce(movieId)
            val category = existingMovie?.category ?: "details"

            movieDao.insertMovie(details.toEntity(category))

            details
        } catch (e: Exception) {
            movieDao.getMovieByIdOnce(movieId)?.toDetails()
        }
    }
}