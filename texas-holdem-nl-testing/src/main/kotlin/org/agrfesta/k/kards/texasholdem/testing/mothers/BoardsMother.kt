package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Board
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.*

fun board(vararg strings: String): Board = object : Board {
    override val cards: Set<Card> = frenchCardsSet(*strings)
    override val phase: GamePhase = when (cards.size) {
        0 -> PRE_FLOP
        3 -> FLOP
        4 -> TURN
        5 -> RIVER
        else -> throw IllegalArgumentException("Unable to get phase from invalid board, cards:${cards}")
    }
}

fun aBoard(phase: GamePhase = PRE_FLOP): Board =  object : Board {
    override val cards: Set<Card> = emptySet() // we don't care about cards here
    override val phase: GamePhase = phase
}
