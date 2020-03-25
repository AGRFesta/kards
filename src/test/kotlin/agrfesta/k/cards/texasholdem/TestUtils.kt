package agrfesta.k.cards.texasholdem

import agrfesta.k.cards.texasholdem.rules.hands.HECompareAssertionData
import agrfesta.kcards.playingcards.cards.Rank
import agrfesta.kcards.playingcards.suits.getFrenchRankFromSymbol
import org.junit.jupiter.api.DynamicTest

fun createRankList(vararg chars: Char): List<Rank> {
    return chars
            .map { c -> getFrenchRankFromSymbol(c) }
}

fun createDynamicTest(data: HECompareAssertionData): DynamicTest =
        DynamicTest.dynamicTest(data.getTitle(), data::assertComparison)
