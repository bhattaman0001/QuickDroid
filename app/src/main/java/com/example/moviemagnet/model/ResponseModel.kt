package com.example.moviemagnet.model

import java.io.*

data class ResponseModel(
    var file_id: String?,
    var file_type: String?,
    var file_name: String?,
    var file_link: String?,
    var date_added: String?,
    var time_ago: String?,
    var file_size: String?,
    var file_size_bytes: String?,
    var referrer_link: String?,
    var referrer_host: String?,
    var readable_path: String?,
) : Serializable
