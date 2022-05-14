package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.deck.Deck
import org.agrfesta.k.cards.playingcards.deck.DeckImpl

fun aDeck(cards: List<Card> = emptyList()): Deck = DeckImpl(cards.toMutableList())

