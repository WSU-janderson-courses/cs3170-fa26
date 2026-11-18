package com.example.marsphotos.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// Prep work: Retrofit and the JSON converter are configured in advance.
// Retrofit base URLs must end with a slash.
private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        Json { ignoreUnknownKeys = true }
            .asConverterFactory("application/json".toMediaType())
    )
    .build()

interface MarsApiService {
    // DEMO 2: Add the @GET("photos") annotation here.
    suspend fun getPhotos(): List<MarsPhoto>
}

object MarsApi {
    // This one Retrofit-created service is reused for all requests.
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
