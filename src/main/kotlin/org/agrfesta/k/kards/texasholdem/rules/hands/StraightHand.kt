package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

class StraightHand(
        val straightTop: Rank)
    : org.agrfesta.k.kards.texasholdem.rules.hands.AbstractTHHand(THPokerHand.STRAIGHT) {

    init {
        require(straightTop >= FIVE) { "The minimum Straight top is FIVE, straightTop: $straightTop" }
    }

    override fun innerCompareTo(ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int {
        require(ce is StraightHand) { "Comparable only to an instance of StraightHand" }
        return compareBy(StraightHand::straightTop)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$straightTop}"
}
