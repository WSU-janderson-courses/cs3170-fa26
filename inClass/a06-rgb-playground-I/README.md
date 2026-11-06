# In-Class Exercise – RGB Playground

## Learning Objectives

By the end of this exercise, you should be able to:

- Break a user interface into smaller reusable composables.
- Pass data into composables using parameters.
- Pass behavior into composables using lambda functions.
- Store UI state using `remember` and `mutableStateOf`.
- Use a `data class` to group related state.
- Use `copy()` to create updated versions of immutable state objects.
- Use sliders to update individual RGB color channels.

---

## Overview

In this exercise, we will build an **RGB Playground** using Jetpack Compose.

The app will let the user:

1. Select a preset color: **Red**, **Green**, or **Blue**.
2. Adjust the individual **red**, **green**, and **blue** channels using sliders.
3. See the background color update automatically as the state changes.

Rather than putting the entire interface into one composable, we will divide the app into smaller reusable components.

The finished app will have approximately this structure:

```text
ColorSelectorApp
│
├── ColorSelector
│   ├── ColorOption
│   ├── ColorOption
│   └── ColorOption
│
└── ColorSliders
    ├── ColorSlider
    ├── ColorSlider
    └── ColorSlider
```

Some of this structure is included in the starter code. You will complete the remaining pieces and connect them together.

---

# Starter Code

The starter project already contains several pieces that we will use throughout the exercise.

## `ColorChoice`

A `ColorChoice` groups together a color's name and its actual Compose `Color`.

```kotlin
data class ColorChoice(
    val name: String,
    val color: Color
)
```

Because both properties are declared with `val`, a `ColorChoice` is treated as immutable.

Instead of changing an existing `ColorChoice`, we will create a new one whenever its state changes.

---

## `colorFromString`

The starter code also contains a helper function that converts one of our color names into a Compose `Color`.

```kotlin
fun colorFromString(colorName: String): Color {
    return when (colorName) {
        "Red" -> Color.Red
        "Green" -> Color.Green
        "Blue" -> Color.Blue
        else -> Color.Unspecified
    }
}
```

---

## Preset Colors

The available preset colors are stored in a list:

```kotlin
val colorOptions = listOf(
    "Red",
    "Green",
    "Blue"
)
```

We will use this list to create our preset color controls.

---

# Part 1 – Examine `ColorOption`

The starter code includes a completed composable named `ColorOption`.

It represents one row containing:

- a `RadioButton`
- a `Text` label

```kotlin
@Composable
fun ColorOption(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(text = name)
    }
}
```

Notice that `ColorOption` does not store any application state itself.

Instead, it receives:

- `name` — the text to display
- `selected` — whether the radio button should be selected
- `onClick` — what should happen when the user selects the option

This pattern will be useful when we create the other composables in this exercise.

---

# Part 2 – Build the Preset Color Selector

Now we can use `ColorOption` to create a group of color choices.

## Step 1 – Complete `ColorSelector`

The starter code contains the following function:

```kotlin
@Composable
fun ColorSelector(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO
}
```

Inside `ColorSelector`, create a `Column`.

Use the provided `colorOptions` list to create a `ColorOption` for each available color.

Each option should:

- display the color name
- be selected when that color matches `selectedColor`
- call `onColorSelected` when clicked

The selected expression will look like:

```kotlin
selected = color == selectedColor
```

When the option is clicked, call:

```kotlin
onColorSelected(color)
```

The resulting structure should look approximately like:

```text
ColorSelector
├── ColorOption("Red")
├── ColorOption("Green")
└── ColorOption("Blue")
```

---

# Part 3 – Connect the Selector to App State

The starter code contains most of `ColorSelectorApp`.

It begins with state containing a `ColorChoice`:

```kotlin
var colorChoice by remember {
    mutableStateOf(
        ColorChoice(
            name = "Red",
            color = Color.Red
        )
    )
}
```

The app's background already uses:

```kotlin
colorChoice.color
```

