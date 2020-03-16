package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed
import agrfesta.kcards.playingcards.cards.cardOf
import agrfesta.kcards.playingcards.deck.AutoShufflingDeck
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.deck.SimpleStackShufflingService
import java.util.Arrays.stream

fun getFrenchRankFromSymbol(symbol: Char): FrenchRank {
    return stream(FrenchRank.values())
            .filter { s -> s.symbol() == symbol }
        .findFirst()
            .orElseThrow { IllegalArgumentException("Symbol '$symbol' is not a French Rank") }
}
fun getFrenchSeedFromSymbol(symbol: Char): FrenchSeed {
    return stream(FrenchSeed.values())
            .filter { s -> s.symbol() == symbol }
        .findFirst()
            .orElseThrow { IllegalArgumentException("Symbol '$symbol' is not a French Seed") }
}
fun frenchCards(): Set<Card> {
    val allCards = HashSet<Card>()
    for (s in FrenchSeed.values()) {
        for (v in FrenchRank.values()) {
            allCards.add(cardOf(v, s))
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
fun createFrenchHand(vararg cards: String): List<Card> {
    return cards.map { createFrenchCard(it) }
}
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

enum class FrenchRank(private val symbol: Char) : Rank {

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

    override fun symbol() = symbol
    override fun ord() = ordinal
}

enum class FrenchSeed(private val symbol: Char) : Seed {

    HEARTS('h'),
    DIAMONDS('d'),
    CLUBS('c'),
    SPADES('s');

    override fun symbol() = symbol
    override fun ord() = ordinal
}