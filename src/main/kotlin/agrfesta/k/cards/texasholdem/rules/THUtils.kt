package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.suits.FrenchRank
import agrfesta.k.cards.texasholdem.utils.POKER_HAND_SIZE
import agrfesta.k.cards.texasholdem.utils.TH_MAX_CARDS
import java.util.*

val cardComparator: Comparator<Card> = compareBy(Card::rank)
        .thenBy(compareBy(Seed::ord), Card::seed)

class OrderedRankListComparator : Comparator<List<Rank>> {
    override fun compare(o1: List<Rank>?, o2: List<Rank>?): Int {
        require(o1 != null && o2 != null) { "Unable to compare null list of Rank" }
        require(o1.size == o2.size) { "Unable to compare null list of Rank" }
        return o1.indices
                .map { i -> o1[i].compareTo(o2[i]) }
                .find { c -> c != 0 } ?: 0
    }

}

fun buildHistogram(hand: Collection<Card>): List<Set<Seed>> {
    val histogram = FrenchRank.values().indices.map { HashSet<Seed>() }
    hand.forEach { histogram[it.rank().ordinal()].add(it.seed()) }
    return histogram
}

fun groupBySeed(set: Set<Card>): Map<Seed, List<Card>> = set
        .groupingBy { it.seed() }
        .fold({ _: Seed, _: Card -> listOf<Card>() },
                { _, accumulator, element ->
                    accumulator.plus(element)
                })

fun getBitSequence(
        histogram: List<Set<Seed>>,
        predicate: (set: Set<Seed>) -> Boolean): Int {
    return histogram.indices
            .map { if (predicate(histogram[it])) 1 shl it else 0 }
            .sum() + if (predicate(histogram[0])) 1 shl histogram.size else 0
}

fun getRankRepetitionsList(set: Set<Card>) = set
        .groupingBy { it.rank() }
        .eachCount().entries
        .map { RankCount(it.key, it.value) }
        .sortedByDescending { it.count }

fun getSeedsBitSequences(histogram: List<Set<Seed>>): IntArray {
    val result = intArrayOf(0, 0, 0, 0)
    for (i in 0..histogram.size) {
        for (s in histogram[i % histogram.size]) {
            result[s.ord()] += 1 shl i
        }
    }
    return result
}

fun checkSize(cards: Set<Card>) {
    require(cards.size >= POKER_HAND_SIZE) { "Unable to evaluate a set of less than five cards: ${cards.size}" }
    require(cards.size <= TH_MAX_CARDS) { "Unable to evaluate a set of more than seven cards: ${cards.size}" }
}
