package agrfesta.k.cards.playingcards.suits

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.cards.cardOf
import agrfesta.k.cards.playingcards.utils.circularIndex
import agrfesta.k.cards.playingcards.utils.circularIndexMapping

fun getFrenchRankFromSymbol(symbol: Char): Rank = frenchRanksSet
    .find { it.symbol == symbol }
    ?: throw IllegalArgumentException("Symbol '$symbol' is not a French Rank")
fun getFrenchRankFromOrdinal(ordinal: Int): Rank = frenchRankList.circularIndex(ordinal)

fun getFrenchSeedFromSymbol(char: Char): FrenchSeed = FrenchSeed.values()
        .find { it.char == char }
        ?: throw IllegalArgumentException("Symbol '$char' is not a French Seed")

fun createFrenchCard(str: String): Card {
    require(str.isNotBlank()) { "Unable to create French Card, received empty String" }
    require(str.length == 2) { "Unable to create French Card, expected two char String, received: $str" }
    return cardOf(
            getFrenchRankFromSymbol(str[0]),
            getFrenchSeedFromSymbol(str[1])
    )
}
fun createFrenchHand(vararg cards: String): List<Card> = cards.map { createFrenchCard(it) }
fun frenchCardsSet(vararg cards: String): Set<Card> = cards.map { createFrenchCard(it) }.toSet()

val ACE = FrenchRank('A', 0)
val KING = FrenchRank('K', 1)
val QUEEN = FrenchRank('Q', 2)
val JACK = FrenchRank('J', 3)
val TEN = FrenchRank('T', 4)
val NINE = FrenchRank('9', 5)
val EIGHT = FrenchRank('8', 6)
val SEVEN = FrenchRank('7', 7)
val SIX = FrenchRank('6', 8)
val FIVE = FrenchRank('5', 9)
val FOUR = FrenchRank('4', 10)
val THREE = FrenchRank('3', 11)
val TWO = FrenchRank('2', 12)
val frenchRanksSet = setOf(ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO)
val frenchRankList = frenchRanksSet.sortedBy { it.ordinal }

class FrenchRank(
    override val symbol: Char,
    override val ordinal: Int): Rank {

    override fun plus(increment: Int): Rank =
        getFrenchRankFromOrdinal( frenchRankList.circularIndexMapping(ordinal - increment) )
    override fun minus(decrement: Int): Rank =
        getFrenchRankFromOrdinal( frenchRankList.circularIndexMapping(ordinal + decrement) )

    override fun compareTo(other: Rank): Int {
        require(other is FrenchRank) { "Comparable only to an instance of FrenchRank" }
        return other.ordinal.compareTo(this.ordinal)
    }

    override fun toString(): String = symbol.toString()
}

enum class FrenchSeed(val char: Char, override val symbol: Char) : Seed {
    HEARTS('h', '♡'),
    DIAMONDS('d', '♢'),
    CLUBS('c', '♣'),
    SPADES('s', '♠');
}
