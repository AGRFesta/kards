package org.agrfesta.k.kards.texasholdem.rules.hands

import org.agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.rules.RankCount
import org.agrfesta.k.kards.texasholdem.utils.COUNT_THREE
import org.agrfesta.k.kards.texasholdem.utils.COUNT_TWO

class FullHouseHand (
        val threeRank: Rank,
        val twoRank: Rank)
    : AbstractTHHand(THPokerHand.FULL_HOUSE) {

    init {
        require(threeRank != twoRank) { "Ranks can't be equal: $threeRank" }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is FullHouseHand) { "Comparable only to an instance of FullHouseHand" }
        return compareBy(FullHouseHand::threeRank)
                .thenBy(FullHouseHand::twoRank)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$threeRank/$twoRank}"

}

fun findFullHouseEvaluation(rankRepList: List<RankCount>): CardsEvaluation? = when {
    rankRepList[0].count == COUNT_THREE && rankRepList[1].count == COUNT_TWO -> {
        val threeRank = rankRepList[0].rank
        val twoRank = rankRepList
            .filter { it.count == 2 }
            .maxOf { it.rank }
        FullHouseHand(threeRank, twoRank)
    }
    rankRepList[0].count == COUNT_THREE && rankRepList[1].count == COUNT_THREE -> {
        val triples = rankRepList
                .filter { it.count == COUNT_THREE }
                .map { it.rank }
                .sortedDescending()
        FullHouseHand(triples[0], triples[1])
    }
    else -> null
}
