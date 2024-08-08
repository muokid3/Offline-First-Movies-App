package com.dm.berxley.offlinefirstmoviesapp.movieslist.data.local.movie

import androidx.room.Query
import androidx.room.Upsert

interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id= :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category= :category")
    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
}