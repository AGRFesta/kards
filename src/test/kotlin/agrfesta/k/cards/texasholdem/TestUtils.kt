package agrfesta.k.cards.texasholdem

import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.suits.getFrenchRankFromSymbol

fun createRankList(vararg chars: Char): List<Rank> {
    return chars
            .map { c -> getFrenchRankFromSymbol(c) }
}
