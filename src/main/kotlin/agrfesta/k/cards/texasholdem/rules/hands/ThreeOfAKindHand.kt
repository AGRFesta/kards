package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.kcards.playingcards.cards.Rank
import kotlinx.collections.immutable.toImmutableList
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.THREE_OF_A_KIND

class ThreeOfAKindHand(
        private val tokRank: Rank,
        firstKicker: Rank, secondKicker: Rank)
    : AbstractTHHand(THREE_OF_A_KIND) {

    init {
        if (firstKicker == secondKicker) {
            throw IllegalArgumentException("Kickers have same Rank: $firstKicker,$secondKicker")
        }
        if (tokRank==firstKicker || tokRank==secondKicker) {
            throw IllegalArgumentException("Rank of kickers can't be equal to tokRank: $tokRank")
        }
    }

    val kickers = listOf(firstKicker, secondKicker)
            .sorted().reversed()
            .toImmutableList()

    override fun innerCompareTo(ce: CardsEvaluation): Int {
        if (ce !is ThreeOfAKindHand) {
            throw IllegalArgumentException("Comparable only to an instance of ThreeOfAKindHand")
        }
        return compareBy(ThreeOfAKindHand::tokRank)
                .thenBy(OrderedRankListComparator(), ThreeOfAKindHand::kickers)
                .compare(this, ce)
    }

    override fun toString(): String = "${getHandValue()}:{$tokRank $kickers}"
}