package org.agrfesta.k.kards.texasholdem.tournaments

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePayments

// info: https://pokersoup.com/tool/blindStructureCalculator

class IncreasingGamePayments(private val structure: List<LevelPayments>, private val gamesPerLevel: UInt): GamePayments {

    init {
        require(gamesPerLevel >= 1u) { "Unable to create an IncreasingGamePayments, gamesPerLevel=$gamesPerLevel" }
        require(structure.isNotEmpty()) { "Unable to create an IncreasingGamePayments from an empty structure" }
    }

    private var games = 0u

    override fun sb(): UInt = structure[level()].sb
    override fun bb(): UInt = structure[level()].bb
    override fun ante(): UInt? = structure[level()].ante

    private fun level(): Int = (games.toInt() / gamesPerLevel.toInt()).coerceAtMost(structure.size-1)

    fun nextGame() {
        games++
    }
}

data class LevelPayments(val sb: UInt, val bb: UInt, val ante: UInt? = null)
