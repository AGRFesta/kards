package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.RankCount
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.FOUR_OF_A_KIND
import agrfesta.k.cards.texasholdem.utils.COUNT_FOUR

class FourOfAKindHand (
        private val fokRank: Rank,
        private val kicker: Rank)
    : AbstractTHHand(FOUR_OF_A_KIND) {

    init {
        if (fokRank == kicker) {
            throw IllegalArgumentException("Kicker's rank can't be equal to fokRank: $fokRank")
        }
    }

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is FourOfAKindHand) {
            throw IllegalArgumentException("Comparable only to an instance of FourOfAKindHand")
        }
        return compareBy(FourOfAKindHand::fokRank)
                .thenBy(FourOfAKindHand::kicker)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$fokRank $kicker}"

}

fun findFourOfAKindEvaluation(rankRepList: List<RankCount>): CardsEvaluation? {
    if (rankRepList[0].count == COUNT_FOUR) {
        val rank = rankRepList[0].rank
        val kicker = rankRepList
                .map { it.rank }
                .filter { it != rank }
                .max()!!
        return FourOfAKindHand(rank, kicker)
    }
    return null
}
