package com.example.tmdbapp.db

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tmdbapp.api.MovieCategory
import com.example.tmdbapp.api.TmdbClient

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val database = MovieDatabase.getDatabase(applicationContext)
            val movieDao = database.movieDao()

            val categoryName = applicationContext
                .getSharedPreferences("movie_cache_prefs", Context.MODE_PRIVATE)
                .getString("selected_category", MovieCategory.TOP_RATED.routeName)
                ?: MovieCategory.TOP_RATED.routeName

            val category = MovieCategory.fromRouteName(categoryName)

            val movies = when (category) {
                MovieCategory.TOP_RATED -> TmdbClient.api.getTopRatedMovies().results
                MovieCategory.TRENDING -> TmdbClient.api.getTrendingMovies().results
                MovieCategory.POPULAR -> TmdbClient.api.getPopularMovies().results
            }

            movieDao.clearMovies()
            movieDao.insertMovies(
                movies.take(15).map { it.toEntity(category.routeName) }
            )

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}