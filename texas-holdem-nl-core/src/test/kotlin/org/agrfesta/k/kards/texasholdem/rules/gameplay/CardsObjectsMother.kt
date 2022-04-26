package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.suits.createFrenchCard
import org.agrfesta.k.cards.playingcards.suits.createFrenchHand
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet

fun cards(vararg strings: String): Array<Card> = frenchCardsSet(*strings).toTypedArray()
fun card(string: String): Card = createFrenchCard(string)
fun cardList(vararg strings: String): List<Card> = createFrenchHand(*strings)
