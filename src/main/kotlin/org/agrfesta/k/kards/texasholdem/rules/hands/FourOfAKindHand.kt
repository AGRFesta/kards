package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.rules.RankCount
import org.agrfesta.k.kards.texasholdem.utils.COUNT_FOUR

class FourOfAKindHand (
        val fokRank: Rank,
        val kicker: Rank)
    : AbstractTHHand(THPokerHand.FOUR_OF_A_KIND) {

    init {
        require(fokRank != kicker) { "Kicker's rank can't be equal to fokRank: $fokRank" }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is FourOfAKindHand) { "Comparable only to an instance of FourOfAKindHand" }
        return compareBy(FourOfAKindHand::fokRank)
                .thenBy(FourOfAKindHand::kicker)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$fokRank $kicker}"

}

fun findFourOfAKindEvaluation(rankRepList: List<RankCount>): CardsEvaluation? {
    if (rankRepList[0].count == COUNT_FOUR) {
        val rank = rankRepList[0].rank
        val kicker = rankRepList
                .map { it.rank }
                .filter { it != rank }
                .maxOrNull()!!
        return FourOfAKindHand(rank, kicker)
    }
    return null
}
