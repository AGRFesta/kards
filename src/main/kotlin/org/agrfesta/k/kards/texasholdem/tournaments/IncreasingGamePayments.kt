package org.agrfesta.k.kards.texasholdem.tournaments

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePayments

// info: https://pokersoup.com/tool/blindStructureCalculator

class IncreasingGamePayments(private val structure: List<LevelPayments>, private val gamesPerLevel: Int): GamePayments {

    init {
        require(gamesPerLevel >= 1) { "Unable to create an IncreasingGamePayments, gamesPerLevel=$gamesPerLevel" }
        require(structure.isNotEmpty()) { "Unable to create an IncreasingGamePayments from an empty structure" }
    }

    private var games = 0

    override fun sb(): Int = structure[level()].sb
    override fun bb(): Int = structure[level()].bb
    override fun ante(): Int? = structure[level()].ante

    private fun level(): Int = (games / gamesPerLevel).coerceAtMost(structure.size-1)

    fun nextGame() {
        games++
    }
}

data class LevelPayments(val sb: Int, val bb: Int, val ante: Int?)
