package agrfesta.kcards.playingcards.french

import agrfesta.kcards.playingcards.cards.Rank
import java.util.Arrays.stream

fun getFrenchRankFromSymbol(symbol: Char): FrenchRank {
    return stream(FrenchRank.values())
        .filter { s -> s.symbol == symbol }
        .findFirst()
        .orElseThrow { IllegalArgumentException("symbol '$symbol' is not a French Rank") }
}

enum class FrenchRank(val symbol: Char) : Rank  {

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