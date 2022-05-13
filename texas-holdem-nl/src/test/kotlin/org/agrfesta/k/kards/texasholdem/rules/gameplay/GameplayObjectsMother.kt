package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.suits.TEN
import org.agrfesta.k.kards.texasholdem.observers.GameResult
import org.agrfesta.k.kards.texasholdem.observers.ShowdownPlayerResult
import org.agrfesta.k.kards.texasholdem.rules.hands.StraightHand
import org.agrfesta.k.kards.texasholdem.testing.mothers.aPlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.anInGamePlayer

fun aGameResult() = GameResult(aPlayer(), 2000u, emptyList())
fun aShowdownPlayerResult() = ShowdownPlayerResult(anInGamePlayer(), null, aCardsEvaluation())
fun aCardsEvaluation() = StraightHand(TEN)
