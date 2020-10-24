package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Players //////////////////////////////////////////////////////////////////////////////////////////////////////////
val alex = aPlayerWithName("Alex")
val poly = aPlayerWithName("Poly")
val jane = aPlayerWithName("Jane")
val dave = aPlayerWithName("Dave")
val maya = aPlayerWithName("Maya")
val juno = aPlayerWithName("Juno")

fun aPlayer() = Player("APlayer", aStrategy())
fun aPlayerWithName(name: String) = Player(name, aStrategy())

fun aPlayerCardsSet() = frenchCardsSet("7s","2c")

fun anInGamePlayer() = InGamePlayer(aPlayer(), 1000, aPlayerCardsSet())
fun anInGamePlayer(name: String) = InGamePlayer(aPlayerWithName(name), 1000, aPlayerCardsSet())
fun anInGamePlayer(stack: Int) = InGamePlayer(aPlayer(), stack, aPlayerCardsSet())
fun anInGamePlayer(name: String, stack: Int, status: PlayerStatus, cards: Set<Card>) = PlayerTestBuilder()
        .player(Player(name, aStrategy()))
        .stack(stack)
        .status(status)
        .cards(cards)
        .build()
fun anInGamePlayer(name: String, stack: Int, strategy: PlayerStrategyInterface) =
        InGamePlayer(Player(name,strategy), stack, aPlayerCardsSet())
fun anInGamePlayer(name: String, stack: Int, status: PlayerStatus, strategy: PlayerStrategyInterface) =
        PlayerTestBuilder()
                .player(Player(name,strategy))
                .stack(stack)
                .status(status)
                .build()

fun foldedPlayer(): InGamePlayer = PlayerTestBuilder()
        .player(Player("FoldedPlayer",aStrategy())).stack(1000).status(PlayerStatus.FOLD)
        .build()
fun allInPlayer(): InGamePlayer = PlayerTestBuilder()
        .player(Player("AllInPlayer",aStrategy())).stack(1000).status(PlayerStatus.ALL_IN)
        .build()
fun callingPlayer(): InGamePlayer = PlayerTestBuilder()
        .player(Player("CallingPlayer",aStrategy())).stack(1000).status(PlayerStatus.CALL)
        .build()
fun raisingPlayer(): InGamePlayer = PlayerTestBuilder()
        .player(Player("RaisingPlayer",aStrategy())).stack(1000).status(PlayerStatus.RAISE)
        .build()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
