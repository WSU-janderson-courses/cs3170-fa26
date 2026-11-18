# CS 3170 — Connect to the Internet: Mars Photos

**In-class live demo** · Approximately 55 minutes · Jetpack Compose / Kotlin

> This walkthrough is adapted from `11_ConnectToInternet.pptx` and the completed
> `MarsPhotosWorking` Android Studio project. It deliberately omits repository /
> dependency-injection / testing implementation from the original slide sequence.
> Students begin with a compilable Android Studio project and complete the marked steps.

## Learning goals

By the end, students should be able to:

- Explain a **client → HTTP GET → server → JSON response** workflow.
- Map JSON field names to Kotlin properties using Kotlin serialization.
- Make a suspending Retrofit call from a ViewModel using `viewModelScope.launch`.
- Reflect Loading, Success, and Error in a Compose UI.
- Load a remote image with Coil's `AsyncImage`.
- **If time allows:** display multiple images using `LazyVerticalGrid`.

## Preparation (before class, already in starter)

The starter project already includes the INTERNET permission in `AndroidManifest.xml`,
Gradle dependencies for Retrofit, `converter-kotlinx-serialization`, Kotlin serialization,
and Coil 2, and a Retrofit builder in `network/MarsApiService.kt`.
It also includes the ViewModel, Compose `Scaffold`, and loading/error/retry screens.
The repository / app container / custom `Application` / ViewModel factory have been removed
so the demo concentrates on network I/O. Retrofit 3's official serializer replaces the
older archived Jake Wharton converter in the original project.

**Instructor preflight:** open and run the starter on the same computer/emulator used in class.
The expected initial message is **Loaded 0 Mars photo records**. Open the API endpoint in
an ordinary browser to check availability:

https://android-kotlin-fun-mars-server.appspot.com/photos

**Student files to edit:**

- `app/src/main/java/com/example/marsphotos/network/MarsPhoto.kt`
- `app/src/main/java/com/example/marsphotos/network/MarsApiService.kt`
- `app/src/main/java/com/example/marsphotos/ui/screens/MarsViewModel.kt`
- `app/src/main/java/com/example/marsphotos/ui/screens/HomeScreen.kt`

## 1. What's being downloaded? (5 minutes)

Start with the API in a browser; don't start by typing Gradle configuration.

```text
GET https://android-kotlin-fun-mars-server.appspot.com/photos
```

Simplified response (the actual endpoint returns many photos):

```json
[
  { "id": "424905", "img_src": "https://mars.jpl.nasa.gov/...jpg" },
  { "id": "424906", "img_src": "https://mars.jpl.nasa.gov/...jpg" }
]
```

Ask: **Is this JSON an object or an array? What is the type of each element?**
The response is an array; each element has an `id` and an image URL.
Retrofit downloads/parses the JSON *metadata*. Coil later downloads the actual JPEG data.
Those are **two distinct network requests**, not one.

**Slide connections:** original slides 2–5 and 14.

## 2. Deserialize JSON into a Kotlin class (5 minutes)

Open `network/MarsPhoto.kt`. It begins as an ordinary `data class`.
Add the imports and annotations:

```kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName("img_src") val imgSrc: String
)
```

**Explain:** `@Serializable` allows the JSON converter to instantiate `MarsPhoto`.
`@SerialName` maps the server field `img_src` onto the Kotlin property `imgSrc`.
The complete endpoint returns `List<MarsPhoto>`.

**Slide connection:** original slide 16 (skip the old dependency-install slide 15).

## 3. Describe the HTTP request (5 minutes)

Open `network/MarsApiService.kt`. The Retrofit builder, base URL, and converter
are already configured. Point out `BASE_URL` and the `MarsApi` singleton,
then add `@GET("photos")` to the interface:

```kotlin
interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}
```

**Explain:** `@GET("photos")` adds `photos` to the base URL.
`suspend` means callers invoke this from a coroutine or another suspend function.
The `MarsApi.retrofitService` object gives us the service implementation created by Retrofit.

**Slide connections:** original slides 6–9 and 17; skip live typing the Retrofit builder.

## 4. Request photos from the ViewModel (15 minutes)

Open `ui/screens/MarsViewModel.kt`. The starter already provides the sealed UI state:

```kotlin
sealed interface MarsUiState {
    data object Loading : MarsUiState
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    data object Error : MarsUiState
}
```

The `init` block already calls `getMarsPhotos()`. Replace only the body of
`getMarsPhotos()` with:

```kotlin
fun getMarsPhotos() {
    marsUiState = MarsUiState.Loading
    viewModelScope.launch {
        try {
            val photos = MarsApi.retrofitService.getPhotos()
            marsUiState = MarsUiState.Success(photos)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            marsUiState = MarsUiState.Error
        }
    }
}
```

Add the import `kotlinx.coroutines.CancellationException` (the `launch` import is already present).
Change the initial ViewModel state to `MarsUiState.Loading` if desired; either way,
`getMarsPhotos()` sets it before launching the network call.

