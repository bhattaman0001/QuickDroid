package com.example.moviemagnet.data.db.entity

import androidx.room.*
import java.io.*

@Entity(
    tableName = "History", /*indices = [Index(value = ["queryName"], unique = true)]*/
    primaryKeys = ["queryName", "queryType"]
)
data class HistoryModel(
    var queryName: String,
    var queryType: String,
)
