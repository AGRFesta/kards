package org.agrfesta.k.kards.texasholdem.rules.hands

import org.agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.cards.playingcards.cards.Seed
import org.agrfesta.k.cards.playingcards.suits.FIVE
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

class StraightFlushHand(
        val straightTop: Rank,
        val seed: Seed)
    : AbstractTHHand(THPokerHand.STRAIGHT_FLUSH) {

    init {
        require(straightTop >= FIVE) { "The minimum Straight top is 5, straightTop: $straightTop" }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is StraightFlushHand) { "Comparable only to an instance of StraightFlushHand" }
        return compareBy(StraightFlushHand::straightTop)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$seed $straightTop}"

}
