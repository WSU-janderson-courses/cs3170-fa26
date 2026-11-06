# In-Class 04: Business Card
## Overview

In this activity, you will use Jetpack Compose to create an app that displays a simple business card.

You will begin with a starter project containing the Android logo and some of the project setup. First, you will work on your own or with nearby classmates. We will then come back together and walk through one possible way to organize and build the interface.

There is not one required visual design. Your finished app should contain the required information and layout elements, but you may make your own choices about colors, spacing, fonts, and images.

## Business Card Contents

Your business card should display:

- An image
- Your name
- A title or short description
- Three pieces of contact information
- An icon beside each contact item

You do not need to use your real phone number, email address, or social-media information. Fictional information is fine.

### 1. Import an Image

The starter project displays the Android logo. Replace it with an image of your choice.

1. Open the Resource Manager.
1. Select Add Resources and then Import Drawables.
1. Select an image from your computer and import it into the project.
1. Find the existing `Image` composable.
1. Replace `R.drawable.android_logo` with the resource name of your imported image.

Your resource filename must contain only lowercase letters, numbers, and underscores.

Use a `Modifier` to give the image an appropriate size.

### 2. Plan the Layout

Before writing the rest of the code, consider how the interface can be divided into sections.

One possible organization is:

A profile section containing the image, name, and title
A contact section containing the phone number, social information, and email address

Think about which parts should be placed inside a `Column` and which should be placed inside a `Row`.

### 3. Build the Profile Section

Create a section containing:

- The imported image
- Your name
- Your title or short description

Arrange these elements vertically and center them horizontally.

Use modifiers and `Text` parameters to customize at least some of the following:

- Image size
- Font size
- Font weight
- Text color
- Padding or spacing

### 4. Build the Contact Section

Create three contact rows. Each row should contain:

- One Material icon
- One piece of contact information

Arrange the icon and text horizontally using a `Row`.

If possible, create a reusable composable such as:

```kotlin
@Composable
fun ContactRow(
icon: ImageVector,
text: String,
modifier: Modifier = Modifier
)
```

Call this composable three times instead of rewriting the complete `Row` each time.

### 5. Arrange the Complete Card

Place the profile and contact sections inside an outer layout.

Use the outer layout to control:

- The location of the two sections
- Horizontal alignment
- Vertical arrangement
- The background color
- Padding around the contents

Your app should use at least:

- One `Column`
- One `Row`
- One `Image`
- Three `Icon` composables
- Several `Text` composables
- Modifiers for size, padding, or background color
- An alignment or arrangement parameter

### 6. Customize and Test

Make at least two visual choices of your own. For example, you could:

- Change the background color
- Change the text or icon colors
- Change the font sizes or weights
- Adjust the spacing between elements
- Add another contact item
- Change the size or placement of the image

Run the app or use the Compose preview to check your work.

---

## Grading: 10 Points

|  Points  | Description                                                                                                                                                                                     |
|--------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|    10    | Completed the activity with a reasonable attempt at the required image, profile information, contact information, and Compose layout. Minor errors or unfinished visial details are acceptable  |
|    8     | Meaningful work was completed, but one major part of the business card is missing or not working                                                                                                |
|    5     | The project shows some progress, but substantial portiona of the activity are incomplete                                                                                                        |
|    0     | No submission, or the starter project was submitted with no meaningful changes                                                                                                                  |
---

## Submission

When you are finished:

1. Build and run your program.
2. Test it using different numbers of dice, sides, and rolls.
3. Commit your changes using a meaningful commit message.
4. Push your changes to GitHub.

Your work is not submitted until your latest commit has been pushed to GitHub.