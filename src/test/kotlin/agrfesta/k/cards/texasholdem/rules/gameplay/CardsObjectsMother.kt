package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.suits.createFrenchCard
import agrfesta.k.cards.playingcards.suits.createFrenchHand
import agrfesta.k.cards.playingcards.suits.frenchCardsSet

fun cards(vararg strings: String): Array<Card> = frenchCardsSet(*strings).toTypedArray()
fun card(string: String): Card = createFrenchCard(string)
fun cardList(vararg strings: String): List<Card> = createFrenchHand(*strings)
