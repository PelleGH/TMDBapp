package com.example.tmdbapp.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tmdbapp.api.AppViewModelFactory
import com.example.tmdbapp.api.TmdbClient
import com.example.tmdbapp.db.MovieDatabase
import com.example.tmdbapp.db.MovieRepository
import com.example.tmdbapp.worker.SyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()

        val repository = MovieRepository(
            api = TmdbClient.api,
            movieDao = database.movieDao()
        )

        val factory = AppViewModelFactory(repository)

        scheduleSyncWorker()

        setContent {
            Navigation(factory = factory)
        }
    }

    private fun scheduleSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<SyncWorker>(
            6,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "movie_sync_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
    }
}