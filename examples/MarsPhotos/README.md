# Mars Photos — CS 3170 live demo starter

This Android Studio project is intentionally incomplete **but should compile and run**.
It initially shows an empty photo result. Use `LIVE_DEMO.md` to complete the exercise.

## What has already been done

- Android app/theme/top app bar and the `MainActivity` entry point
- INTERNET permission
- Retrofit, Kotlin serialization, and Coil dependencies
- Retrofit builder, JSON converter, and reusable service object
- Loading, Success, Error UI states and a simple screen scaffold
- A working Retry button and a ViewModel (the network call itself is left for class)

## Live-coding tasks

1. `network/MarsPhoto.kt`: add `@Serializable` and `@SerialName("img_src")`.
2. `network/MarsApiService.kt`: add `@GET("photos")` to the request function.
3. `ui/screens/MarsViewModel.kt`: implement `getMarsPhotos()` using `viewModelScope.launch`.
4. `ui/screens/HomeScreen.kt`: display the first photo with Coil `AsyncImage`.
5. **Optional:** turn the single photo into a `LazyVerticalGrid`.

## Run

1. Unzip, then open this folder (containing `settings.gradle.kts`) in Android Studio.
2. Sync Gradle; choose an emulator or device with internet access.
3. Run the app and verify it displays **Loaded 0 Mars photo records**.
4. Follow `LIVE_DEMO.md` one milestone at a time.

The base API is https://android-kotlin-fun-mars-server.appspot.com/photos .
It returns an array of objects containing `id` and `img_src`.

**Teaching scope:** This version intentionally omits the repository, application container,
custom ViewModel factory, and unit-test scaffolding from the longer Google tutorial.
The original complete project remains the reference for those architecture topics.
