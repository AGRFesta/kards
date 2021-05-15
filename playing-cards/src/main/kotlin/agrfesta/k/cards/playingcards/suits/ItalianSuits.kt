package agrfesta.k.cards.playingcards.suits

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.cards.cardOf
import agrfesta.k.cards.playingcards.utils.circularIndex
import agrfesta.k.cards.playingcards.utils.circularIndexMapping

fun getItalianRankFromSymbol(symbol: Char): Rank = italianRanksSet
        .find { it.symbol == symbol }
        ?: throw IllegalArgumentException("Symbol '$symbol' is not an Italian Rank")
fun getItalianRankFromOrdinal(ordinal: Int): Rank = italianRankList
        .circularIndex(ordinal)

fun getItalianSeedFromSymbol(symbol: Char): ItalianSeed = ItalianSeed.values()
        .find { it.symbol == symbol }
        ?: throw IllegalArgumentException("Symbol '$symbol' is not an Italian Seed")

fun createItalianCard(str: String): Card {
    require(str.isNotBlank()) { "Unable to create Italian Card, received empty String" }
    require(str.length == 2) { "Unable to create Italian Card, expected two char String, received: $str" }
    return cardOf(
            getItalianRankFromSymbol(str[0]),
            getItalianSeedFromSymbol(str[1])
    )
}
fun createItalianHand(vararg cards: String): List<Card> {
    return cards.map { createItalianCard(it) }
}

val ASSO = ItalianRank('A', 0)
val RE = ItalianRank('K', 1)
val CAVALLO = ItalianRank('H', 2)
val FANTE = ItalianRank('J', 3)
val SETTE = ItalianRank('7', 4)
val SEI = ItalianRank('6', 5)
val CINQUE = ItalianRank('5', 6)
val QUATTRO = ItalianRank('4', 7)
val TRE = ItalianRank('3', 8)
val DUE = ItalianRank('2', 9)
val italianRanksSet = setOf(ASSO, RE, CAVALLO, FANTE, SETTE, SEI, CINQUE, QUATTRO, TRE, DUE)
val italianRankList = italianRanksSet.sortedBy { it.ordinal }

class ItalianRank(
    override val symbol: Char,
    override val ordinal: Int): Rank {

    override fun plus(increment: Int): Rank =
        getItalianRankFromOrdinal( italianRankList.circularIndexMapping(ordinal - increment) )
    override fun minus(decrement: Int): Rank =
        getItalianRankFromOrdinal( italianRankList.circularIndexMapping(ordinal + decrement) )

    override fun compareTo(other: Rank): Int {
        require(other is ItalianRank) { "Comparable only to an instance of ItalianRank" }
        return other.ordinal.compareTo(this.ordinal)
    }

    override fun toString(): String = symbol.toString()
}

enum class ItalianSeed(override val symbol: Char) : Seed {
    SPADE('s'),
    COPPE('c'),
    DENARI('d'),
    BASTONI('b');
}
