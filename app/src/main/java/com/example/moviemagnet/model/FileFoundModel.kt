package com.example.moviemagnet.model

import com.example.moviemagnet.data.db.entity.ResponseModel

data class FileFoundModel(
    val files_found: MutableList<ResponseModel>,
    val status: String,
)
