package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.TWO_PAIR

class TwoPairHand(
        private val majorPairRank: Rank,
        private val minorPairRank: Rank,
        private val kicker: Rank)
    : AbstractTHHand(TWO_PAIR) {

    init {
        require(majorPairRank != minorPairRank) { "Pairs rank can't be equal: $majorPairRank" }
        require(minorPairRank <= majorPairRank)
        { "Minor pair rank can't be greater than major pair: $majorPairRank,$minorPairRank" }
        require(majorPairRank != kicker && minorPairRank != kicker)
        { "Pairs rank can't be equal to kicker rank: $kicker" }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
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
