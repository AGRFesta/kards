package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.STRAIGHT
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.suits.FIVE

class StraightHand(
        private val straightTop: Rank)
    : AbstractTHHand(STRAIGHT) {

    init {
        if (straightTop < FIVE) {
            throw IllegalArgumentException("The minimum Straight top is FIVE, straightTop: $straightTop")
        }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is StraightHand) {
            throw IllegalArgumentException("Comparable only to an instance of StraightHand")
        }
        return compareBy(StraightHand::straightTop)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$straightTop}"
}
