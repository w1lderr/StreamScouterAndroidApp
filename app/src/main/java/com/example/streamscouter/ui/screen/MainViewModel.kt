package com.example.streamscouter.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamscouter.data.repo.MainScreenRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    private val _repository = MutableStateFlow(MainScreenRepo())
    private val _movie_description = MutableStateFlow("")
    val uiState: Flow<MainUiState> get() = _uiState
    val movie_description: Flow<String> get() = _movie_description

    init {
        getRandomMovies()
    }

    private fun setShowResult(value: String) {
        _uiState.value = _uiState.value.copy(showResult = value)
    }

    fun setToast(toast: String) {
        _uiState.value = _uiState.value.copy(toast = toast)
    }

    fun onMovieDescriptionChanged(movie_description: String) {
        _movie_description.value = movie_description
    }

    fun getMovies() {
        val current_movie_description = _movie_description.value

        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (current_movie_description.isNotEmpty()) {
                    val movies = _repository.value.getRecommendations(current_movie_description)
                    _uiState.value = _uiState.value.copy(movies = movies)
                    setShowResult("Result:")
                } else {
                    withContext(Dispatchers.Main) {
                        setToast("U forgot to describe ur movie :)")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    setToast("Error: $e")
                }
            }
        }
    }

    fun getRandomMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val movies = _repository.value.getRandomRecommendations()
                _uiState.value = _uiState.value.copy(movies = movies)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    setToast("Error: $e")
                }
            }
        }
    }
}