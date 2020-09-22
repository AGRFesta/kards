package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.deck.Deck
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import agrfesta.k.cards.texasholdem.DeckListImpl
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.texasholdem.playercontext.publicData

fun aDeck(): Deck = DeckListImpl(listOf())
fun aTable(): Table = Table(listOf(aPlayer(),aPlayer()), 0)

fun aContext(): GameContext = GameContext(aTable(), aGamePayments(), EmptyBoard(aDeck()), mapOf())
fun aContext(table: Table, payments: GamePayments): GameContext =
        GameContext(table, payments, EmptyBoard(aDeck()), mapOf())
fun aPlayerContext(): PlayerGameContext = PlayerGameContext(aPlayer().asOwnPlayer(),aGamePayments(),
        EmptyBoard(aDeck()).info(),0,aTable().publicData(), mapOf())

fun aGamePayments(): GamePayments = GamePaymentsFixedImpl(10, 20)
fun blinds(sb: Int, bb: Int): GamePayments = GamePaymentsFixedImpl(sb, bb)

fun board(vararg strings: String): Board = object : Board {
    override fun cards(): Set<Card> = frenchCardsSet(*strings)
    override fun next(): Board = this
    override fun info(): BoardInfo = when (cards().size) {
        0 -> BoardInfo(cards(), GamePhase.PRE_FLOP)
        3 -> BoardInfo(cards(), GamePhase.FLOP)
        4 -> BoardInfo(cards(), GamePhase.TURN)
        5 -> BoardInfo(cards(), GamePhase.RIVER)
        else -> throw IllegalArgumentException("Unable to get info from invalid board, cards:${cards()}")
    }
}
