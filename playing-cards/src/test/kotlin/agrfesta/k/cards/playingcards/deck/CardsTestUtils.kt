package agrfesta.k.cards.playingcards.deck

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.cards.cardOf

fun aCard() = cardOf(aRank(), aSeed())
fun aRank() = rankOf('R')
fun aSeed() = seedOf('s', 1)

fun deckContent(deck: Deck): Collection<Card> {
    val content = mutableListOf<Card>()
    while (!deck.isEmpty()) {
        content.add(deck.draw())
    }
    return content
}

