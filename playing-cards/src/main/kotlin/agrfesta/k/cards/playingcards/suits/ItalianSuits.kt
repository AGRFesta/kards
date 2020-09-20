package agrfesta.k.cards.playingcards.suits

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.Rank
import agrfesta.k.cards.playingcards.cards.Seed
import agrfesta.k.cards.playingcards.cards.cardOf
import agrfesta.k.cards.playingcards.utils.circularIndex

fun getItalianRankFromSymbol(symbol: Char): Rank = ItalianRank.values()
        .map(ItalianRank::adapter)
        .find { it.symbol() == symbol }
        ?: throw IllegalArgumentException("Symbol '$symbol' is not an Italian Rank")
fun getItalianRankFromOrdinal(ordinal: Int): Rank = ItalianRank.values()
        .map(ItalianRank::adapter)
        .circularIndex(ordinal)

fun getItalianSeedFromSymbol(symbol: Char): ItalianSeed = ItalianSeed.values()
        .find { it.symbol() == symbol }
        ?: throw IllegalArgumentException("Symbol '$symbol' is not an Italian Seed")

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

class ItalianRankAdapter(private val ir: ItalianRank): Rank {
    override fun symbol(): Char = ir.symbol()
    override fun ordinal(): Int = ir.ordinal
    override fun plus(increment: Int): Rank = getItalianRankFromOrdinal(ordinal() +
            ItalianRank.values().size - (increment % ItalianRank.values().size))
    override fun minus(decrement: Int): Rank = getItalianRankFromOrdinal(ordinal() +
            ItalianRank.values().size + (decrement % ItalianRank.values().size))

    override fun compareTo(other: Rank): Int {
        if (other !is ItalianRankAdapter) {
            throw IllegalArgumentException("Comparable only to an instance of ItalianRankAdapter")
        }
        return other.ir.compareTo(ir)
    }

    override fun toString(): String = ir.toString()
}

val ASSO = ItalianRank.ASSO.adapter
val RE = ItalianRank.RE.adapter
val CAVALLO = ItalianRank.CAVALLO.adapter
val FANTE = ItalianRank.FANTE.adapter
val SETTE = ItalianRank.SETTE.adapter
val SEI = ItalianRank.SEI.adapter
val CINQUE = ItalianRank.CINQUE.adapter
val QUATTRO = ItalianRank.QUATTRO.adapter
val TRE = ItalianRank.TRE.adapter
val DUE = ItalianRank.DUE.adapter

enum class ItalianRank(private val symbol: Char) {

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

    val adapter = ItalianRankAdapter(this)

    fun symbol() = symbol
    fun ord() = ordinal
}

enum class ItalianSeed(private val symbol: Char) : Seed {

    SPADE('s'),
    COPPE('c'),
    DENARI('d'),
    BASTONI('b');

    override fun symbol() = symbol
    override fun ord() = ordinal
}
