package com.example.tmdbapp.api

import com.example.tmdbapp.dataclasses.TopRatedMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): TopRatedMoviesResponse
}
