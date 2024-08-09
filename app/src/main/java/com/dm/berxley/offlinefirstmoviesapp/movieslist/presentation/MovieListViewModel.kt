package com.dm.berxley.offlinefirstmoviesapp.movieslist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.repository.MovieListRepository
import com.dm.berxley.offlinefirstmoviesapp.movieslist.util.Category
import com.dm.berxley.offlinefirstmoviesapp.movieslist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) : ViewModel() {
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovies(false)
        getUpcomingMovies(false)
    }

    private fun onEvent(event: MovieListUiEvent) {
        when (event) {
            MovieListUiEvent.Navigate -> {
                _movieListState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }

            }

            is MovieListUiEvent.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovies(true)
                } else if (event.category == Category.UPCOMING) {
                    getUpcomingMovies(true)
                }
            }
        }
    }

    private fun getPopularMovies(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMoviesList(
                forceFetchFromRemote,
                _movieListState.value.popularMovieListPage,
                Category.POPULAR
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update {
                                it.copy(
                                    popularMovieList = movieListState.value.popularMovieList + popularList,
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }

                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }

            }


        }
    }

    private fun getUpcomingMovies(forceFetchFromRemote: Boolean) {

        viewModelScope.launch {

            _movieListState.update {
                it.copy(isLoading = true)
            }


            movieListRepository.getMoviesList(
                forceFetchFromRemote,
                _movieListState.value.upcomingMovieListPage,
                Category.UPCOMING
            ).collectLatest { result ->

                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            _movieListState.update {
                                it.copy(
                                    upcomingMovieList = movieListState.value.upcomingMovieList + upcomingList,
                                    upcomingMovieListPage = movieListState.value.upcomingMovieListPage + 1
                                )
                            }
                        }
                    }
                }

            }

        }

    }

}