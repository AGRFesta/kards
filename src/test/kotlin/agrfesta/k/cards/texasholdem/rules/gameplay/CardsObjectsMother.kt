package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.suits.createFrenchCard
import agrfesta.kcards.playingcards.suits.createFrenchHand
import agrfesta.kcards.playingcards.suits.frenchCardsSet

fun cards(vararg strings: String): Array<Card> = frenchCardsSet(*strings).toTypedArray()
fun card(string: String): Card = createFrenchCard(string)
fun cardList(vararg strings: String): List<Card> = createFrenchHand(*strings)