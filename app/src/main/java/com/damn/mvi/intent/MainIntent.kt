package com.damn.mvi.intent

sealed class MainIntent {
    object FetchUser: MainIntent()
}