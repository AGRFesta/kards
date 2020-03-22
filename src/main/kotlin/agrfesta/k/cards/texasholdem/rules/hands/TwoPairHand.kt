package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.kcards.playingcards.cards.Rank

class TwoPairHand (
        private val majorPairRank: Rank,
        private val minorPairRank: Rank,
        private val kicker: Rank)
    : AbstractTHHand(THPokerHand.TWO_PAIR) {

    init {
        if (majorPairRank == minorPairRank) {
            throw IllegalArgumentException("Pairs rank can't be equal: $majorPairRank")
        }
        if (minorPairRank > majorPairRank) {
            throw IllegalArgumentException("Minor pair rank can't be greater than major pair: $majorPairRank,$minorPairRank")
        }
        if (majorPairRank==kicker || minorPairRank==kicker) {
            throw IllegalArgumentException("Pairs rank can't be equal to kicker rank: $kicker")
        }
    }

    override fun innerCompareTo(he: CardsEvaluation): Int {
        if (he !is TwoPairHand) {
            throw IllegalArgumentException("Comparable only to an instance of TwoPairHand")
        }
        return rawCompareTo(he)
    }

    private fun rawCompareTo(tph: TwoPairHand): Int {
        return compareBy(TwoPairHand::majorPairRank)
                .thenBy(TwoPairHand::minorPairRank)
                .thenBy(TwoPairHand::kicker)
                .compare(this, tph)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$majorPairRank/$minorPairRank $kicker}"
    }
}