Your next job is to add the `ColorSelector`.

## Step 2 – Add `ColorSelector`

Call `ColorSelector` from inside `ColorSelectorApp`.

The currently selected name should come from:

```kotlin
colorChoice.name
```

For now, when the user chooses a different color, replace `colorChoice` with a new `ColorChoice`.

For example:

```kotlin
colorChoice = ColorChoice(
    name = newColor,
    color = colorFromString(newColor)
)
```

Run the app.

You should now be able to select **Red**, **Green**, or **Blue**, and the background should change.

---

# Part 4 – Use `copy()`

Our preset color selector already works, but right now we create a completely new ColorChoice manually whenever the user chooses a color.

Because `ColorChoice` is a Kotlin `data class`, Kotlin provides a `copy()` function that lets us create a new object based on an existing one.

We will move the color-update logic into `ColorChoice` so that `ColorSelectorApp` does not need to know how a `ColorChoice` should be rebuilt.

---

## Step 3 – Add `withString`

Add the following member function to `ColorChoice`:

```kotlin
fun withString(newName: String): ColorChoice {
    return copy(
        name = newName,
        color = colorFromString(newName)
    )
}
```

This creates a **new** `ColorChoice` based on the current one, but replaces its `name` and `color`.

Now go back to `ColorSelectorApp`.

Replace the code that manually creates a new `ColorChoice` with:

```kotlin
colorChoice = colorChoice.withString(newColor)
```

Run the app again and make sure the preset buttons still work.

The difference is that the logic for creating an updated `ColorChoice` now belongs to the `ColorChoice` class itself. We will use this same pattern again when we update individual RGB channels.

---

# Part 5 – Build a Reusable Slider

Next, we will create a reusable composable representing one RGB channel.

Unlike `ColorOption`, this composable is **not completed for you**.

Use `ColorOption` as an example of how a small composable can receive both data and a function.

## Step 4 – Complete `ColorSlider`

The starter code contains:

```kotlin
@Composable
fun ColorSlider(
    name: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO
}
```

Create a `Row` containing:

- a `Text` displaying the channel name
- a `Slider`

For example:

```text
Red     ───────●────────
```

The slider should use:

```kotlin
value = value
```

and use the provided function when the value changes:

```kotlin
onValueChange = onValueChange
```

You may give the `Text` a fixed width so the three sliders line up.

For example:

```kotlin
Modifier.width(60.dp)
```

---

# Part 6 – Build the RGB Slider Group

Now that one RGB slider works, we can use it to build all three controls.

## Step 5 – Complete `ColorSliders`

The starter code contains:

```kotlin
@Composable
fun ColorSliders(
    color: Color,
    updateColor: (String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO
}
```

Create a `Column` containing three `ColorSlider` calls.

---

## Red Slider

The red slider should use:

```kotlin
color.red
```

for its value.

When it changes, call:

```kotlin
updateColor("Red", newValue)
```

For example:

```kotlin
ColorSlider(
    name = "Red",
    value = color.red,
    onValueChange = { newValue ->
        updateColor("Red", newValue)
    }
)
```

---

## Green Slider

Create another `ColorSlider` using:

```kotlin
color.green
```

and send:

```text
"Green"
```

to `updateColor`.

---

## Blue Slider

Create another `ColorSlider` using:

```kotlin
color.blue
```

and send:

```text
"Blue"
```

to `updateColor`.

Your component structure should now look like:

```text
ColorSliders
├── ColorSlider("Red")
├── ColorSlider("Green")
└── ColorSlider("Blue")
```

---

# Part 7 – Update One RGB Channel

The Android `Color` class also behaves like an immutable value.

We cannot directly change:

```kotlin
color.red
```

Instead, we create a copy of the `Color` with one channel replaced.

For example:

```kotlin
color.copy(red = newValue)
```

We will put this logic inside `ColorChoice`.

---

## Step 6 – Add `withColorChannel`

Add a new member function to `ColorChoice`:

