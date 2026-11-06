package com.example.charactercard

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class CharacterClass(
    @StringRes val nameResId: Int,
    @ColorRes val frameColorResId: Int,
    @DrawableRes val iconResId: Int
) {
    DUMMY(
        R.string.dummy_class_name,
        R.color.dummy_color,
        R.drawable.dummy_icon
    ),

    DRINKY(
        nameResId = R.string.drinky_class,
        frameColorResId = R.color.borderColor,
        iconResId = R.drawable.outline_moon_stars_24
    )

    // Add your character class
}

data class Character(
    val name: String,
    val characterClass: CharacterClass,
    val description: String,
    val cost: Int,
    val power: Int,
    val toughness: Int,
    @DrawableRes val artworkResId: Int,
    @DrawableRes val characterIconResId: Int
)