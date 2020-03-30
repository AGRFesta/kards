package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed
import agrfesta.kcards.playingcards.cards.cardOf
import agrfesta.kcards.playingcards.deck.AutoShufflingDeck
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.deck.SimpleStackShufflingService

fun getFrenchRankFromSymbol(symbol: Char): Rank = FrenchRank.values()
            .map(FrenchRank::adapter)
            .find { s -> s.symbol() == symbol }
                ?: throw IllegalArgumentException("Symbol '$symbol' is not a French Rank")

fun getFrenchSeedFromSymbol(char: Char): FrenchSeed = FrenchSeed.values()
            .find { s -> s.char == char }
                ?: throw IllegalArgumentException("Symbol '$char' is not a French Seed")

fun frenchCards(): Set<Card> {
    val allCards = HashSet<Card>()
    for (s in FrenchSeed.values()) {
        for (v in FrenchRank.values()) {
            allCards.add(cardOf(v.adapter, s))
        }
    }
    return allCards
}

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
fun frenchCardsSet(vararg cards: String): Set<Card> = createFrenchHand(*cards).toSet()
fun createFrenchDeck(): Deck {
    val deck = AutoShufflingDeck(SimpleStackShufflingService())
    deck.add(frenchCards())
    return deck
}
fun createFrenchDeck(init: ()->Deck): Deck {
    val deck = init()
    deck.add(frenchCards())
    return deck
}

class FrenchRankAdapter(private val fr: FrenchRank): Rank {
    override fun symbol(): Char = fr.symbol()
    override fun ordinal(): Int = fr.ordinal

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