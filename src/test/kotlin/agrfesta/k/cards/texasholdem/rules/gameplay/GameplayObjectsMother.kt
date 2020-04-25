package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.DeckListImpl
import agrfesta.kcards.playingcards.deck.Deck

fun aStrategy(): PlayerStrategyInterface = object : PlayerStrategyInterface {
        override fun act(context: GameContext): Action = anAction()
        override fun toString(): String = "aStrategy"
}
fun anAction(): Action = object : Action {
    override fun getAmount(): Int? = null
}
fun aPlayer() = Player("APlayer", 1000) { aStrategy() }
fun aPlayer(name: String) = Player(name, 1000) { aStrategy() }
fun aPlayer(stack: Int) = Player("APlayer", stack) { aStrategy() }
//fun aPlayer(name: String, stack: Int) = Player(name, stack) { aStrategy() }

fun foldedPlayer(): Player {
    val player = Player("FoldedPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.FOLD
    return player
}
fun allInPlayer(): Player {
    val player = Player("AllInPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.ALL_IN
    return player
}
fun callingPlayer(): Player {
    val player = Player("CallingPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.CALL
    return player
}
fun raisingPlayer(): Player {
    val player = Player("RaisingPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.RAISE
    return player
}

fun aDeck(): Deck = DeckListImpl(listOf())
fun aGamePayments(): GamePayments = GamePaymentsFixedImpl(10, 20)
fun aTable(): Table = Table(listOf(aPlayer(),aPlayer()), 0)
fun aContext(): GameContext = GameContext(aTable(), aGamePayments(), EmptyBoard(aDeck()))
