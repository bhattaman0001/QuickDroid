package com.example.moviemagnet.util

sealed class Resource<T>(
    val d1: T? = null,
    val d2: Double? = null,
    val d3: String? = null,
    val message: String? = null
) {
    class Success<T>(d1: T, d2: Double, d3: String) : Resource<T>(d1, d2, d3)
    class Error<T>(message: String) : Resource<T>(null, null, null, message)
    class Loading<T> : Resource<T>()
}