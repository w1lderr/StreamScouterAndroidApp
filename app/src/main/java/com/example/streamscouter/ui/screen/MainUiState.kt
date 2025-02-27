package com.example.streamscouter.ui.screen

import com.example.streamscouter.data.model.Movie

data class MainUiState (
    val showResult: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val toast: String = ""
)