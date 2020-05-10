package agrfesta.k.cards.texasholdem.tournaments

import agrfesta.k.cards.texasholdem.rules.gameplay.GamePayments

// info: https://pokersoup.com/tool/blindStructureCalculator

//TODO test
class IncreasingGamePayments(private val structure: List<LevelPayments>, private val gamesPerLevel: Int): GamePayments {
    private var games = 0

    override fun sb(): Int = structure[level()].sb
    override fun bb(): Int = structure[level()].bb
    override fun ante(): Int? = structure[level()].ante

    private fun level(): Int = (games / gamesPerLevel).coerceAtMost(structure.size-1)

    fun nextGame() {
        games++
    }
}

class LevelPayments(val sb: Int, val bb: Int, val ante: Int?)