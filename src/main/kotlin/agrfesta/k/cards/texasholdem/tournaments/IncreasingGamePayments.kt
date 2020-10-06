package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePayments

// info: https://pokersoup.com/tool/blindStructureCalculator

class IncreasingGamePayments(structure: List<LevelPayments>, gamesPerLevel: Int): GamePayments {
    private val gamesPerLevel = if (gamesPerLevel < 1) {
        throw IllegalArgumentException("Unable to create an IncreasingGamePayments, gamesPerLevel=$gamesPerLevel")
    } else {
        gamesPerLevel
    }
    private val structure: List<LevelPayments> = if (structure.isEmpty()) {
        throw IllegalArgumentException("Unable to create an IncreasingGamePayments from an empty structure")
    } else {
        structure
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
