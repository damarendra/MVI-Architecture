package com.damn.mvi.data.api

import com.damn.mvi.data.model.User

class ApiHelperImpl (private val apiService: ApiService) :
    ApiHelper {
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}