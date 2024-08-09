package com.dm.berxley.offlinefirstmoviesapp.di

import com.dm.berxley.offlinefirstmoviesapp.movieslist.data.repository.MovieListRepositoryImpl
import com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindMovieListRepository(
        movieListRepositoryImpl: MovieListRepositoryImpl
    ): MovieListRepository
}