```kotlin
fun withColorChannel(
    channel: String,
    newValue: Float
): ColorChoice {
    val newColor = when (channel) {
        "Red" -> color.copy(red = newValue)
        "Green" -> color.copy(green = newValue)
        "Blue" -> color.copy(blue = newValue)
        else -> color
    }

    return copy(
        name = "Custom",
        color = newColor
    )
}
```

Notice that this creates:

1. a new `Color`
2. a new `ColorChoice`

The original objects are not modified.

---

# Part 8 – Connect the Sliders

The last step is to connect `ColorSliders` to the application's state.

## Step 7 – Add `ColorSliders`

Inside `ColorSelectorApp`, add:

```kotlin
ColorSliders(
    color = colorChoice.color,
    updateColor = { channel, value ->
        colorChoice =
            colorChoice.withColorChannel(channel, value)
    }
)
```

Run the app.

Moving any of the three sliders should immediately change the background color.

Once one of the sliders is changed, the color's name becomes:

```text
Custom
```

If you select one of the preset colors again, the color should return to that preset.

---

# Finished Structure

Your finished app should have approximately this structure:

```text
ColorSelectorApp
│
│   colorChoice state
│
├── ColorSelector
│   │
│   ├── ColorOption("Red")
│   ├── ColorOption("Green")
│   └── ColorOption("Blue")
│
└── ColorSliders
    │
    ├── ColorSlider("Red")
    ├── ColorSlider("Green")
    └── ColorSlider("Blue")
```

The important thing to notice is where the application state is stored.

`ColorSelectorApp` owns the current `ColorChoice`.

The smaller composables receive the information they need through parameters.

When the user interacts with one of those composables, it calls a function provided by its parent.

A useful way to think about this is:

```text
Values move down through parameters.

Events move up through functions.
```

---

# Optional Enhancements

Once the required version works, you can experiment with additional features.

Possible additions include:

- Display the current numerical RGB values.
- Add a **Reset** button.
- Give each slider a track matching its RGB channel.
- Add additional preset colors.
- Add a `Text` displaying the current color name.
- Replace the preset radio buttons with a `DropdownMenu`.
- Allow custom colors to be saved and named.

These should be treated as additions to the working version rather than replacements for the required functionality.

---

# Summary

In this exercise, we:

- Used an existing reusable composable.
- Created additional reusable composables following the same pattern.
- Combined small composables into larger UI components.
- Stored state using `remember` and `mutableStateOf`.
- Passed values into child composables.
- Passed events back to parent composables using lambda functions.
- Used a Kotlin `data class` to represent application state.
- Used `copy()` to create updated immutable objects.
- Manipulated individual RGB channels using sliders.

The main Compose pattern from this exercise is:

```text
State lives near the top.

Values move down.

Events move up.
```

---

## Grading


| Category | Points | Criteria |
|---|---:|---|
| **Color Selector** | 2 | `ColorSelector` correctly creates the Red, Green, and Blue options using the provided `ColorOption`, and the selected option updates correctly. |
| **Preset Color State** | 1 | Selecting a preset color correctly updates the `ColorChoice` and changes the background. |
| **ColorSlider Composable** | 2 | `ColorSlider` correctly displays a label and slider, uses the provided `value`, and calls `onValueChange` when changed. |
| **ColorSliders Composable** | 2 | Creates working Red, Green, and Blue sliders using `ColorSlider` and passes the correct channel/value to `updateColor`. |
| **RGB State Updates** | 2 | `withColorChannel()` correctly creates an updated color and the background changes as sliders move. |
| **Completion / Code Quality** | 1 | Project builds and runs, required functionality is present, and the submitted code is reasonably organized. |
| **Total** | **10** | |

---

# Turn-In

Before submitting:

- Make sure the project builds and runs.
- Test all three preset colors.
- Test all three RGB sliders.
- Make sure changing a preset updates the background.
- Make sure changing a slider updates the background.
- Commit your completed work.
- Push your commits to your assignment repository.