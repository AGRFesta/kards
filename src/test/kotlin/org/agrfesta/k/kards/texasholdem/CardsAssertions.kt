package org.agrfesta.k.kards.texasholdem

import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet

fun willAssertThatCards(vararg cards: String) =
        LazyAssertionBuilder(frenchCardsSet(*cards))
