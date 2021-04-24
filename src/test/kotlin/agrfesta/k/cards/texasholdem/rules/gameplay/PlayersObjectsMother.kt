package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.texasholdem.rules.gameplay.InGamePlayerTestBuilder.Companion.buildingAnInGamePlayer
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.ALL_IN
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.CALL
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.RAISE
import io.mockk.every
import io.mockk.mockk

///// Strategies ///////////////////////////////////////////////////////////////////////////////////////////////////////
fun aStrategy(): PlayerStrategyInterface = object : PlayerStrategyInterface {
    override fun act(context: PlayerGameContext<OwnPlayer>): Action = anAction()
    override fun toString(): String = "aStrategy"
}

fun strategyMock(vararg actions: Action): PlayerStrategyInterface {
    val strategy = mockk<PlayerStrategyInterface>()
    every { strategy.act(any()) } returnsMany actions.toList()
    return strategy
}
fun strategyMock(contexts: MutableList<PlayerGameContext<OwnPlayer>>,vararg actions: Action): PlayerStrategyInterface {
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
fun anInGamePlayer(name: String, stack: Int, status: PlayerStatus, cards: Set<Card>) = buildingAnInGamePlayer()
        .forPlayer(Player(name, aStrategy()))
        .withAStackOf(stack)
        .inStatus(status)
        .withCards(cards)
        .build()
fun anInGamePlayer(name: String, stack: Int, strategy: PlayerStrategyInterface) =
        InGamePlayer(Player(name,strategy), stack, aPlayerCardsSet())
fun anInGamePlayer(name: String, stack: Int, status: PlayerStatus, strategy: PlayerStrategyInterface) =
        buildingAnInGamePlayer()
                .forPlayer(Player(name,strategy))
                .withAStackOf(stack)
                .inStatus(status)
                .build()

fun foldedPlayer(): InGamePlayer = buildingAnInGamePlayer()
        .forPlayer(aPlayerWithName("FoldedPlayer")).inStatus(FOLD)
        .build()
fun allInPlayer(): InGamePlayer = buildingAnInGamePlayer()
        .forPlayer(aPlayerWithName("AllInPlayer")).inStatus(ALL_IN)
        .build()
fun callingPlayer(): InGamePlayer = buildingAnInGamePlayer()
        .forPlayer(aPlayerWithName("CallingPlayer")).inStatus(CALL)
        .build()
fun raisingPlayer(): InGamePlayer = buildingAnInGamePlayer()
        .forPlayer(aPlayerWithName("RaisingPlayer")).inStatus(RAISE)
        .build()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