**Run checkpoint:** the app should now say **Loaded [number] Mars photo records**
(the exact count depends on the server). Beneath it, the starter displays the URL of the
first image as text. This proves the network and deserialization steps work **before** Coil.

**Discuss:** `viewModelScope` keeps work tied to the ViewModel; a network request must
not block the UI. If the request fails, show Error instead of crashing. Cancellation
should be rethrown rather than treated as an ordinary failure.

**Slide connections:** original slides 9–13, 17. The starter already contains the
Loading/Error composables, so students can focus on the request.

## 5. Turn an image URL into an actual image (10 minutes)

Open `ui/screens/HomeScreen.kt`. Find the `MarsPhotoCard` composable and replace
its placeholder `Card` body with Coil's `AsyncImage`:

```kotlin
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
```

Android Studio can help add these imports:

```kotlin
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.marsphotos.R
```

**Run checkpoint:** the screen should show one remote Mars photo instead of its URL.
The existing `PhotosGridScreen` deliberately displays only `photos.first()`; we
change that in the optional next step.

**Explain:** Retrofit loads the *list of URLs*; Coil downloads, decodes,
loads, and caches the images. `placeholder` appears while an image loads;
`error` is shown when an individual image fails.

**Slide connections:** original slides 28–29. The starter includes the image resources.

## 6. Optional / stretch: Show a grid (10 minutes)

If time allows, replace `PhotosGridScreen` in `HomeScreen.kt` with:

```kotlin
@Composable
fun PhotosGridScreen(
    photos: List<MarsPhoto>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(photos, key = { it.id }) { photo ->
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
```

Imports:

```kotlin
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
```

**Run checkpoint:** a scrollable grid of downloaded images.
Note the `key = { it.id }` and `GridCells.Adaptive(150.dp)` behavior as you resize
or rotate the emulator. Depending on internet/image-server reliability, a few
individual image requests might fail even when the JSON request succeeds.

**Slide connections:** original slides 30–32.

## 7. If time remains: Error and Retry (5 minutes)

Toggle airplane mode / disable the emulator's network, run the request,
and observe the Error screen. Restore connectivity and press **Retry**.

The `HomeScreen` error UI and `onRetry` callback are supplied in the starter;
students don't need to write the retry button during the main demo.
Discuss that the API request could succeed while one image request fails,
and these are distinct error paths.

**Slide connection:** original slide 33.

## Time budget and stopping points

| Segment | Time | Minimum outcome |
| --- | ---: | --- |
| Inspect API JSON | 5 min | Recognize JSON array and URL field |
| `MarsPhoto` annotations | 5 min | Type-safe data model |
| `@GET` Retrofit declaration | 5 min | Service method described |
| ViewModel request | 15 min | List downloaded; count appears |
| Coil `AsyncImage` | 10 min | **One Mars photo shown — core goal** |
| `LazyVerticalGrid` | 10 min | Stretch goal: gallery |
| Review / troubleshoot | 5 min | Explain two network stages |

**If running behind:** stop after Coil. One successfully rendered remote image
already satisfies the main learning goal. Do not add repository/DI or tests in class.

## What we deliberately postponed from the original slides

- **Slides 18–23:** repository, dependency injection, `AppContainer`, custom
  `Application`, and ViewModel factory. These are useful for larger apps but distract
  from the network/Coil demonstration; the simplified ViewModel uses `MarsApi` directly.
- **Slides 24–27 and 34:** fake API service, repository tests, ViewModel test dispatcher.
  Good follow-up lab, but not a 55-minute demo.
- **Slide 15:** version numbers/dependency syntax from the original deck are superseded
  by the preconfigured `libs.versions.toml` and official Retrofit converter.
- **Slide 8:** singleton discussion is kept short: students use `MarsApi.retrofitService`,
  rather than building Retrofit in each request.

## Troubleshooting notes

- **`Unable to create converter for List<MarsPhoto>` / missing serializer:** check
  `@Serializable`, Kotlin serialization plugin, and `@SerialName`.
- **`HTTP method annotation is required`:** check `@GET("photos")`.
- **`IllegalArgumentException: baseUrl must end in /`:** `BASE_URL` should end with `/`
  (already fixed in the starter).
- **Blank screen / no photos:** check emulator connectivity and Logcat;
  check `INTERNET` permission. The initial starter intentionally shows zero photos.
- **Photos count displays but images are broken:** JSON loaded, but Coil's separate
  image requests may have failed; test an `img_src` URL in a browser.
- **Imports unresolved:** make sure you're editing the `network` and `ui/screens`
  packages supplied in the project and that Gradle sync completed.

## Resources

- Original material: `11_ConnectToInternet.pptx` (34 slides)
- Original solution: `MarsPhotosWorking.zip`
- Google Android Basics: https://developer.android.com/courses/android-basics-compose/unit-5
- API: https://android-kotlin-fun-mars-server.appspot.com/photos
