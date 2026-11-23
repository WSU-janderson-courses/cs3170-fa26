package com.example.marsphotos.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DEMO 1: Make this class @Serializable and map the JSON key "img_src".
// The API supplies an array of { "id": "...", "img_src": "..." } objects.
@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName("img_src") val imgSrc: String
)