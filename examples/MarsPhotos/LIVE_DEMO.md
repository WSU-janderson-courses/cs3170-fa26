# CS 3170 — Mars Photos: An Incremental Networking Demo

**Two class periods (about 55 minutes each)**  
Kotlin · Jetpack Compose · Retrofit · JSON serialization · coroutines · Coil 2

Based on the completed `MarsPhotosWorking.zip` project and `11_ConnectToInternet.pptx`. We follow the slides' original progression—**raw JSON → deserialized objects → one record → one image → photo grid**—without adding repository/container/factory/test infrastructure during the demo (those appear on original slides 18–27).

**Files:** `MarsPhotos_Day1_Starter_Incremental.zip` is the runnable starting point; `MarsPhotos_Raw_JSON_Checkpoint.zip` is the optional mid-Day-1 checkpoint; `MarsPhotos_Day2_Checkpoint_Incremental.zip` is the end-of-Day-1 result; `MarsPhotos_Instructor_Solution_Incremental.zip` is the completed app. Each ZIP contains this walkthrough as `LIVE_DEMO.md`.

**Before class:** Sync and run the starter. It deliberately shows *Loaded 0 Mars photo records*. `AndroidManifest.xml` already declares `INTERNET`, and Gradle dependencies are already added, **including converter-scalars**. Appendix A lists the exact aliases if you want to teach setup later.

## What we intentionally leave preconfigured

- Project/theme/`Scaffold`, `MarsPhotosApp()`, and the Compose screen scaffolding
- Gradle plugins and dependencies (Retrofit, Scalars converter, Kotlin serialization, Coil)
- Internet permission and drawable resources
- `MarsUiState.Loading/Success/Error` and the initial empty-list placeholder

## Files to edit live

| File | Live-coding change |
|---|---|
| `network/MarsApiService.kt` | Define raw String GET; later add typed GET and JSON converter |
| `ui/screens/MarsViewModel.kt` | Coroutines; first raw JSON state, then typed `Success` |
| `ui/screens/HomeScreen.kt` | Render JSON preview, then first record's two properties |
| `network/MarsPhoto.kt` | Add serialization annotations |
| Day 2: `ui/screens/HomeScreen.kt` | Coil image, adaptive grid, placeholders, retry |

---

# Day 1 — From HTTP JSON to Kotlin objects (55 minutes)

## 1. See the GET response in a browser (5 minutes)

Open <https://android-kotlin-fun-mars-server.appspot.com/photos>. Optionally open Chrome DevTools → **Network**, refresh, and find `Request Method: GET`, `Status: 200`, and the **Response** tab.

The response is a **JSON array** (square brackets) of objects (curly braces):

```json
[
  {"id": "424905", "img_src": "https://mars.jpl.nasa.gov/..."},
  {"id": "424906", "img_src": "https://mars.jpl.nasa.gov/..."}
]
```

These image URLs are illustrative. Explain that the API gives us **metadata containing image URLs**, not the image bytes themselves.

**Question:** If the browser can show this JSON, how can an Android application request the same response?

*Original slides 2–5, 14.*

## 2. Find the libraries and permission (4 minutes)

Open `app/build.gradle.kts`. The starter already contains:

```kotlin
implementation(libs.retrofit)
implementation(libs.retrofit.scalars)
implementation(libs.kotlinx.serialization.retrofit)
implementation(libs.kotlinx.serialization.json)
implementation(libs.coil.compose)
```

- `converter-scalars` allows Retrofit to return the response as a **String**.
- `converter-kotlinx-serialization` maps JSON into Kotlin objects once we add `@Serializable`.
- Coil turns image URLs into images (Day 2).

In `app/src/main/AndroidManifest.xml`, point out this line *inside `<manifest>`, outside `<application>`*:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

No runtime permission prompt is needed for `INTERNET`.

*Original slides 6, 10, 15.*

## 3. First request: ask Retrofit for a String (10 minutes)

Open the initially empty `network/MarsApiService.kt` and enter **all** of this:

```kotlin
package com.example.marsphotos.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"

interface MarsApiService {
    @GET("photos")
    suspend fun getRawPhotos(): String
}

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
```

Explain `@GET("photos")`, `suspend`, the trailing slash in `BASE_URL`, and why `by lazy` means the Retrofit service is created once on first access.

**At this point the app still shows 0 records; the ViewModel is not requesting data yet.**

*Original slides 7–9.*

