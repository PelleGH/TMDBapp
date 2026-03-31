package com.example.tmdbapp.api

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.dataclasses.Movie
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

class HomeViewModel : ViewModel() {
    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            val response = TmdbClient.api.getTopRatedMovies()
            movies = response.results.take(15)
        }
    }
}