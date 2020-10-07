package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
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
    override fun getType() = ActionType.Call
}

fun call(): Action = CallAction()
fun raise(amount: Int): Action = RaiseAction(amount)
fun fold(): Action = FoldAction()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Players //////////////////////////////////////////////////////////////////////////////////////////////////////////
fun aPlayer() = GamePlayer(Player("APlayer",aStrategy()), 1000)
fun aPlayer(name: String) = GamePlayer(Player(name,aStrategy()), 1000)
fun aPlayer(stack: Int) = GamePlayer(Player("APlayer",aStrategy()), stack)
fun aPlayer(name: String, stack: Int) = GamePlayer(Player(name,aStrategy()), stack)
fun aPlayer(name: String, stack: Int, status: PlayerStatus, cards: Set<Card>) = PlayerTestBuilder()
        .player(Player(name, aStrategy()))
        .stack(stack)
        .status(status)
        .cards(cards)
        .build()
fun aPlayer(name: String, stack: Int, strategy: PlayerStrategyInterface) = GamePlayer(Player(name,strategy), stack)
fun aPlayer(name: String, stack: Int, status: PlayerStatus, strategy: PlayerStrategyInterface) = PlayerTestBuilder()
        .player(Player(name,strategy))
        .stack(stack)
        .status(status)
        .build()

fun foldedPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("FoldedPlayer",aStrategy())).stack(1000).status(PlayerStatus.FOLD)
        .build()
fun allInPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("AllInPlayer",aStrategy())).stack(1000).status(PlayerStatus.ALL_IN)
        .build()
fun callingPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("CallingPlayer",aStrategy())).stack(1000).status(PlayerStatus.CALL)
        .build()
fun raisingPlayer(): GamePlayer = PlayerTestBuilder()
        .player(Player("RaisingPlayer",aStrategy())).stack(1000).status(PlayerStatus.RAISE)
        .build()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
