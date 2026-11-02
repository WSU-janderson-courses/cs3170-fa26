# Navigate Between Screens — Live-Coding Guide

**CS 3170 · Mobile Application Development**  
**Demo:** Cupcake app · Jetpack Compose Navigation  
**Based on:** `9_NavigateBetweenScreens.pptx` (25 slides)

[Android's original Navigation codelab](https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation)

> **Starting point:** Open the *Cupcake starter app*, which already has the UI composables (`StartOrderScreen`, `SelectOptionScreen`, `OrderSummaryScreen`), `OrderViewModel`, `OrderUiState`, and `DataSource`. We are **connecting** those screens, not recreating their layouts. The screenshots in the deck represent an older version of the codelab; adjust imports or existing parameter names if the starter has changed.
>
> **Suggested rhythm:** Explain the goal, type the small change, then run the app at the checkpoints. Some intermediate edits will not compile until the call sites and previews are updated.

## Roadmap

```text
Start ── choose quantity ──▶ Flavor ── Next ──▶ Pickup ── Next ──▶ Summary
                          │                     │                      │
                          └────── Cancel ───────┴────── Cancel ────────┘
                                       │
                                       ▼
                              Start (order reset)

Summary ── Send Order ──▶ Android Sharesheet (another app)

App bar: screen-specific title + Up button when there is a previous screen
```

**Live-demo goals:**

1. Define destinations and place them inside a `NavHost`.
2. Send UI events *out* of each screen using lambda callbacks.
3. Handle forward navigation, cancellation, and the back stack centrally.
4. Share an order using an Android `Intent`.
5. Update the app bar from the active route.

---

## 1. Introduce navigation (slides 2–4)

**Explain while showing the starter:**

- A real app typically has multiple screens, but each of our screens is still a composable.
- `NavController` decides which destination to display and maintains navigation history.
- A **navigation graph** associates destinations (routes) with composable content.
- `NavHost` renders the composable matching the current destination.
- A **route** is a unique identifier for a destination. Here we use strings derived from an enum.

**Ask:** “If there are four different composables, what decides which one the user sees?”

## 2. Declare the four destinations (slide 5)

**File:** `CupcakeScreen.kt` (or wherever the starter keeps its screen enum).

Create/update the enum:

```kotlin
enum class CupcakeScreen {
    Start,
    Flavor,
    Pickup,
    Summary
}
```

**Explain:** `CupcakeScreen.Start.name` is the string `"Start"`. Each name will become a route, not a screen object.

**Trace the flow:** Start = quantity, Flavor = choice, Pickup = date, Summary = review/send/cancel.

## 3. Give `CupcakeApp` a navigation controller (slides 6–7)

**File:** The file containing `CupcakeApp()` (named `CupcakeScreen.kt` in the slide discussion; the starter may use a different filename).

Useful imports (Android Studio can add these as you type):

```kotlin
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
```

Add a navigation controller to the `CupcakeApp` parameters:

```kotlin
@Composable
fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Existing app layout
}
```

Inside the existing `Scaffold` content, collect the ViewModel state and replace the old fixed-screen content with `NavHost`:

```kotlin
Scaffold(
    topBar = {
        CupcakeAppBar(
            canNavigateBack = false,
            navigateUp = { /* We will implement this later. */ }
        )
    }
) { innerPadding ->
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = CupcakeScreen.Start.name,
        modifier = Modifier.padding(innerPadding)
    ) {
        // Destination composables go here.
    }
}
```

Keep the starter's existing theme, imports, and any `Scaffold` styling. We will update the app bar in **Step 11**.

**Point out the three key parameters:**

- `navController` — which controller operates this host?
- `startDestination` — what route appears first?
- `modifier` — keep the content out from under the app bar using `innerPadding`.

At this point the host is empty: **we have a container, but no destinations**.

## 4. Register the four screens (slides 8–12)

Each destination follows the same pattern:

```kotlin
composable(route = CupcakeScreen.Start.name) {
    // Render the UI for this destination.
}
```

Add the following **inside the `NavHost { ... }` block**, one destination at a time.

### Start: select a quantity (slide 9)

```kotlin
composable(route = CupcakeScreen.Start.name) {
    StartOrderScreen(
        quantityOptions = DataSource.quantityOptions,
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    )
}
```

**Explain:** `DataSource.quantityOptions` supplies choices. The composable handles drawing the buttons. Nothing navigates yet.

### Flavor: select a flavor (slide 10)

```kotlin
composable(route = CupcakeScreen.Flavor.name) {
    val context = LocalContext.current
    SelectOptionScreen(
        subtotal = uiState.price,
        options = DataSource.flavors.map { id ->
            context.resources.getString(id)
        },
        onSelectionChanged = { viewModel.setFlavor(it) },
        modifier = Modifier.fillMaxHeight()
    )
}
```

**Explain:** `DataSource.flavors` holds string resource IDs; `LocalContext.current` lets us resolve those IDs into the actual strings shown in the UI. Choosing a value updates the shared ViewModel.

### Pickup: select a date (slide 11)

```kotlin
composable(route = CupcakeScreen.Pickup.name) {
    SelectOptionScreen(
        subtotal = uiState.price,
        options = uiState.pickupOptions,
        onSelectionChanged = { viewModel.setDate(it) },
        modifier = Modifier.fillMaxHeight()
    )
}
```

**Ask:** “Why can we use the *same* `SelectOptionScreen` for two different destinations?” (Its data and selection callback differ.)

### Summary: review the order (slide 12)

```kotlin
composable(route = CupcakeScreen.Summary.name) {
    OrderSummaryScreen(
        orderUiState = uiState,
        modifier = Modifier.fillMaxHeight()
    )
}
```

**Checkpoint:** All four destinations are registered. Run the app if the screen signatures still compile. It should show **Start only**: we have not hooked up the buttons yet.

---

## 5. Keep navigation logic outside individual screens (slide 13)

**Say:** “We *could* pass `navController` into every screen. Instead, the screen will report **what the user did**, and `CupcakeApp` will decide **where to go**.”

This produces a clean separation:

```text
Button inside screen ─▶ callback with data ─▶ CupcakeApp
                                           ├─ update ViewModel
                                           └─ navigate to route
```

The screen UI is more reusable and easier to preview or test. Navigation decisions stay in one place.

## 6. Add callbacks to the screen composables (slides 14–16)

> **Build warning:** New required parameters will temporarily produce errors at old call sites and `@Preview` functions. Update those callers before the next run.

### `StartOrderScreen`: report the chosen quantity (slide 14)

Add this parameter to the function:

```kotlin
@Composable
fun StartOrderScreen(
    quantityOptions: List<Pair<Int, Int>>,
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Existing UI
}
```

In the loop that creates the quantity buttons, send the number to the callback:

```kotlin
quantityOptions.forEach { item ->
    SelectQuantityButton(
        labelResourceId = item.first,
        onClick = { onNextButtonClicked(item.second) }
    )
}
```

**Explain:** `item.first` is the label resource ID, and `item.second` is the numeric quantity. The screen knows *which quantity*, but not *which screen comes next*.

In `StartOrderPreview()`, provide a no-op callback:

```kotlin
StartOrderScreen(
    quantityOptions = DataSource.quantityOptions,
    onNextButtonClicked = {},
    modifier = Modifier.fillMaxSize()
)
```

### `SelectOptionScreen`: report Next and Cancel (slide 15)

Add the two callbacks, retaining the existing parameters:

```kotlin
@Composable
fun SelectOptionScreen(
    subtotal: String,
    options: List<String>,
    onSelectionChanged: (String) -> Unit = {},
    onCancelButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Existing UI
}
```

Wire the existing buttons to the callbacks:

```kotlin
OutlinedButton(
    modifier = Modifier.weight(1f),
    onClick = onCancelButtonClicked
) {
    Text("Cancel")
}

Button(
    modifier = Modifier.weight(1f),
    enabled = selectedValue.isNotEmpty(),
    onClick = onNextButtonClicked
) {
    Text("Next")
}
```

**Explain:** The selection stays local to the screen until it is reported with `onSelectionChanged`. The *Next* button remains disabled until a choice has been made.

> The slide images show `stringResource("Cancel")` and `stringResource("Next")`; in actual Kotlin, `stringResource()` takes a **resource ID**, not a string literal. The snippets above use plain `Text()` to remain self-contained. Use `stringResource(R.string.… )` if those resources exist in your starter.

Update any previews to provide no-op `onCancelButtonClicked = {}` and `onNextButtonClicked = {}`.

### `OrderSummaryScreen`: report Send and Cancel (slide 16)

Add these parameters:

```kotlin
@Composable
fun OrderSummaryScreen(
    orderUiState: OrderUiState,
    onCancelButtonClicked: () -> Unit,
    onSendButtonClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Existing UI
}
```

Connect the existing Send/Cancel buttons. The screen already prepares the summary variables (`newOrder` and `orderSummary` in the slides):

```kotlin
Button(
    modifier = Modifier.fillMaxWidth(),
    onClick = { onSendButtonClicked(newOrder, orderSummary) }
) {
    Text("Send Order to Another App")
}

OutlinedButton(
    modifier = Modifier.fillMaxWidth(),
    onClick = onCancelButtonClicked
) {
    Text("Cancel")
}
```

In `OrderSummaryPreview()`, add the no-op callback arguments:

```kotlin
onCancelButtonClicked = {},
onSendButtonClicked = { subject: String, summary: String -> }
```

**Ask:** “What two pieces of information are passed to Send?” (The subject and the order summary.)

## 7. Connect forward navigation in `CupcakeApp` (slide 17)

**Explain:** `navController.navigate(route)` pushes a destination onto the back stack and shows it.

Return to the `NavHost` and update the **calls** to the screens. These are the changes to make; keep the `modifier`, selection callbacks, and other arguments from Step 4.

### Start → Flavor

```kotlin
StartOrderScreen(
    quantityOptions = DataSource.quantityOptions,
    onNextButtonClicked = { cupcakeQuantity ->
        viewModel.setQuantity(cupcakeQuantity)
        navController.navigate(CupcakeScreen.Flavor.name)
    },
    modifier = Modifier
        .fillMaxSize()
        .padding(dimensionResource(R.dimen.padding_medium))
)
```

**Call out the order:** Update the quantity in the shared ViewModel **before** navigating.

### Flavor → Pickup

```kotlin
SelectOptionScreen(
    subtotal = uiState.price,
    options = DataSource.flavors.map { id -> context.resources.getString(id) },
    onSelectionChanged = { viewModel.setFlavor(it) },
    onNextButtonClicked = {
        navController.navigate(CupcakeScreen.Pickup.name)
    },
    onCancelButtonClicked = { /* Implement in Step 8 */ },
    modifier = Modifier.fillMaxHeight()
)
```

This is inside `composable(route = CupcakeScreen.Flavor.name)` where `context` was defined.

### Pickup → Summary

```kotlin
SelectOptionScreen(
    subtotal = uiState.price,
    options = uiState.pickupOptions,
    onSelectionChanged = { viewModel.setDate(it) },
    onNextButtonClicked = {
        navController.navigate(CupcakeScreen.Summary.name)
    },
    onCancelButtonClicked = { /* Implement in Step 8 */ },
    modifier = Modifier.fillMaxHeight()
)
```

### Summary: callback placeholders for now

```kotlin
OrderSummaryScreen(
    orderUiState = uiState,
    onCancelButtonClicked = { /* Implement in Step 8 */ },
    onSendButtonClicked = { _, _ -> /* Implement in Step 10 */ },
    modifier = Modifier.fillMaxHeight()
)
```

**Checkpoint — Run the app.** Choose a quantity, flavor, then pickup date. You should arrive at Summary, and the order details should match your selections. The callbacks for Cancel/Send are intentionally unfinished at this stage.

---

## 8. Understand the back stack and implement Cancel (slides 18–19)

Draw this on the board as the user advances:

```text
After Start:             [Start]
After choosing quantity: [Start, Flavor]
After choosing flavor:   [Start, Flavor, Pickup]
After choosing pickup:   [Start, Flavor, Pickup, Summary]
```

**Explain:** Ordinary Back/Up removes the current destination. Cancel should do something stronger: clear the order and jump all the way back to **Start**.

The API is:

```kotlin
navController.popBackStack(route, inclusive)
```

- `route` — the destination we want to return to.
- `inclusive = false` — keep that destination in the stack.
- `inclusive = true` — remove that destination too.

Add the helper shown in the slides, outside `CupcakeApp`:

```kotlin
private fun cancelOrderAndNavigateToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(
        route = CupcakeScreen.Start.name,
        inclusive = false
    )
}
```

Replace the three Cancel placeholders (Flavor, Pickup, Summary) with:

```kotlin
onCancelButtonClicked = {
    cancelOrderAndNavigateToStart(viewModel, navController)
}
```

**Checkpoint — Run the app.** Navigate to Pickup or Summary, press Cancel, and verify that Start appears with the previous selections cleared. Press system Back from a later screen as a comparison: it only goes back one destination.

## 9. Introduce Android `Intent`s (slide 20)

**Explain:** Navigating inside Cupcake uses the **Navigation component**. Asking *another app* to handle an action uses an **Intent**.

For sharing:

- `Intent.ACTION_SEND` requests a share operation.
- `type = "text/plain"` says the payload is text.
- `Intent.EXTRA_SUBJECT` and `Intent.EXTRA_TEXT` carry the data.
- `Intent.createChooser(...)` opens the Android Sharesheet.

**Ask:** “Why don't we make a route for Gmail or Messages?” (Those are other apps; the operating system selects a compatible handler.)

## 10. Share the order (slides 21–22)

Useful imports:

```kotlin
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
```

Add this top-level helper in the app file:

```kotlin
private fun shareOrder(
    context: Context,
    subject: String,
    summary: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cupcake_order)
        )
    )
}
```

In the **Summary** destination, get the current context and replace the Send placeholder:

```kotlin
composable(route = CupcakeScreen.Summary.name) {
    val context = LocalContext.current

    OrderSummaryScreen(
        orderUiState = uiState,
        onCancelButtonClicked = {
            cancelOrderAndNavigateToStart(viewModel, navController)
        },
        onSendButtonClicked = { subject: String, summary: String ->
            shareOrder(
                context = context,
                subject = subject,
                summary = summary
            )
        },
        modifier = Modifier.fillMaxHeight()
    )
}
```

**Checkpoint — Run the app.** Reach Summary and press *Send Order to Another App*. The Android Sharesheet should appear. Select a compatible app if the emulator offers one; you don't have to finish sending anything.

> If your starter doesn't have `R.string.new_cupcake_order`, add that string resource or replace `context.getString(...)` in `createChooser` with a short literal title such as `"Share cupcake order"`.

---

## 11. Make the app bar respond to navigation (slides 23–24)

**Goal:** The title changes with the active destination, and the Up button appears only when there is a previous destination.

### Give each route a title resource

Update `CupcakeScreen`:

```kotlin
import androidx.annotation.StringRes

enum class CupcakeScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Flavor(title = R.string.choose_flavor),
    Pickup(title = R.string.choose_pickup_date),
    Summary(title = R.string.order_summary)
}
```

**Explain:** `name` remains the route string; `title` is a string resource ID for the app bar.

### Update `CupcakeAppBar`

Add `currentScreen: CupcakeScreen` to its parameters, then update the title inside the existing `TopAppBar`:

```kotlin
title = {
    Text(stringResource(id = currentScreen.title))
}
```

Keep the app bar's existing colors and styling. Its navigation icon should follow this pattern (if the starter already has this logic, retain it):

```kotlin
navigationIcon = {
    if (canNavigateBack) {
        IconButton(onClick = navigateUp) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Navigate up"
            )
        }
    }
}
```

Android Studio can import `Icons`, `Icons.AutoMirrored.Filled.ArrowBack`, `Icon`, and `IconButton` as needed.

### Observe the current destination in `CupcakeApp`

Before `Scaffold`, add:

```kotlin
val backStackEntry by navController.currentBackStackEntryAsState()
val currentScreen = CupcakeScreen.valueOf(
    backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
)
```

Replace the top-bar call with:

```kotlin
CupcakeAppBar(
    currentScreen = currentScreen,
    canNavigateBack = navController.previousBackStackEntry != null,
    navigateUp = { navController.navigateUp() }
)
```

**Explain:** `currentBackStackEntryAsState()` is observable Compose state. When navigation changes it, Compose recomposes the app bar with the new title. `previousBackStackEntry` tells us whether Up should be visible.

**Checkpoint — Run the app.** The app bar should read Start / Flavor / Pickup / Summary as you advance. Up should be absent on Start and available on later screens. Pressing Up returns to the preceding screen without resetting the order.

---

## 12. Final end-to-end test (slide 25)

- [ ] App launches on the Start screen, with no Up button.
- [ ] Choosing a quantity takes the user to Flavor and updates the order.
- [ ] Next remains disabled until an option is selected (where implemented by the starter).
- [ ] Flavor → Pickup → Summary works; the summary reflects all selections.
- [ ] Up returns to the prior screen and preserves the in-progress order.
- [ ] Cancel from Flavor, Pickup, or Summary returns to Start and resets the order.
- [ ] Send from Summary opens the Android Sharesheet with the order text.
- [ ] The app-bar title tracks the destination.

### Closing questions

1. What is the difference between **`navigate()`** and **`popBackStack()`**?
2. Why do the screen composables receive **callbacks** instead of `NavHostController`?
3. Why does the `OrderViewModel` live at the `CupcakeApp` level?
4. Why do we need an **Intent** to share the order instead of another `composable(route = ...)`?

## Quick troubleshooting during the demo

| Symptom | Check |
|---|---|
| `NavHost` / `composable` unresolved | Add/import the `androidx.navigation:navigation-compose` dependency if the starter does not already include it. |
| Screen function reports missing parameters | Add the new lambdas to **every** caller, including `@Preview` functions. |
| Nothing happens when Next is tapped | Does the button call `onNextButtonClicked`? Does `CupcakeApp` pass a lambda with `navController.navigate(...)`? |
| App doesn't show the first screen | Is `CupcakeScreen.Start.name` the `startDestination`, and is its route registered? |
| Cancel returns to Start but old data remains | Call `viewModel.resetOrder()` before `popBackStack()`. |
| `stringResource()` error | Supply `R.string.some_name`, not a quoted string literal. |
| App-bar title never changes | Use `currentBackStackEntryAsState()` and pass the derived `currentScreen` to `CupcakeAppBar`. |
| Up button appears on Start | Check `navController.previousBackStackEntry != null`. |

---

**Instructor note:** This is an **incremental guide**, not a replacement for the complete starter source code. Keep the existing UI, state types, theme, `@Preview` wrappers, and resources; edit the highlighted integration points as you code. The original slides use a string-route/enum Navigation API, matching this walkthrough.
