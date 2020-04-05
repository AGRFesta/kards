package agrfesta.k.cards.texasholdem.draws

import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed
import kotlinx.collections.immutable.toImmutableList

/*
    A flush draw, or four flush, is a hand with four cards of the same suit that may improve to a flush.
    For example, K♣ 9♣ 8♣ 5♣. A flush draw has nine outs (thirteen cards of the suit less the four
    already in the hand). If a player has a flush draw in Hold'em, the probability to flush the hand in
    the end is 34.97 percent if there are two more cards to come, and 19.56 percent (9 live cards divided
    by 46 unseen cards) if there is only one more card to come.
 */

class FlushDraw(
        first: Rank, second: Rank, third: Rank, fourth: Rank,
        val seed: Seed): Draw {

    val ranks = listOf(first, second, third, fourth)
            .sorted().reversed()
            .toImmutableList()

    init {
        val set = setOf(first, second, third, fourth)
        if (set.size != 4) {
            throw IllegalArgumentException("Multiple with same Rank: $first,$second,$third,$fourth")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is FlushDraw) return false
        if (other.seed != seed) return false
        if (other.ranks != ranks) return false
        return true
    }
    override fun hashCode(): Int {
        var hash = 7
        hash = 31 * hash + seed.hashCode()
        hash = 31 * hash + ranks.hashCode()
        return hash
    }

    override fun toString(): String {
        val rankSymbols = ranks.map { it.symbol() }
        return "$rankSymbols${seed.symbol()}"
    }
}