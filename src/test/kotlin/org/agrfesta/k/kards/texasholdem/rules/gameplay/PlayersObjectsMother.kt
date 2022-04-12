package org.agrfesta.k.kards.texasholdem.rules.gameplay

import io.mockk.every
import io.mockk.mockk
import org.agrfesta.k.cards.playingcards.cards.Card
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.ALL_IN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.NONE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.RAISE
import java.util.*

///// Strategies ///////////////////////////////////////////////////////////////////////////////////////////////////////
fun aStrategy(): PlayerStrategyInterface = { _, _ -> anAction() }

fun strategyMock(vararg actions: Action): PlayerStrategyInterface {
    val strategy = mockk<PlayerStrategyInterface>()
    every { strategy(any(), any()) } returnsMany actions.toList()
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
val alex = aPlayer(name = "Alex")
val poly = aPlayer(name = "Poly")
val jane = aPlayer(name = "Jane")
val dave = aPlayer(name = "Dave")
val maya = aPlayer(name = "Maya")
val juno = aPlayer(name = "Juno")

fun aPlayerCardsSet() = frenchCardsSet("7s","2c")

fun anIdentity(uuid: UUID = UUID.randomUUID(), name: String = "aName"): PlayerIdentity = uuid identifies name

fun aPlayer(uuid: UUID = UUID.randomUUID(), name: String = "aName", strategy: PlayerStrategyInterface = aStrategy()):
        Player = uuid identifies name playingAs strategy

fun aPublicInGamePlayer(
    uuid: UUID = UUID.randomUUID(),
    name: String = "aName",
    stack: UInt = 0u,
    status: PlayerStatus = NONE): PublicInGamePlayer =
    PublicInGamePlayerImpl(PublicSittingPlayerImpl(uuid identifies name, stack), status)

fun aSittingPlayer(player: Player, stack: UInt = 0u): SittingPlayer = player sittingWith stack
fun aSittingPlayer(
    uuid: UUID = UUID.randomUUID(),
    name: String = "aName",
    stack: UInt = 0u,
    strategy: PlayerStrategyInterface = aStrategy()): SittingPlayer =
    uuid identifies name playingAs strategy sittingWith stack

fun anInGamePlayer(
    uuid: UUID = UUID.randomUUID(),
    name: String = "aName",
    stack: UInt = 0u,
    strategy: PlayerStrategyInterface = aStrategy(),
    status: PlayerStatus = NONE,
    cards: Set<Card> = aPlayerCardsSet()): InGamePlayer =
    InGamePlayerImpl(uuid identifies name playingAs strategy sittingWith stack, status, cards)

fun foldedPlayer(): InGamePlayer = anInGamePlayer(name = "FoldedPlayer", status = FOLD)
fun allInPlayer(): InGamePlayer = anInGamePlayer(name = "AllInPlayer", status = ALL_IN)
fun callingPlayer(): InGamePlayer = anInGamePlayer(name = "CallingPlayer", status = CALL)
fun raisingPlayer(): InGamePlayer = anInGamePlayer(name = "RaisingPlayer", status = RAISE)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
