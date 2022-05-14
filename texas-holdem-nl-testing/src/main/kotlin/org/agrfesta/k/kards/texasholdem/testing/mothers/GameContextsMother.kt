package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.Board
import org.agrfesta.k.kards.texasholdem.rules.gameplay.BoardInSequence
import org.agrfesta.k.kards.texasholdem.rules.gameplay.EmptyBoard
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContext
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GameContextImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePayments
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.MutableGameContextImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.MutablePot
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerAction
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PublicInGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Table
import org.agrfesta.k.kards.texasholdem.rules.gameplay.emptyPhasePots
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.aPublicInGamePlayerTable
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.anInGameTable
import java.util.*
import java.util.UUID.randomUUID

fun aGameContext(
    uuid: UUID = randomUUID(),
    table: Table<PublicInGamePlayer> = aPublicInGamePlayerTable(),
    payments: GamePayments = aGamePayments(),
    board: Board = board(),
    history: Map<GamePhase, List<PlayerAction>> = emptyMap(),
    phasePots: Map<GamePhase, MutablePot> = emptyMap()
): GameContext = GameContextImpl(uuid, table, payments, board, history, phasePots)

fun aContext(table: Table<InGamePlayer> = anInGameTable(), payments: GamePayments = aGamePayments())
        : MutableGameContextImpl {
    val phasePots = emptyPhasePots { mutableMapOf() }
    return MutableGameContextImpl(uuid = randomUUID(), table = table, payments = payments,
        board = EmptyBoard(aDeck()) as BoardInSequence, phasePots = phasePots)
}
