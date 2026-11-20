package com.example.marsphotos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marsphotos.network.MarsPhoto
import com.example.marsphotos.ui.theme.MarsPhotosTheme

@Composable
fun HomeScreen(
    marsUiState: MarsUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(modifier = modifier.padding(contentPadding)) {
        when (marsUiState) {
            MarsUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            is MarsUiState.Error -> Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Could not load Mars photos: ${marsUiState.message}")
                Button(onClick = onRetry) { Text("Retry") }
            }

            is MarsUiState.Success -> Text("There are ${marsUiState.photos.size} photos")// PhotosGridScreen(marsUiState.photos)
            is MarsUiState.RawJson -> Text(marsUiState.json)
        }
    }
}

@Composable
fun PhotosGridScreen(
    photos: List<MarsPhoto>,
    modifier: Modifier = Modifier
) {
    // DEMO 5: Replace this Column with a LazyVerticalGrid (optional).
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Loaded ${photos.size} Mars photo records")
        if (photos.isEmpty()) {
            Text("No photos yet. The ViewModel is still using starter data.")
        } else {
            MarsPhotoCard(
                photo = photos.first(),
                modifier = Modifier.fillMaxWidth().height(240.dp)
            )
        }
    }
}

@Composable
fun MarsPhotoCard(
    photo: MarsPhoto,
    modifier: Modifier = Modifier
) {
    // DEMO 4: Replace this Text with Coil's AsyncImage.
    Card(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                "Photo ${photo.id}: ${photo.imgSrc}",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotosGridScreenPreview() {
    MarsPhotosTheme {
        PhotosGridScreen(List(10) {MarsPhoto("MarsPhoto::id: $it","MarsPhoto::imgSrc")})
    }
}

@Preview(showBackground = true)
@Composable
fun MarsPhotoCardPreview() {
    MarsPhotosTheme {
        MarsPhotoCard(
            photo = MarsPhoto(
                id = "MarsPhoto::id",
                imgSrc = "MarsPhoto::imgSrc"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MarsPhotosTheme {
        HomeScreen(
            marsUiState = MarsUiState.Loading,
            onRetry = {},
                    )
    }
}