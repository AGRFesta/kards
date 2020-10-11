package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.PAIR
import agrfesta.k.cards.texasholdem.utils.COUNT_THREE
import kotlinx.collections.immutable.toImmutableList

class PairHand(
        private val pairRank: Rank,
        firstKicker: Rank, secondKicker: Rank, thirdKicker: Rank)
    : AbstractTHHand(PAIR) {

    init {
        val set = setOf(firstKicker, secondKicker, thirdKicker)
        require(set.size == COUNT_THREE) { "Multiple kickers with same Rank: $firstKicker,$secondKicker,$thirdKicker" }
        require(!set.contains(pairRank)) { "Rank of kickers can't be equal to pairRank: $pairRank" }
    }

    val kickers = listOf(firstKicker, secondKicker, thirdKicker)
            .sorted().reversed()
            .toImmutableList()

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        require(ce is PairHand) { "Comparable only to an instance of PairHand" }
        return compareBy(PairHand::pairRank)
                .thenBy(OrderedRankListComparator(), PairHand::kickers)
                .compare(this, ce)
    }

    override fun toString(): String {
        return "${getHandValue()}:{$pairRank $kickers}"
    }
}
