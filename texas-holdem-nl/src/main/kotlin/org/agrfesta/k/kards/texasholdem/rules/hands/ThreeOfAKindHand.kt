package org.agrfesta.k.kards.texasholdem.rules.hands

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import org.agrfesta.k.kards.texasholdem.rules.OrderedRankListComparator
import org.agrfesta.k.kards.texasholdem.rules.RankCount
import org.agrfesta.k.kards.texasholdem.utils.COUNT_THREE
import org.agrfesta.k.kards.texasholdem.utils.FIRST_POS
import org.agrfesta.k.kards.texasholdem.utils.SECOND_POS

class ThreeOfAKindHand(
        val tokRank: Rank,
        firstKicker: Rank, secondKicker: Rank)
    : AbstractTHHand(THPokerHand.THREE_OF_A_KIND) {

    init {
        require(firstKicker != secondKicker) { "Kickers have same Rank: $firstKicker,$secondKicker" }
        require(tokRank != firstKicker && tokRank != secondKicker)
        { "Rank of kickers can't be equal to tokRank: $tokRank" }
    }

    val kickers = listOf(firstKicker, secondKicker)
            .sorted().reversed()

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is ThreeOfAKindHand) { "Comparable only to an instance of ThreeOfAKindHand" }
        return compareBy(ThreeOfAKindHand::tokRank)
                .thenBy(OrderedRankListComparator(), ThreeOfAKindHand::kickers)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$tokRank $kickers}"
}

fun findThreeOfAKindEvaluation(
        cards: Collection<Card>,
        rankRepList: List<RankCount>): CardsEvaluation? {
    if (rankRepList[0].count == COUNT_THREE) {
        val rank = rankRepList[0].rank
        val kickers = cards
                .filter { it.rank != rank }
                .map { it.rank }
                .sortedDescending()
        return ThreeOfAKindHand(rank, kickers[FIRST_POS], kickers[SECOND_POS])
    }
    return null
}
