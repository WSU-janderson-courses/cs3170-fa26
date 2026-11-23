package com.example.marsphotos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marsphotos.network.MarsPhoto
import com.example.marsphotos.ui.theme.MarsPhotosTheme
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.res.painterResource
import com.example.marsphotos.R

@Composable
fun HomeScreen(
    marsUiState: MarsUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(modifier = modifier.padding(contentPadding)) {
        when (marsUiState) {
            MarsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            is MarsUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Could not load Mars photos: ${marsUiState.message}")
                    Button(onClick = onRetry) { Text("Retry") }
                }
            }

            is MarsUiState.Success -> {
                PhotosGridScreen(marsUiState.photos)
            }

            is MarsUiState.RawJson -> {
                Text(
                    "Raw JSON response\n(${marsUiState.json.length} characters total)\n-------\n${marsUiState.json}",
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun PhotosGridScreen(
    photos: List<MarsPhoto>,
    modifier: Modifier = Modifier
) {
    // DEMO 5: Replace this Column with a LazyVerticalGrid (optional).
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items = photos, key = { photo -> photo.id} ) { photo ->
            MarsPhotoCard(
                photo = photo,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photo.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "Mars photo ${photo.id}",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.ic_broken_image),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PhotosGridScreenPreview() {
    MarsPhotosTheme {
        PhotosGridScreen(List(10) { MarsPhoto("MarsPhoto::id: $it", "MarsPhoto::imgSrc") })
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