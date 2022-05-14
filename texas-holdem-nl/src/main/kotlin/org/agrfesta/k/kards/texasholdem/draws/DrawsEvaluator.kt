package org.agrfesta.k.kards.texasholdem.draws

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation

interface DrawsEvaluator {
    fun evaluate(set: Set<Card>, ce: CardsEvaluation?): Set<Draw>
    fun evaluate(set: Set<Card>): Set<Draw>
}
