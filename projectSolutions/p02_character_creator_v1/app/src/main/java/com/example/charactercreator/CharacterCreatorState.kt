package com.example.charactercreator

import com.example.charactercreator.model.CharacterClass

enum class Stat(
    val iconId: Int,
) {
    STRENGTH(R.drawable.speed),
    ENDURANCE(R.drawable.strength),
    SPEED(R.drawable.endurance),
    FOCUS(R.drawable.focus),
}

data class CharacterCreatorState(
    val name: String = "",
    val characterClass: CharacterClass = CharacterClass.entries.first(),
    val description: String = "",
    val stats: Map<Stat, Int> =
        Stat.entries.associateWith { 0 },
) {
    val totalPoints: Int
        get() = stats.values.sum()

    val remainingPoints: Int
        get() = MAX_POINTS - totalPoints

    // Calculated character attributes
    val power: Int
        get() = 2 * stats.getValue(Stat.STRENGTH) + stats.getValue(Stat.SPEED)

    val toughness: Int
        get() = 2 * stats.getValue(Stat.ENDURANCE) + stats.getValue(Stat.SPEED)

    val cost: Int
        get() = ((power + toughness) / 4 - stats.getValue(Stat.FOCUS) / 2).coerceAtLeast(1)

    // Returns a new state with an updated stat,
    // enforcing the point allocation rules.
    fun updateStats(stat: Stat, delta: Int): CharacterCreatorState {
        if (delta > 0 && totalPoints >= MAX_POINTS) return this
        val currentValue = stats.getOrDefault(stat, 0)
        val newValue = (currentValue + delta).coerceAtLeast(0)
        if (newValue == currentValue) return this

        val newStats = stats.toMutableMap()
        newStats.set(stat, newValue)

        return copy(stats = newStats)
    }

    companion object {
        const val MAX_POINTS = 10
    }
}