package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.marsphotos.network.MarsPhoto


sealed interface MarsUiState {
    data object Loading : MarsUiState
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    data object Error : MarsUiState
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
        marsUiState = MarsUiState.Success(emptyList()) // temporary starter behavior
    }
}
