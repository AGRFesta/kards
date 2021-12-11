package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import kotlinx.collections.immutable.toImmutableList
import org.agrfesta.k.kards.texasholdem.rules.OrderedRankListComparator
import org.agrfesta.k.kards.texasholdem.utils.COUNT_THREE

class PairHand(
        val pairRank: Rank,
        firstKicker: Rank, secondKicker: Rank, thirdKicker: Rank)
    : org.agrfesta.k.kards.texasholdem.rules.hands.AbstractTHHand(THPokerHand.PAIR) {

    init {
        val set = setOf(firstKicker, secondKicker, thirdKicker)
        require(set.size == COUNT_THREE) { "Multiple kickers with same Rank: $firstKicker,$secondKicker,$thirdKicker" }
        require(!set.contains(pairRank)) { "Rank of kickers can't be equal to pairRank: $pairRank" }
    }

    val kickers = listOf(firstKicker, secondKicker, thirdKicker)
            .sorted().reversed()
            .toImmutableList()

    override fun innerCompareTo(ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int {
        require(ce is PairHand) { "Comparable only to an instance of PairHand" }
        return compareBy(PairHand::pairRank)
                .thenBy(OrderedRankListComparator(), PairHand::kickers)
                .compare(this, ce)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$pairRank $kickers}"
    }
}
