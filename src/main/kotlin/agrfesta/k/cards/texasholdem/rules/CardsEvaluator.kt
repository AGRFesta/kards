package agrfesta.k.cards.texasholdem.rules

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.kcards.playingcards.cards.Card

interface CardsEvaluator {
    fun evaluate(set: Set<Card>): CardsEvaluation
}