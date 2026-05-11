package com.example.tmdbapp.worker

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tmdbapp.api.TmdbClient
import com.example.tmdbapp.db.MovieDatabase
import com.example.tmdbapp.db.MovieRepository

class SyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val database = Room.databaseBuilder(
                applicationContext,
                MovieDatabase::class.java,
                "movie_database"
            ).build()

            val repository = MovieRepository(
                api = TmdbClient.api,
                movieDao = database.movieDao()
            )

            repository.refreshAllCategories()

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}