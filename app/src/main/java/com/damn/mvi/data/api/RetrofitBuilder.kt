package com.damn.mvi.data.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {
    private const val BASE_URL =
        "https://606d352a603ded0017503366.mockapi.io/"

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).
        addConverterFactory(MoshiConverterFactory.create()).build()

    val apiService: ApiService = getRetrofit()
        .create(ApiService::class.java)
}