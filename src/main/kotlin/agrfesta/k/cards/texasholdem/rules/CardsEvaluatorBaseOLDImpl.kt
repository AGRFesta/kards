package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.texasholdem.rules.hands.HighCardHand
import agrfesta.k.cards.texasholdem.rules.hands.StraightFlushHand
import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.cards.Seed
import agrfesta.kcards.playingcards.suits.*
import java.math.BigInteger


class CardsEvaluatorBaseOLDImpl: CardsEvaluator {
    private val seqComparator: Comparator<List<Card>> = compareBy { it.size }
    private val cardComparator: Comparator<Card> = compareBy { it.rank() }

    override fun evaluate(set: Set<Card>): CardsEvaluation {
        checkSize(set)

        val rankRepList = set.groupingBy { it.rank() }
                .eachCount().entries
                .map { Pair(it.key,it.value) }
                .toList()
        val groupedBySeed: Map<Seed, List<Card>> = set.groupingBy { it.seed() }
                .fold(  { _: Seed, _: Card -> listOf<Card>()},
                        { _, accumulator, element ->
                            accumulator.plus(element)
                        })

        val straightFlushCards = straightFlush(groupedBySeed)
        if (straightFlushCards != null) {
            return StraightFlushHand(straightFlushCards[0].rank(),straightFlushCards[0].seed())
        }

        return HighCardHand(ACE, JACK, TEN, EIGHT, SEVEN)
    }

    private fun checkSize(cards: Set<Card>) {
        if (cards.size < 5) {
            throw IllegalArgumentException(
                    "Unable to evaluate a set of less than five cards: ${cards.size}")
        }
        if (cards.size > 7) {
            throw IllegalArgumentException(
                    "Unable to evaluate a set of more than seven cards: ${cards.size}")
        }
    }

    private fun sequences(cards: List<Card>): List<List<Card>> {
        val result: MutableList<List<Card>> = ArrayList()
        var last: MutableList<Card> = ArrayList()
        var prev: Int? = null
        for (card in cards) {
            val ord: Int = card.rank().ordinal()
            if (prev != null && prev != ord - 1) {
                if (prev != ord) { //Start new sequence
                    result.add(last)
                    last = ArrayList()
                    last.add(card)
                }
            } else {
                last.add(card)
            }
            prev = ord
        }
        if (last[last.size - 1].rank() === BigInteger.TWO
                && cards[0].rank() === ACE) {
            last.add(cards[0])
        }
        result.add(last)
        return result
    }

    private fun straight(cards: List<Card>): List<Card>? = sequences(cards)
                .filter { it.size > 4 }
                .maxBy { it.size }

    private fun straightFlush(groupedBySeed: Map<Seed, List<Card>>): List<Card>? = groupedBySeed.values
                .filter { it.size > 4 }
                .map { straight(it.toList().sortedWith(cardComparator)) }
                .find { it != null }

}