package org.agrfesta.k.kards.texasholdem.tournaments

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePayments

// info: https://pokersoup.com/tool/blindStructureCalculator

data class LevelPayments(val sb: UInt, val bb: UInt, val ante: UInt? = null)

data class IncreasingGamePaymentsDefinition(val structure: List<LevelPayments>, val gamesPerLevel: UInt) {
    init {
        require(gamesPerLevel >= 1u) { "Unable to create an IncreasingGamePayments, gamesPerLevel=$gamesPerLevel" }
        require(structure.isNotEmpty()) { "Unable to create an IncreasingGamePayments from an empty structure" }
    }

    fun generatePayments() = IncreasingGamePayments(this)
}

class IncreasingGamePayments(private val definition: IncreasingGamePaymentsDefinition): GamePayments {
    private var games = 0

    override val sb: UInt
        get() = definition.structure[level()].sb
    override val bb: UInt
        get() = definition.structure[level()].bb
    override val ante: UInt?
        get() = definition.structure[level()].ante

    private fun level(): Int = (games / definition.gamesPerLevel.toInt())
        .coerceAtMost(definition.structure.size-1)

    fun nextGame() { games++ }
}
