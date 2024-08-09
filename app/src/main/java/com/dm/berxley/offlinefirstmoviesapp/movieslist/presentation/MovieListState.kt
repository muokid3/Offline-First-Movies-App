package com.dm.berxley.offlinefirstmoviesapp.movieslist.presentation

import com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.model.Movie

data class MovieListState(
    val  isLoading: Boolean = false,
    val popularMovieListPage: Int = 1,
    val upcomingMovieListPage: Int = 1,
    val isCurrentPopularScreen: Boolean = true,
    var currentScreenTitle: String = "Popular Movies",
    val popularMovieList: List<Movie> = emptyList(),
    val upcomingMovieList: List<Movie> = emptyList(),
)
