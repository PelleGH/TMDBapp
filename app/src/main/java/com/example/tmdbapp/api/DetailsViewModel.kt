package com.example.tmdbapp.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbapp.api.TmdbClient.api
import com.example.tmdbapp.dataclasses.Cast
import com.example.tmdbapp.dataclasses.MovieDetailsResponse
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {

    var movie by mutableStateOf<MovieDetailsResponse?>(null)
        private set
    var directors by mutableStateOf<List<String>>(emptyList())
        private set
    var cast by mutableStateOf<List<Cast>>(emptyList())
        private set
    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                val details = api.getMovieDetails(movieId)
                val credits = api.getCredits(movieId)

                movie = details

                directors = credits.crew
                    .filter { it.job == "Director" }
                    .map { it.name }

                cast = credits.cast

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}