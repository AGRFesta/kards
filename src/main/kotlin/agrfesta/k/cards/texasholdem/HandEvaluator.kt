package agrfesta.k.cards.texasholdem

import agrfesta.kcards.playingcards.cards.Card

interface HandEvaluator {
    fun evaluate(hand: Set<Card?>): HandEvaluation
}