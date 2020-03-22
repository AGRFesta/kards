package agrfesta.kcards.playingcards.deck

import agrfesta.kcards.playingcards.cards.Card
import agrfesta.kcards.playingcards.cards.cardOf
import java.util.*

fun aCard() = cardOf(aRank(), aSeed())
fun aRank() = rankOf('R')
fun aSeed() = seedOf('s',1)

fun deckContent(deck: Deck): Collection<Card> {
    val content = ArrayList<Card>()
    while (!deck.isEmpty()) {
        content.add(deck.draw())
    }
    return content
}

class ReverseStackShufflingService : ShufflingService {
    override fun shuffle(cards: Stack<Card>): Stack<Card> {
        cards.reverse()
        return cards
    }
}