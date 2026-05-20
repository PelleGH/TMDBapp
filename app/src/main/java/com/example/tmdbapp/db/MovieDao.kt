package com.example.tmdbapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM cached_movies WHERE category = :category")
    fun getMoviesByCategory(category: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM cached_movies WHERE id = :movieId LIMIT 1")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Query("DELETE FROM cached_movies")
    suspend fun clearMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
}