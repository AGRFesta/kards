package org.agrfesta.k.kards.texasholdem.rules

import org.agrfesta.k.cards.playingcards.cards.Card

interface CardsEvaluator {
    fun evaluate(set: Set<Card>): CardsEvaluation
}
