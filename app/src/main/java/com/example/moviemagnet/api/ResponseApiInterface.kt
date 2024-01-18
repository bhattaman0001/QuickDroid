package com.example.moviemagnet.api

import com.example.moviemagnet.*
import com.example.moviemagnet.model.*
import retrofit2.*
import retrofit2.http.*

interface ResponseApiInterface {
    @GET("/")
    suspend fun getData(
        @Header("X-RapidAPI-Key") header1: String,
        @Header("X-RapidAPI-Host") header2: String,
        @Query("q") parameter1: String,
        @Query("type") parameter2: String,
    ): Response<FileFoundModel>
}