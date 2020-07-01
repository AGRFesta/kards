package agrfesta.k.cards.texasholdem

import agrfesta.k.cards.playingcards.suits.frenchCardsSet

fun willAssertThatCards(vararg cards: String) =
        LazyAssertionBuilder(frenchCardsSet(*cards))