package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.FLUSH
import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed
import kotlinx.collections.immutable.toImmutableList

class FlushHand (
        first: Rank, second: Rank, third: Rank, fourth: Rank, fifth: Rank,
        private val seed: Seed)
    : AbstractTHHand(FLUSH) {

    val ranks = listOf(first, second, third, fourth, fifth)
            .sorted().reversed()
            .toImmutableList()

    init {
        val set = setOf(first, second, third, fourth, fifth)
        if (set.size != 5) {
            throw IllegalArgumentException("Multiple with same Rank: $first,$second,$third,$fourth,$fifth")
        }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is FlushHand) {
            throw IllegalArgumentException("Comparable only to an instance of FlushHand")
        }
        return compareBy(OrderedRankListComparator(), FlushHand::ranks)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$seed $ranks}"
}