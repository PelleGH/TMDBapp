package com.example.tmdbapp.api

import com.example.tmdbapp.dataclasses.CreditsResponse
import com.example.tmdbapp.dataclasses.MovieDetailsResponse
import com.example.tmdbapp.dataclasses.TopRatedMoviesResponse
import com.example.tmdbapp.dataclasses.VideosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): TopRatedMoviesResponse

    @GET("3/trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("language") language: String = "en-US"
    ): TopRatedMoviesResponse

    @GET("3/movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): TopRatedMoviesResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieDetailsResponse

    @GET("3/movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): CreditsResponse

    @GET("3/movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): VideosResponse
}
