package com.example.tmdbapp.dataclasses

data class VideosResponse(
    val results: List<VideoResult>
)

data class VideoResult(
    val key: String,
    val site: String,
    val type: String,
    val name: String
)