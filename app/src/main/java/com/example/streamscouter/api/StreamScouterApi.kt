package com.example.streamscouter.api

import com.example.streamscouter.data.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface StreamScouterApi {
    @GET("getRecommendations")
    suspend fun getRecommendations(@Query("movie_description") movie_description: String): List<Movie>

    @GET("getRandomRecommendations")
    suspend fun getRandomRecommendations(): List<Movie>
}