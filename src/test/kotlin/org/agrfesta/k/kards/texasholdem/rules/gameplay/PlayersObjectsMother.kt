package org.agrfesta.k.kards.texasholdem.rules.gameplay

import io.mockk.every
import io.mockk.mockk
import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayerTestBuilder.Companion.buildingAnInGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.NONE

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
    override val amount: UInt? = null
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
fun anOpponent(
    name: String = "APlayer",
    stack: UInt = 1000u,
    status: PlayerStatus = NONE) = Opponent(name, stack, status)

fun aPlayerCardsSet() = frenchCardsSet("7s","2c")

fun aPlayerStack(player: Player = aPlayer(), stack: UInt = 1000u) = PlayerStack(player, stack)

fun anInGamePlayer() = InGamePlayer(aPlayer(), 1000u, aPlayerCardsSet())
fun anInGamePlayer(name: String) = InGamePlayer(aPlayerWithName(name), 1000u, aPlayerCardsSet())
fun anInGamePlayer(stack: UInt) = InGamePlayer(aPlayer(), stack, aPlayerCardsSet())
fun anInGamePlayer(name: String, stack: UInt, status: PlayerStatus, cards: Set<Card>) = buildingAnInGamePlayer()
        .forPlayer(Player(name, aStrategy()))
        .withAStackOf(stack)
        .inStatus(status)
        .withCards(cards)
        .build()
fun anInGamePlayer(name: String, stack: UInt, strategy: PlayerStrategyInterface) =
        InGamePlayer(Player(name,strategy), stack, aPlayerCardsSet())
fun anInGamePlayer(name: String, stack: UInt, status: PlayerStatus, strategy: PlayerStrategyInterface) =
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
