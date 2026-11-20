package com.example.marsphotos.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.kotlinx.serialization.asConverterFactory

// We'll write the MarsApiService interface and configure Retrofit here.
// For now the application uses placeholder ViewModel state, so this empty file
// is intentional and the project can run before we add networking.
