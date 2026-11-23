package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.network.MarsApi
import com.example.marsphotos.network.MarsPhoto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch


sealed interface MarsUiState {
    data object Loading : MarsUiState
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    data class Error(val message: String) : MarsUiState
    data class RawJson(val json: String): MarsUiState
}

class MarsViewModel : ViewModel() {
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Success(emptyList()))
        private set

    init {
        getMarsPhotos()
    }

    fun getMarsPhotos() {
        // DEMO 3: Use viewModelScope.launch to call the Retrofit service.
        // Set Loading before the request, Success after it, or Error on failure.
        marsUiState = MarsUiState.Loading
        viewModelScope.launch {
            try {
                // val json = MarsApi.retrofitService.getRawPhotos()
                val photos = MarsApi.retrofitService.getPhotos()
                marsUiState = MarsUiState.Success(photos)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                marsUiState = MarsUiState.Error("Error: ${e.message}")
            }
        }
    }
}
