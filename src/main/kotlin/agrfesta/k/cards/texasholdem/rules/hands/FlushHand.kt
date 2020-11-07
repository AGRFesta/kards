package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.FLUSH
import agrfesta.k.cards.texasholdem.utils.POKER_HAND_SIZE

class FlushHand(
        first: Rank, second: Rank, third: Rank, fourth: Rank, fifth: Rank,
        val seed: Seed)
    : AbstractTHHand(FLUSH) {

    val ranks = listOf(first, second, third, fourth, fifth)
            .sorted()
            .reversed()

    init {
        val set = setOf(first, second, third, fourth, fifth)
        require(set.size == POKER_HAND_SIZE) { "Multiple with same Rank: $first,$second,$third,$fourth,$fifth" }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is FlushHand) { "Comparable only to an instance of FlushHand" }
        return compareBy(OrderedRankListComparator(), FlushHand::ranks)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$seed $ranks}"
}
