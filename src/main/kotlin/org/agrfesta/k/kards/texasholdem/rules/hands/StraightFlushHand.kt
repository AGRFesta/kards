package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

class StraightFlushHand(
        val straightTop: Rank,
        val seed: Seed)
    : AbstractTHHand(THPokerHand.STRAIGHT_FLUSH) {

    init {
        require(straightTop >= FIVE) { "The minimum Straight top is FIVE, straightTop: $straightTop" }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is StraightFlushHand) { "Comparable only to an instance of StraightFlushHand" }
        return compareBy(StraightFlushHand::straightTop)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$seed $straightTop}"

}
