package com.dm.berxley.offlinefirstmoviesapp.details.presentation

import com.dm.berxley.offlinefirstmoviesapp.movieslist.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
) {
}