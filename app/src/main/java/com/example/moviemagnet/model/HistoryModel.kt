package com.example.moviemagnet.model

import androidx.room.*
import java.io.*

@Entity(
    tableName = "History",
    primaryKeys = ["queryName", "queryType"]
)
data class HistoryModel(
    var queryName: String,
    var queryType: String,
)
