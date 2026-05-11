package com.example.tmdbapp.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.dataclasses.Movie
import com.example.tmdbapp.db.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    var topRatedMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var trendingMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var popularMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    init {
        observeCache()
        refreshMovies()
    }

    private fun observeCache() {
        viewModelScope.launch {
            repository.getMoviesFromCache(MovieCategory.TOP_RATED).collect {
                topRatedMovies = it
            }
        }

        viewModelScope.launch {
            repository.getMoviesFromCache(MovieCategory.TRENDING).collect {
                trendingMovies = it
            }
        }

        viewModelScope.launch {
            repository.getMoviesFromCache(MovieCategory.POPULAR).collect {
                popularMovies = it
            }
        }
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            try {
                repository.refreshAllCategories()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
