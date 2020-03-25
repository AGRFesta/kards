package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.STRAIGHT_FLUSH
import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed
import agrfesta.kcards.playingcards.suits.FIVE

class StraightFlushHand(
        private val straightTop: Rank,
        private val seed: Seed)
    : AbstractTHHand(STRAIGHT_FLUSH) {

    init {
        if (straightTop < FIVE) {
            throw IllegalArgumentException("The minimum Straight top is FIVE, straightTop: $straightTop")
        }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is StraightFlushHand) {
            throw IllegalArgumentException("Comparable only to an instance of StraightFlushHand")
        }
        return compareBy(StraightFlushHand::straightTop)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$seed $straightTop}"

}