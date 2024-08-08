package com.dm.berxley.offlinefirstmoviesapp.movieslist.data.repository

import com.dm.berxley.offlinefirstmoviesapp.movieslist.data.local.movie.MovieDatabase
import com.dm.berxley.offlinefirstmoviesapp.movieslist.data.mappers.toMovie
import com.dm.berxley.offlinefirstmoviesapp.movieslist.data.mappers.toMovieEntity
import com.dm.berxley.offlinefirstmoviesapp.movieslist.data.remote.MovieApi
import com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.model.Movie
import com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.repository.MovieListRepository
import com.dm.berxley.offlinefirstmoviesapp.movieslist.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {
    override suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        page: Int,
        category: String
    ): Flow<Resource<List<Movie>>> {
        return flow {

            emit(Resource.Loading(true))

            val localMoviesList = movieDatabase.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMovies = localMoviesList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovies) {
                emit(Resource.Success(
                    data = localMoviesList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))
                emit(Resource.Loading(false))
                return@flow
            }


            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(
                movieEntities.map { movieEntity ->
                    movieEntity.toMovie(category)
                }
            ))

            emit(Resource.Loading(false))
//            return@flow


        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movie = movieDatabase.movieDao.getMovieById(id)

            if (movie != null) {
                emit(
                    Resource.Success(
                        data = movie.toMovie(category = movie.category)
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error(message = "Error, no such movie"))
            emit(Resource.Loading(false))
        }
    }
}