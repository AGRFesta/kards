package org.agrfesta.k.kards.texasholdem.rules

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.cards.Rank
import org.agrfesta.k.cards.playingcards.cards.Seed
import org.agrfesta.k.cards.playingcards.suits.frenchRankList
import org.agrfesta.k.kards.texasholdem.utils.POKER_HAND_SIZE
import org.agrfesta.k.kards.texasholdem.utils.TH_MAX_CARDS

val cardComparator: Comparator<Card> = compareBy(Card::rank)
        .thenBy(compareBy(Seed::ordinal), Card::seed)

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
    val histogram = frenchRankList.indices.map { HashSet<Seed>() }
    hand.forEach { histogram[it.rank.ordinal].add(it.seed) }
    return histogram
}

fun groupBySeed(set: Set<Card>): Map<Seed, List<Card>> = set
        .groupingBy { it.seed }
        .fold({ _: Seed, _: Card -> listOf() },
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
        .groupingBy { it.rank }
        .eachCount().entries
        .map { RankCount(it.key, it.value) }
        .sortedByDescending { it.count }

fun getSeedsBitSequences(histogram: List<Set<Seed>>): IntArray {
    val result = intArrayOf(0, 0, 0, 0)
    for (i in 0..histogram.size) {
        for (s in histogram[i % histogram.size]) {
            result[s.ordinal] += 1 shl i
        }
    }
    return result
}

fun checkSize(cards: Set<Card>) {
    require(cards.size >= POKER_HAND_SIZE) { "Unable to evaluate a set of less than five cards: ${cards.size}" }
    require(cards.size <= TH_MAX_CARDS) { "Unable to evaluate a set of more than seven cards: ${cards.size}" }
}