## 4. Fetch raw JSON and show it on screen (12 minutes)

### In `MarsViewModel.kt`, add a RawJson state

Add this to the **existing** `sealed interface MarsUiState`:

```kotlin
data class RawJson(val json: String) : MarsUiState
```

Add imports:

```kotlin
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.network.MarsApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
```

Change the initial value of `marsUiState` to `MarsUiState.Loading` (keep `by mutableStateOf` and `private set`). Replace the existing `getMarsPhotos()` function:

```kotlin
fun getMarsPhotos() {
    marsUiState = MarsUiState.Loading
    viewModelScope.launch {
        try {
            val json = MarsApi.retrofitService.getRawPhotos()
            marsUiState = MarsUiState.RawJson(json)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            marsUiState = MarsUiState.Error
        }
    }
}
```

**Explain:** The network call suspends without freezing the UI; setting Compose state triggers recomposition. `CancellationException` must not be swallowed by the generic exception handler.

### In `HomeScreen.kt`, show a preview of the JSON

Add imports:

```kotlin
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.font.FontFamily
```

Inside the existing `when (marsUiState)` block, add this branch. **Do not replace the existing Loading, Error, or Success branches.**

```kotlin
is MarsUiState.RawJson -> {
    val preview = marsUiState.json.take(1600)
    val suffix = if (marsUiState.json.length > 1600) {
        "\n...\n(${marsUiState.json.length} characters total)"
    } else {
        ""
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Raw JSON response")
        Text(preview + suffix, fontFamily = FontFamily.Monospace)
    }
}
```

**Checkpoint — RUN:** The app should now show raw JSON, beginning with `[` and containing the keys `id` and `img_src`. We display the first 1,600 characters to avoid rendering a huge response in the Compose UI. The full response is still in `RawJson.json`.

If you're short on time, use `MarsPhotos_Raw_JSON_Checkpoint.zip` to reach this exact point.

*Original slides 9–13 (we defer detailed UI error behavior until Day 2).*

## 5. Map JSON objects to `MarsPhoto` (12 minutes)

### Make the Kotlin data class serializable

In `network/MarsPhoto.kt`, add the annotations and imports:

```kotlin
package com.example.marsphotos.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName("img_src") val imgSrc: String
)
```

Point to one JSON object in the browser: `id` maps directly, but `img_src` must be mapped to `imgSrc` using `@SerialName`.

### Give Retrofit another method for the *same* GET

In the existing `MarsApiService` interface, **keep** `getRawPhotos()` and add:

```kotlin
@GET("photos")
suspend fun getPhotos(): List<MarsPhoto>
```

Add the following imports to `MarsApiService.kt`:

```kotlin
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory
```

Update the existing Retrofit builder by **adding** the JSON converter **after** the Scalars converter:

```kotlin
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(
        Json { ignoreUnknownKeys = true }
            .asConverterFactory("application/json".toMediaType())
    )
    .build()
```

Why keep both? The Scalars converter handles methods returning `String`; the Kotlin serialization converter handles `List<MarsPhoto>`. Both methods call the same endpoint but Retrofit interprets the return type differently.

**Checkpoint:** App still shows raw JSON because we haven't changed the ViewModel call yet.

### Switch from a String to Kotlin objects

In `MarsViewModel.getMarsPhotos()` replace just these two lines:

```kotlin
val json = MarsApi.retrofitService.getRawPhotos()
marsUiState = MarsUiState.RawJson(json)
```

with:

```kotlin
val photos = MarsApi.retrofitService.getPhotos()
marsUiState = MarsUiState.Success(photos)
```

**Checkpoint — RUN:** The existing starter screen shows the count and the first photo record (currently formatted as one long line). We have gone from a `String` to `List<MarsPhoto>` without changing the HTTP URL.

*Original slides 14–17.*

## 6. Display the first object's fields (8 minutes)

In `HomeScreen.kt`, find `MarsPhotoCard` and replace its current `Card` block with:

```kotlin
Card(modifier = modifier) {
    Column(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("ID: ${photo.id}")
        Text("Image URL: ${photo.imgSrc}")
    }
}
```

`PhotosGridScreen` already uses `photos.first()` when the list isn't empty. It's safe because the starter checks `photos.isEmpty()` first.

**Checkpoint — RUN:** See **Loaded N Mars photo records**, followed by the first record's `id` and `imgSrc`. Compare the field names to the browser JSON. We haven't asked Coil to load an image yet.

