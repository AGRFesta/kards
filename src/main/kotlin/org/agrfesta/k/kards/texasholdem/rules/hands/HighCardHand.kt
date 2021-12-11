package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.kards.texasholdem.rules.OrderedRankListComparator
import org.agrfesta.k.kards.texasholdem.utils.FIFTH_POS
import org.agrfesta.k.kards.texasholdem.utils.FIRST_POS
import org.agrfesta.k.kards.texasholdem.utils.FOURTH_POS
import org.agrfesta.k.kards.texasholdem.utils.POKER_HAND_SIZE
import org.agrfesta.k.kards.texasholdem.utils.SECOND_POS
import org.agrfesta.k.kards.texasholdem.utils.THIRD_POS

class HighCardHand(
        firstKicker: Rank, secondKicker: Rank, thirdKicker: Rank, fourthKicker: Rank, fifthKicker: Rank)
    : org.agrfesta.k.kards.texasholdem.rules.hands.AbstractTHHand(THPokerHand.HIGH_CARD) {

    init {
        val set = setOf(firstKicker, secondKicker, thirdKicker, fourthKicker, fifthKicker)
        require(set.size == POKER_HAND_SIZE) {
            "Multiple kickers with same Rank: " +
                    "$firstKicker,$secondKicker,$thirdKicker,$fourthKicker,$fifthKicker"
        }
    }

    val kickers = listOf(firstKicker, secondKicker, thirdKicker, fourthKicker, fifthKicker)
            .sorted()
            .reversed()

    override fun innerCompareTo(ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int {
        require(ce is HighCardHand) { "Comparable only to an instance of HighCardHand" }
        return compareBy(OrderedRankListComparator(), HighCardHand::kickers)
                .compare(this, ce)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$kickers}"
    }

}

fun getHighCardEvaluation(cards: Collection<Card>): org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation {
    val kickers = cards
            .map { it.rank() }
            .sortedDescending()
    return HighCardHand(
            kickers[FIRST_POS],
            kickers[SECOND_POS],
            kickers[THIRD_POS],
            kickers[FOURTH_POS],
            kickers[FIFTH_POS])
}
