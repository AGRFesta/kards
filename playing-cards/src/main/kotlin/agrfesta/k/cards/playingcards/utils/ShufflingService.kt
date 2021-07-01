package agrfesta.k.cards.playingcards.utils

import agrfesta.k.cards.playingcards.cards.Card

/**
 * Shuffles the cards modifying the [MutableList] of [Card].
 */
typealias Shuffler = (MutableList<Card>) -> Unit

/**
 * Simple shuffler that wraps [MutableList.shuffle].
 */
val simpleShuffler: Shuffler = { it.shuffle() }
