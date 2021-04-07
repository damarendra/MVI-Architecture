package com.damn.mvi.data.api

import com.damn.mvi.data.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}