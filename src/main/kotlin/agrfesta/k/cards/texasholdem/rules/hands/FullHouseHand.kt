package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.FULL_HOUSE

class FullHouseHand (
        private val threeRank: Rank,
        private val twoRank: Rank)
    : AbstractTHHand(FULL_HOUSE) {

    init {
        if (threeRank == twoRank) {
            throw IllegalArgumentException("Ranks can't be equal: $threeRank")
        }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is FullHouseHand) {
            throw IllegalArgumentException("Comparable only to an instance of FullHouseHand")
        }
        return compareBy(FullHouseHand::threeRank)
                .thenBy(FullHouseHand::twoRank)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$threeRank/$twoRank}"
}