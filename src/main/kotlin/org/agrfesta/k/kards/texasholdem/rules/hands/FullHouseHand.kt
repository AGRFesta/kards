package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.kards.texasholdem.rules.RankCount
import org.agrfesta.k.kards.texasholdem.utils.COUNT_THREE
import org.agrfesta.k.kards.texasholdem.utils.COUNT_TWO

class FullHouseHand (
        val threeRank: Rank,
        val twoRank: Rank)
    : org.agrfesta.k.kards.texasholdem.rules.hands.AbstractTHHand(THPokerHand.FULL_HOUSE) {

    init {
        require(threeRank != twoRank) { "Ranks can't be equal: $threeRank" }
    }

    override fun innerCompareTo(ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int {
        require(ce is FullHouseHand) { "Comparable only to an instance of FullHouseHand" }
        return compareBy(FullHouseHand::threeRank)
                .thenBy(FullHouseHand::twoRank)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$threeRank/$twoRank}"

}

fun findFullHouseEvaluation(rankRepList: List<RankCount>): org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation? = when {
    rankRepList[0].count == COUNT_THREE && rankRepList[1].count == COUNT_TWO -> {
        val threeRank = rankRepList[0].rank
        val twoRank = rankRepList
                .filter { it.count == 2 }
                .map { it.rank }
                .max()!!
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