**Optional 2-minute deep dive:** To show deserialization as an explicit operation before letting Retrofit do it for you, temporarily call `getRawPhotos()` again and write:

```kotlin
val rawJson = MarsApi.retrofitService.getRawPhotos()
val photos = Json { ignoreUnknownKeys = true }
    .decodeFromString<List<MarsPhoto>>(rawJson)
marsUiState = MarsUiState.Success(photos)
```

You'll need `import kotlinx.serialization.json.Json` in the ViewModel. Restore the two-line `getPhotos()` version afterward. This directly illustrates that converting JSON text to objects is a separate operation; the Retrofit converter automates it.

**Stop Day 1 here.** If class time runs short, the required finish line is the raw JSON checkpoint; the typed-result step can open Day 2. `MarsPhotos_Day2_Checkpoint_Incremental.zip` provides the typed-result state for students who need it.

*Original slides 16–17 and the pre-image portion of 28.*

---

# Day 2 — Display remote images with Coil (55 minutes)

## 1. Recap and run (5 min)

Use the end-of-Day-1 app or open `MarsPhotos_Day2_Checkpoint_Incremental.zip`.

- It should retrieve records and display the first photo **ID and image URL as text**.
- The JSON request succeeded, but the image itself has not been downloaded or rendered yet.

**Question:** Which library will turn the URL string into an image? (Coil.)

## 2. Display the first remote image (15 min)

Open `ui/screens/HomeScreen.kt`. Find `MarsPhotoCard`.

Add imports:

```kotlin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
```

Replace the `Card` content (keep its surrounding function signature):

```kotlin
Card(modifier = modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photo.imgSrc)
            .crossfade(true)
            .build(),
        contentDescription = "Mars photo ${photo.id}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}
```

**Checkpoint — RUN:** A single Martian landscape photo should appear. Some URLs supplied by the external API may fail independently of the list request. If an individual photo doesn't load, inspect Logcat or try another photo in the list before changing the Retrofit code.

**Explain:** Retrofit fetches *JSON containing URLs*; Coil makes a second HTTP request for a particular image and handles decode/cache/display. `ContentScale.Crop` fills the given bounds while cropping excess image area.

*Original slide: 28.*

## 3. Turn one image into a photo grid (15 min)

Still in `HomeScreen.kt`, add imports:

```kotlin
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
```

