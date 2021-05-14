package com.example.uploadpicture.api

data class Payload(
    val downloadUri: String,
    val fileId: String,
    val fileName: String,
    val fileType: String,
    val uploadStatus: Boolean
)