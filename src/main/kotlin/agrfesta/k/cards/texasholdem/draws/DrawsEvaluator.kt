package agrfesta.k.cards.texasholdem.draws

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import agrfesta.kcards.playingcards.cards.Card

interface DrawsEvaluator {
    fun evaluate(set: Set<Card>, ce: CardsEvaluation?): Set<Draw>
    fun evaluate(set: Set<Card>): Set<Draw>
}