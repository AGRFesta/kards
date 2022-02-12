package org.agrfesta.k.kards.texasholdem.rules.gameplay

import agrfesta.k.cards.playingcards.cards.Card
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import io.mockk.every
import io.mockk.mockk
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayerTestBuilder.Companion.buildingAnInGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL

///// Strategies ///////////////////////////////////////////////////////////////////////////////////////////////////////
fun aStrategy(): PlayerStrategyInterface = { anAction() }

fun strategyMock(vararg actions: Action): PlayerStrategyInterface {
    val strategy = mockk<PlayerStrategyInterface>()
    every { strategy(any()) } returnsMany actions.toList()
    return strategy
}

fun folder() = strategyMock(fold())
fun limper() = strategyMock(call())
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Actions //////////////////////////////////////////////////////////////////////////////////////////////////////////
fun anAction(): Action = object : Action {
    override val amount: Int? = null
    override val type: ActionType = ActionType.Call
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

fun aPlayerStack() = PlayerStack(aPlayer(), 1000)

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
        .forPlayer(aPlayerWithName("FoldedPlayer")).inStatus(PlayerStatus.FOLD)
        .build()
fun allInPlayer(): InGamePlayer = buildingAnInGamePlayer()
        .forPlayer(aPlayerWithName("AllInPlayer")).inStatus(PlayerStatus.ALL_IN)
        .build()
fun callingPlayer(): InGamePlayer = buildingAnInGamePlayer()
        .forPlayer(aPlayerWithName("CallingPlayer")).inStatus(CALL)
        .build()
fun raisingPlayer(): InGamePlayer = buildingAnInGamePlayer()
        .forPlayer(aPlayerWithName("RaisingPlayer")).inStatus(PlayerStatus.RAISE)
        .build()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
