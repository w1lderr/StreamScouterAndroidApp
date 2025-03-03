package com.example.streamscouter.ui.screen

import com.example.streamscouter.data.model.Movie

data class MainUiState (
    val showResult: String = "Top-4 random movies/serials",
    val movies: List<Movie> = emptyList(),
    val toast: String = "",

)