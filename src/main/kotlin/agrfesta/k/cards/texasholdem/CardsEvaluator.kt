package agrfesta.k.cards.texasholdem

import agrfesta.kcards.playingcards.cards.Card

interface CardsEvaluator {
    fun evaluate(cards: List<Card>): CardsEvaluation
}