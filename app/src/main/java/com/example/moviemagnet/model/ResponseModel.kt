package com.example.moviemagnet.model

import androidx.room.*
import java.io.*

@Entity(
    tableName = "foundFile" /*primaryKeys = ["id", "file_link", "file_name"]*/,
    indices = [Index(value = ["file_link", "file_name"], unique = true)]
)
data class ResponseModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "file_link")
    var file_link: String?,
    var file_id: String?,
    var file_type: String?,
    @ColumnInfo(name = "file_name")
    var file_name: String?,
    var date_added: String?,
    var time_ago: String?,
    var file_size: String?,
    var file_size_bytes: String?,
    var referrer_link: String?,
    var referrer_host: String?,
    var readable_path: String?,
)
