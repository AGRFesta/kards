package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.playingcards.cards.Card
import io.mockk.every
import io.mockk.mockk

///// Strategies ///////////////////////////////////////////////////////////////////////////////////////////////////////
fun aStrategy(): PlayerStrategyInterface = object : PlayerStrategyInterface {
    override fun act(context: PlayerGameContext): Action = anAction()
    override fun toString(): String = "aStrategy"
}

fun strategyMock(vararg actions: Action): PlayerStrategyInterface {
    val strategy = mockk<PlayerStrategyInterface>()
    every { strategy.act(any()) } returnsMany actions.toList()
    return strategy
}
fun strategyMock(contexts: MutableList<PlayerGameContext>,vararg actions: Action): PlayerStrategyInterface {
    val strategy = mockk<PlayerStrategyInterface>()
    every { strategy.act(capture(contexts)) } returnsMany actions.toList()
    return strategy
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Actions //////////////////////////////////////////////////////////////////////////////////////////////////////////
fun anAction(): Action = object : Action {
    override fun getAmount(): Int? = null
}

fun call(): Action = CallAction()
fun raise(amount: Int): Action = RaiseAction(amount)
fun fold(): Action = FoldAction()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Players //////////////////////////////////////////////////////////////////////////////////////////////////////////
fun aPlayer() = GamePlayer(Player("APlayer"), 1000) { aStrategy() }
fun aPlayer(name: String) = GamePlayer(Player(name), 1000) { aStrategy() }
fun aPlayer(stack: Int) = GamePlayer(Player("APlayer"), stack) { aStrategy() }
fun aPlayer(name: String, stack: Int) = GamePlayer(Player(name), stack) { aStrategy() }
fun aPlayer(name: String, stack: Int, status: PlayerStatus, cards: Set<Card>) = PlayerTestBuilder()
            .player(Player(name)).stack(stack).status(status).cards(cards)
        .build()
fun aPlayer(name: String, stack: Int, strategy: PlayerStrategyInterface) = GamePlayer(Player(name), stack) { strategy }
fun aPlayer(name: String, stack: Int, status: PlayerStatus, strategy: PlayerStrategyInterface) = PlayerTestBuilder()
                .player(Player(name)).stack(stack).status(status).strategy { strategy }
            .build()

fun foldedPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("FoldedPlayer")).stack(1000).status(PlayerStatus.FOLD)
        .build()
fun allInPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("AllInPlayer")).stack(1000).status(PlayerStatus.ALL_IN)
        .build()
fun callingPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("CallingPlayer")).stack(1000).status(PlayerStatus.CALL)
        .build()
fun raisingPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("RaisingPlayer")).stack(1000).status(PlayerStatus.RAISE)
        .build()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////