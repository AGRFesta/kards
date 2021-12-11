package org.agrfesta.k.kards.texasholdem.draws

import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation
import agrfesta.k.cards.playingcards.cards.Card

interface DrawsEvaluator {
    fun evaluate(set: Set<Card>, ce: org.agrfesta.k.kards.texasholdem.rules.CardsEvaluation?): Set<Draw>
    fun evaluate(set: Set<Card>): Set<Draw>
}
