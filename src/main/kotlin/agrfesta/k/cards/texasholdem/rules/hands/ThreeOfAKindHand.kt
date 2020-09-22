package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.texasholdem.rules.OrderedRankListComparator
import agrfesta.k.cards.texasholdem.rules.RankCount
import agrfesta.k.cards.texasholdem.rules.hands.THPokerHand.THREE_OF_A_KIND
import agrfesta.k.cards.texasholdem.utils.COUNT_THREE
import agrfesta.k.cards.texasholdem.utils.FIRST_POS
import agrfesta.k.cards.texasholdem.utils.SECOND_POS
import kotlinx.collections.immutable.toImmutableList

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

fun findThreeOfAKindEvaluation(
        cards: Collection<Card>,
        rankRepList: List<RankCount>): CardsEvaluation? {
    if (rankRepList[0].count == COUNT_THREE) {
        val rank = rankRepList[0].rank
        val kickers = cards
                .filter { it.rank() != rank }
                .map { it.rank() }
                .sortedDescending()
        return ThreeOfAKindHand(rank, kickers[FIRST_POS], kickers[SECOND_POS])
    }
    return null
}
