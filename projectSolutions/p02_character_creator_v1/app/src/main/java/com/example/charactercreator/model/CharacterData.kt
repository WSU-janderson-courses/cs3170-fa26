package com.example.charactercreator.model

import com.example.charactercreator.R

enum class CharacterClass(
    val nameResId: Int,
    val frameColorResId: Int,
    val iconResId: Int
) {
    ROGUE(
        nameResId = R.string.rogue,
        frameColorResId = R.color.rogue,
        iconResId = R.drawable.rogue
    ),

    MAGE(
        nameResId = R.string.mage,
        frameColorResId = R.color.mage,
        iconResId = R.drawable.mage
    ),

    PALADIN(
        nameResId = R.string.paladin,
        frameColorResId = R.color.paladin,
        iconResId = R.drawable.paladin
    ),

    WARRIOR(
        nameResId = R.string.warrior,
        frameColorResId = R.color.warrior,
        iconResId = R.drawable.warrior,
    )

}


data class CharacterData(
    val name: String,
    val characterClass: CharacterClass,
    val description: String,
    val cost: Int,
    val power: Int,
    val toughness: Int,
    val artworkResId: Int,
    val characterIconResId: Int
)