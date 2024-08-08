package com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.repository

import com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.model.Movie
import com.dm.berxley.offlinefirstmoviesapp.movieslist.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {

    suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        page: Int,
        category: String
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovie(id: Int): Flow<Resource<Movie>>

}