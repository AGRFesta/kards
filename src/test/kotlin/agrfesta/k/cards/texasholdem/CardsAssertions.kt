package agrfesta.k.cards.texasholdem

import agrfesta.kcards.playingcards.suits.frenchCardsSet

fun willAssertThatCards(vararg cards: String) =
        LazyAssertionBuilder(frenchCardsSet(*cards))