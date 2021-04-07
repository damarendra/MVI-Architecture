package com.damn.mvi.data.api

import com.damn.mvi.data.model.User

interface ApiHelper {
    suspend fun getUsers(): List<User>
}