package agrfesta.k.cards.playingcards.suits

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.cards.cardOf

fun getFrenchRankFromSymbol(symbol: Char): Rank = FrenchRank.values()
        .map(FrenchRank::adapter)
        .find { s -> s.symbol() == symbol }
                ?: throw IllegalArgumentException("Symbol '$symbol' is not a French Rank")
fun getFrenchRankFromOrdinal(ordinal: Int): Rank = FrenchRank.values()
        .map(FrenchRank::adapter)
        .find { it.ordinal() == ordinal % FrenchRank.values().size }!!

fun getFrenchSeedFromSymbol(char: Char): FrenchSeed = FrenchSeed.values()
            .find { s -> s.char == char }
                ?: throw IllegalArgumentException("Symbol '$char' is not a French Seed")

fun createFrenchCard(str: String): Card {
    if (str.isBlank()) {
        throw IllegalArgumentException("Unable to create French Card, received empty String")
    }
    if (str.length != 2) {
        throw IllegalArgumentException("Unable to create French Card, expected two char String, received: $str")
    }
    return cardOf(
            getFrenchRankFromSymbol(str[0]),
            getFrenchSeedFromSymbol(str[1])
    )
}
fun createFrenchHand(vararg cards: String): List<Card> = cards.map { createFrenchCard(it) }
fun frenchCardsSet(vararg cards: String): Set<Card> = cards.map { createFrenchCard(it) }.toSet()

class FrenchRankAdapter(private val fr: FrenchRank): Rank {
    override fun symbol(): Char = fr.symbol()
    override fun ordinal(): Int = fr.ordinal

    override fun plus(increment: Int): Rank = getFrenchRankFromOrdinal(ordinal() +
            FrenchRank.values().size - (increment % FrenchRank.values().size))
    override fun minus(decrement: Int): Rank = getFrenchRankFromOrdinal(ordinal() +
            FrenchRank.values().size + (decrement % FrenchRank.values().size))

    override fun compareTo(other: Rank): Int {
        if (other !is FrenchRankAdapter) {
            throw IllegalArgumentException("Comparable only to an instance of FrenchRankAdapter")
        }
        return other.fr.compareTo(fr)
    }

    override fun toString(): String = fr.toString()
}

val ACE = FrenchRank.ACE.adapter
val KING = FrenchRank.KING.adapter
val QUEEN = FrenchRank.QUEEN.adapter
val JACK = FrenchRank.JACK.adapter
val TEN = FrenchRank.TEN.adapter
val NINE = FrenchRank.NINE.adapter
val EIGHT = FrenchRank.EIGHT.adapter
val SEVEN = FrenchRank.SEVEN.adapter
val SIX = FrenchRank.SIX.adapter
val FIVE = FrenchRank.FIVE.adapter
val FOUR = FrenchRank.FOUR.adapter
val THREE = FrenchRank.THREE.adapter
val TWO = FrenchRank.TWO.adapter

enum class FrenchRank(private val symbol: Char) {

    ACE('A'),
    KING('K'),
    QUEEN('Q'),
    JACK('J'),
    TEN('T'),
    NINE('9'),
    EIGHT('8'),
    SEVEN('7'),
    SIX('6'),
    FIVE('5'),
    FOUR('4'),
    THREE('3'),
    TWO('2');

    val adapter = FrenchRankAdapter(this)

    fun symbol() = symbol
}

enum class FrenchSeed(val char: Char, private val unicode: Char) : Seed {

    HEARTS('h', '♡'),
    DIAMONDS('d', '♢'),
    CLUBS('c', '♣'),
    SPADES('s', '♠');

    override fun symbol() = unicode
    override fun ord() = ordinal
}
