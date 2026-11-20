package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.marsphotos.network.MarsPhoto
import kotlinx.coroutines.launch

sealed interface MarsUiState {
    data object Loading : MarsUiState
    data class Success(val photos: String) : MarsUiState
    data class Error(val message: String) : MarsUiState
}

class MarsViewModel : ViewModel() {
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    init {
        getMarsPhotos()
    }

    fun getMarsPhotos() {
        // DEMO 3: Use viewModelScope.launch to call the Retrofit service.
        // Set Loading before the request, Success after it, or Error on failure.
        marsUiState = MarsUiState.Loading // temporary starter behavior
    }
}
