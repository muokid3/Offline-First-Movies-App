package com.dm.berxley.offlinefirstmoviesapp.movieslist.presentation

sealed interface MovieListUiEvent {

    data class Paginate(val category: String) : MovieListUiEvent
    object Navigate : MovieListUiEvent
}