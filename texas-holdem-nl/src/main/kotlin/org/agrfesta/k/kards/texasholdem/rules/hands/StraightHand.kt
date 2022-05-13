package org.agrfesta.k.kards.texasholdem.rules.hands

import org.agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

class StraightHand(
        val straightTop: Rank)
    : AbstractTHHand(THPokerHand.STRAIGHT) {

    init {
        require(straightTop >= FIVE) { "The minimum Straight top is 5, straightTop: $straightTop" }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is StraightHand) { "Comparable only to an instance of StraightHand" }
        return compareBy(StraightHand::straightTop)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$straightTop}"
}
