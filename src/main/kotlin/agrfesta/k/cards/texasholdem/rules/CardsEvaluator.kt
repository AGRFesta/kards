package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.playingcards.cards.Card

interface CardsEvaluator {
    fun evaluate(set: Set<Card>): CardsEvaluation
}