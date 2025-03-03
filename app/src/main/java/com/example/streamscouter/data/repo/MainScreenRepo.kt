package com.example.streamscouter.data.repo

import com.example.streamscouter.api.RetrofitService
import com.example.streamscouter.data.model.Movie

class MainScreenRepo {
    private val streamScouterApi = RetrofitService().getStreamScouterApi()

    suspend fun getRecommendations(film_description: String): List<Movie> {
        return streamScouterApi.getRecommendations(film_description)
    }

    suspend fun getRandomRecommendations(): List<Movie> {
        return streamScouterApi.getRandomRecommendations()
    }
}