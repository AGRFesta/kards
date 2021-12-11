package org.agrfesta.k.kards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

class TwoPairHand(
        val majorPairRank: Rank,
        val minorPairRank: Rank,
        val kicker: Rank)
    : org.agrfesta.k.kards.texasholdem.rules.hands.AbstractTHHand(THPokerHand.TWO_PAIR) {

    init {
        require(majorPairRank != minorPairRank) { "Pairs rank can't be equal: $majorPairRank" }
        require(minorPairRank <= majorPairRank)
        { "Minor pair rank can't be greater than major pair: $majorPairRank,$minorPairRank" }
        require(majorPairRank != kicker && minorPairRank != kicker)
        { "Pairs rank can't be equal to kicker rank: $kicker" }
    }

    override fun innerCompareTo(ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation): Int {
        require(ce is TwoPairHand) { "Comparable only to an instance of TwoPairHand" }
        return compareBy(TwoPairHand::majorPairRank)
                .thenBy(TwoPairHand::minorPairRank)
                .thenBy(TwoPairHand::kicker)
                .compare(this, ce)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$majorPairRank/$minorPairRank $kicker}"
    }
}
