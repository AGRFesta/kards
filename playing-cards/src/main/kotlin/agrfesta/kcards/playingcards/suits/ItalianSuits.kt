package agrfesta.kcards.playingcards.suits

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.cards.Seed
import agrfesta.kcards.playingcards.cards.cardOf
import agrfesta.kcards.playingcards.deck.AutoShufflingDeck
import agrfesta.kcards.playingcards.deck.Deck
import agrfesta.kcards.playingcards.deck.SimpleStackShufflingService
import java.util.*

fun getItalianRankFromSymbol(symbol: Char): ItalianRank {
    return Arrays.stream(ItalianRank.values())
            .filter { s -> s.symbol() == symbol }
            .findFirst()
            .orElseThrow { IllegalArgumentException("Symbol '$symbol' is not an Italian Rank") }
}
fun getItalianSeedFromSymbol(symbol: Char): ItalianSeed {
    return Arrays.stream(ItalianSeed.values())
            .filter { s -> s.symbol() == symbol }
            .findFirst()
            .orElseThrow { IllegalArgumentException("Symbol '$symbol' is not an Italian Seed") }
}
fun italianCards(): Set<Card> {
    val allCards = HashSet<Card>()
    for (s in ItalianSeed.values()) {
        for (v in ItalianRank.values()) {
            allCards.add(cardOf(v, s))
        }
    }
    return allCards
}

fun createItalianCard(str: String): Card {
    if (str.isBlank()) {
        throw IllegalArgumentException("Unable to create Italian Card, received empty String")
    }
    if (str.length != 2) {
        throw IllegalArgumentException("Unable to create Italian Card, expected two char String, received: $str")
    }
    return cardOf(
            getItalianRankFromSymbol(str[0]),
            getItalianSeedFromSymbol(str[1])
    )
}
fun createItalianHand(vararg cards: String): List<Card> {
    return cards.map { createItalianCard(it) }
}
fun createItalianDeck(): Deck {
    val deck = AutoShufflingDeck(SimpleStackShufflingService())
    deck.add(italianCards())
    return deck
}
fun createItalianDeck(init: ()-> Deck): Deck {
    val deck = init()
    deck.add(italianCards())
    return deck
}

enum class ItalianRank(private val symbol: Char) : Rank {

    ASSO('A'),
    RE('K'),
    CAVALLO('H'),
    FANTE('J'),
    SETTE('7'),
    SEI('6'),
    CINQUE('5'),
    QUATTRO('4'),
    TRE('3'),
    DUE('2');

    override fun symbol() = symbol
    override fun ord() = ordinal
}

enum class ItalianSeed(private val symbol: Char) : Seed {

    SPADE('s'),
    COPPE('c'),
    DENARI('d'),
    BASTONI('b');

    override fun symbol() = symbol
    override fun ord() = ordinal
}