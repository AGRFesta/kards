package org.agrfesta.k.kards.texasholdem.rules.gameplay

import java.util.*

interface GameContext {
    val uuid: UUID
    val table: Table<PublicInGamePlayer>
    val payments: GamePayments
    val board: Board
    val history: Map<GamePhase, List<PlayerAction>>
    val phasePots: Map<GamePhase, Pot>

    fun getPhaseHistory(): List<PlayerAction>
    fun getGlobalPot(): Pot
}
class GameContextImpl(
    override val uuid: UUID,
    override val table: Table<PublicInGamePlayer>,
    override val payments: GamePayments,
    override val board: Board,
    override val history: Map<GamePhase, List<PlayerAction>>,
    override val phasePots: Map<GamePhase, Pot>
): GameContext {

    override fun getPhaseHistory(): List<PlayerAction> = history[board.phase] ?: emptyList()
    override fun getGlobalPot(): Pot = phasePots.values
        .map { it.toMutablePot() }
        .reduceOrNull { a, b -> a + b } ?: emptyMap()

}

class MutableGameContextImpl(
    val uuid: UUID,
    val table: Table<InGamePlayer>,
    val payments: GamePayments,
    var board: BoardInSequence,
    val history: Map<GamePhase, MutableList<PlayerAction>>  = mapOf(
        GamePhase.PRE_FLOP to mutableListOf(),
        GamePhase.FLOP to mutableListOf(),
        GamePhase.TURN to mutableListOf(),
        GamePhase.RIVER to mutableListOf()
    ),
    val phasePots: Map<GamePhase, MutablePot>
) {
    fun getGlobalPot(): MutablePot = phasePots.values.reduce { a, b -> (a + b) }
    fun getPhasePot() = phasePots[board.phase]
        ?: throw IllegalStateException("Pot not initialized at ${board.phase}")
    fun getPhaseHistory(): List<PlayerAction> = history[board.phase]
        ?: throw IllegalStateException("History not initialized at ${board.phase}")
    fun addPlayerActionToPhaseHistory(playerAction: PlayerAction) {
        history[board.phase]?.add(playerAction)
            ?: throw IllegalStateException("History not initialized at ${board.phase}")
    }

    fun toGameContext(): GameContext = GameContextImpl(
        uuid = uuid,
        table = table.map { it.asPublicInGamePlayer() },
        payments = payments,
        board = board as Board,
        history = history.mapValues { (_, history) -> history.toList() },
        phasePots = phasePots
    )
}

data class PlayerAction(val player: PlayerIdentity, val action: Action) {
    override fun toString() = "${player.name} $action"
}
infix fun PlayerIdentity.does(action: Action) = PlayerAction(this, action)

fun emptyPhasePots(initialPotSupplier: () -> MutablePot): Map<GamePhase, MutablePot> = mapOf(
    GamePhase.PRE_FLOP to initialPotSupplier(),
    GamePhase.FLOP to initialPotSupplier(),
    GamePhase.TURN to initialPotSupplier(),
    GamePhase.RIVER to initialPotSupplier()
)