Replace `PhotosGridScreen` (the original has a `Column` and shows only `photos.first()`):

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
        items(items = photos, key = { it.id }) { photo ->
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

**Checkpoint — RUN:** Scroll through the photos. `LazyVerticalGrid` only composes visible items and nearby items. `GridCells.Adaptive(150.dp)` decides how many columns fit. `key = { it.id }` gives stable identities to the items.

**Student mini-experiment:** Change `minSize` from `150.dp` to `100.dp`, then to `200.dp`. What changes? Why does `aspectRatio(1.5f)` matter?

*Original slides: 30–32.*

## 4. Image placeholders and errors (8 min)

Add imports:

```kotlin
import androidx.compose.ui.res.painterResource
import com.example.marsphotos.R
```

Inside the `AsyncImage` call, add these arguments:

```kotlin
placeholder = painterResource(R.drawable.loading_img),
error = painterResource(R.drawable.ic_broken_image),
```

The images already exist in the starter's `res/drawable` directory.

**Explain the difference:** the application's whole-list network request has Loading/Error UI; each Coil image request can independently show its own placeholder or broken-image symbol.

*Original slide: 29.*

## 5. Add Retry to the error state (10 min)

The starter's `HomeScreen` already takes a callback `onRetry: () -> Unit`, and `MarsPhotosApp.kt` passes `marsViewModel::getMarsPhotos`. All that remains is a button.

Add the import:

```kotlin
import androidx.compose.material3.Button
```

Within `HomeScreen`, find the `MarsUiState.Error -> Column(...) { ... }` branch. Under the existing message, add:

```kotlin
Button(onClick = onRetry) {
    Text("Retry")
}
```

**Test:** With Wi-Fi/network disabled or airplane mode enabled, force-stop and relaunch the app. The full request should show Error. Reenable connectivity and tap **Retry**; the app should return to the gallery if the endpoint is available. (Depending on emulator state and caching, disabling connectivity may not always reproduce the same failure.)

**Instructor note:** The `onRetry` callback is a Compose example of passing behavior rather than passing the ViewModel directly into the reusable error screen.

*Original slides: 11–13, 33.*

## 6. Review (2 min)

Ask students: Why might the JSON count load successfully when some photo tiles display broken images? Which screen behavior should be controlled by the ViewModel, and which is local to Coil?

### If you're ahead of schedule

- Display each `photo.id` as a label below its image in a `Column`.
- Let students replace `GridCells.Adaptive` with `GridCells.Fixed(3)`.
- Show the Retrofit request path versus the image URL to emphasize two different HTTP requests.
- Discuss where a repository and dependency injection would fit **if the app became larger** (original slides 18–27).

---

# Appendix A — Dependencies, exactly as configured

**No student action is required for this demo.** The starter already has everything below. These directions are for lecture explanation or future reuse. The versions follow the supplied Android Studio project where practical, and replace its duplicated Retrofit and older third-party serialization converter entries.

### A1. Version catalog: `gradle/libs.versions.toml`

Relevant entries (do not replace the entire catalog with just these):

```toml
[versions]
kotlin = "2.2.21"
retrofit = "3.0.0"
kotlinxSerializationJson = "1.9.0"
coilCompose = "2.7.0"

[libraries]
retrofit = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }
retrofit-scalars = { module = "com.squareup.retrofit2:converter-scalars", version.ref = "retrofit" }
kotlinx-serialization-retrofit = { module = "com.squareup.retrofit2:converter-kotlinx-serialization", version.ref = "retrofit" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinxSerializationJson" }
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coilCompose" }

[plugins]
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

Notice that the catalog's `kotlinx-serialization-retrofit` alias becomes `libs.kotlinx.serialization.retrofit` in Gradle Kotlin DSL. This project intentionally uses **Coil 2.7.0**, matching the imports `coil.compose.AsyncImage` and `coil.request.ImageRequest` in your completed app. **Coil 3** uses different coordinates/imports and requires a separate network module for HTTP images; don't mix instructions from the two major versions.

### A2. App-level `app/build.gradle.kts`

Within `plugins { ... }`:

```kotlin
alias(libs.plugins.kotlin.serialization)
```

Within `dependencies { ... }`:

```kotlin
implementation(libs.retrofit)
implementation(libs.retrofit.scalars)
implementation(libs.kotlinx.serialization.retrofit)
implementation(libs.kotlinx.serialization.json)
implementation(libs.coil.compose)
```

Select **Sync Project with Gradle Files** after adding dependencies to an existing project. The original project uses Android Gradle Plugin 9.4.1 with its compatibility settings in `gradle.properties` (`android.builtInKotlin=false`, `android.newDsl=false`) because it still applies `org.jetbrains.kotlin.android`. The supplied starter retains those settings to avoid introducing a build-system migration during this lesson.

### A3. Internet permission: `app/src/main/AndroidManifest.xml`

Direct child of `<manifest>` and **before** `<application>`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### A4. Short dependency troubleshooting checklist

- **Unresolved `retrofit2` / `kotlinx.serialization`:** Gradle sync, then check plugin and aliases.
- **`Serializer for class 'MarsPhoto' is not found`:** Missing `@Serializable`, serialization plugin, or mismatched property annotation.
- **Retrofit base URL error:** It must end with `/`.
- **Missing Internet permission / network errors:** Verify `AndroidManifest.xml`, emulator connectivity, and the endpoint in a browser.
- **JSON loads but images are broken:** The image host may be unavailable; this isn't necessarily a Retrofit error.
- **Coil unresolved imports:** This starter uses **Coil 2**, so imports begin with `coil.compose` / `coil.request` instead of `coil3.*`.
- **Android Studio build-plugin errors:** Open this exact starter project; it retains the AGP / Kotlin compatibility settings of the supplied completed project.

## Source trail / slide mapping

- Day 1: original slides 2–17, **including** the scalar String response, then JSON conversion and one-record display.
- Not implemented in demo: original slides 18–27 (repository, dependency injection, ViewModel factory, local tests); discuss if desired.
- Day 2: original slides 28–33 (`AsyncImage`, placeholders, grids, Retry).
- Original slide 34 (updated ViewModel test) is omitted because tests were outside the two-class scope.

Official references: [Android — Get data from the internet](https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet) · [Android — Load and display images](https://developer.android.com/codelabs/basic-android-kotlin-compose-load-images) · [Retrofit](https://square.github.io/retrofit/) · [Coil](https://coil-kt.github.io/coil/).
