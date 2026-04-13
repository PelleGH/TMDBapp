package com.example.tmdbapp.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.dataclasses.Movie
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var topRatedMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var trendingMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var popularMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            val topRatedDeferred = async { TmdbClient.api.getTopRatedMovies() } // loads all three at the same time
            val trendingDeferred = async { TmdbClient.api.getTrendingMovies() }
            val popularDeferred = async { TmdbClient.api.getPopularMovies() }

            topRatedMovies = topRatedDeferred.await().results.take(15)
            trendingMovies = trendingDeferred.await().results.take(15)
            popularMovies = popularDeferred.await().results.take(15)
        }
    }

    fun getMoviesForCategory(category: MovieCategory): List<Movie> {
        return when (category) {
            MovieCategory.TOP_RATED -> topRatedMovies
            MovieCategory.TRENDING -> trendingMovies
            MovieCategory.POPULAR -> popularMovies
        }
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
