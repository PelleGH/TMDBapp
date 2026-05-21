package com.example.tmdbapp.api

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.dataclasses.Movie
import com.example.tmdbapp.db.MovieDatabase
import com.example.tmdbapp.db.toEntity
import com.example.tmdbapp.db.toMovie
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.core.content.edit
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tmdbapp.db.SyncWorker

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val movieDao = MovieDatabase.getDatabase(application).movieDao()

    var topRatedMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var trendingMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var popularMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var selectedCategoryMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var observeCacheJob: Job? = null

    init {
        loadHomeSections()
    }

    private fun loadHomeSections() {
        viewModelScope.launch {
            topRatedMovies = try {
                TmdbClient.api.getTopRatedMovies().results.take(15)
            } catch (e: Exception) {
                emptyList()
            }

            trendingMovies = try {
                TmdbClient.api.getTrendingMovies().results.take(15)
            } catch (e: Exception) {
                emptyList()
            }

            popularMovies = try {
                TmdbClient.api.getPopularMovies().results.take(15)
            } catch (e: Exception) {
                emptyList()
            }

            if (
                topRatedMovies.isEmpty() &&
                trendingMovies.isEmpty() &&
                popularMovies.isEmpty()
            ) {
                errorMessage = "No internet connection"
            }
        }
    }

    fun selectCategory(category: MovieCategory) {
        getApplication<Application>()
            .getSharedPreferences("movie_cache_prefs", Application.MODE_PRIVATE)
            .edit {
                putString("selected_category", category.routeName)
            }

        observeCachedMovies(category)

        viewModelScope.launch {
            try {
                errorMessage = null
                refreshCategory(category)
            } catch (e: Exception) {
                errorMessage = "No internet connection"
                enqueueSyncWorker()
            }
        }
    }
    private fun enqueueSyncWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(getApplication())
            .enqueueUniqueWork(
                "sync_selected_category",
                ExistingWorkPolicy.REPLACE,
                request
            )
    }
    private fun observeCachedMovies(category: MovieCategory) {
        observeCacheJob?.cancel()

        observeCacheJob = viewModelScope.launch {
            movieDao.getMoviesByCategory(category.routeName).collect { cachedMovies ->
                selectedCategoryMovies = cachedMovies.map { it.toMovie() }
            }
        }
    }

    suspend fun refreshCategory(category: MovieCategory) {
        val movies = when (category) {
            MovieCategory.TOP_RATED -> TmdbClient.api.getTopRatedMovies().results
            MovieCategory.TRENDING -> TmdbClient.api.getTrendingMovies().results
            MovieCategory.POPULAR -> TmdbClient.api.getPopularMovies().results
        }

        movieDao.clearMovies()
        movieDao.insertMovies(
            movies.take(15).map { it.toEntity(category.routeName) }
        )
    }
}

enum class MovieCategory(val routeName: String, val title: String) {
    TOP_RATED("top_rated", "Top Rated Movies"),
    TRENDING("trending", "Trending Movies"),
    POPULAR("popular", "Popular Movies");

    companion object {
        fun fromRouteName(routeName: String): MovieCategory {
            return entries.firstOrNull { it.routeName == routeName } ?: TOP_RATED
        }
    }
}