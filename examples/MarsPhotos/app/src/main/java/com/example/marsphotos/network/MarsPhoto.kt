package com.example.marsphotos.network

// DEMO 1: Make this class @Serializable and map the JSON key "img_src".
// The API supplies an array of { "id": "...", "img_src": "..." } objects.
data class MarsPhoto(
    val id: String,
    val imgSrc: String
)