package com.example.tmdbapp.dataclasses

data class CreditsResponse(
    val crew: List<Crew>,
    val cast: List<Cast>
)
data class Cast(
    val id: String,
    val name: String,
    val character: String?,
    // maybe profile_path later if im feeling fancy
)
data class Crew(
    val job: String,
    val name: String
)