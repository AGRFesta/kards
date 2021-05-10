package agrfesta.k.cards.texasholdem.rules.gameplay

import java.util.*

typealias InGameContext = GameContext<InGamePlayer, BoardInSequence, InGamePlayer, InGamePot>
typealias ViewGameContext = GameContext<SeatNameStack, Board, SeatName, Pot<SeatName>>
typealias ActGameContext = HeroGameContext<OwnPlayer, Opponent, Board, SeatName, Pot<SeatName>>
typealias ViewHeroGameContext = HeroGameContext<OpponentHero, SeatNameStack, Board, SeatName, Pot<SeatName>>

typealias ActionsHistory = Map<GamePhase, List<PlayerAction>>

interface GameContext<T: SeatNameStack, B: Board, N: SeatName, P: Pot<N>> {
    val uuid: UUID
    val table: Table<T>
    val payments: GamePayments
    val board: B
    val history: Map<GamePhase, List<PlayerAction>>
    val phasePots: Map<GamePhase, P>

    fun getActualPot() = phasePots[board.phase]
        ?: throw IllegalStateException("Pot not initialized at ${board.phase}")
    fun getGlobalPot(): P = phasePots.values.reduce { a: P, b: P -> (a + b) as P }
    fun getActions(phase: GamePhase) = history.getOrElse(phase){ emptyList() }
    fun getActualActions() = history.getOrElse(board.phase){ emptyList() }
    fun <T1: SeatNameStack, B1: Board, N1: SeatName, P1: Pot<N1>> map(
        playerMapper: (T) -> T1,
        boardMapper: (B) -> B1,
        potMapper: (P) -> P1 ): GameContext<T1, B1, N1, P1> = GameContextImpl(
            uuid,
            table.map { playerMapper(it) },
            payments,
            boardMapper(board),
            history,
            phasePots.mapValues { potMapper(it.value) } )
}
interface HeroGameContext<H: SeatNameStack, T: SeatNameStack, B: Board, N: SeatName, P: Pot<N>> {
    val hero: H
    val context: GameContext<T, B, N, P>
}

class GameContextImpl<T: SeatNameStack, B: Board, N: SeatName, P: Pot<N>>(
    override val uuid: UUID,
    override val table: Table<T>,
    override val payments: GamePayments,
    override val board: B,
    override val history: ActionsHistory = emptyHistory(),
    override val phasePots: Map<GamePhase, P> = emptyPhasePots { mapOf<N, Int>() as P }
    ): GameContext<T, B, N, P>

class HeroGameContextImpl<H: SeatNameStack, T: SeatNameStack, B: Board, N: SeatName, P: Pot<N>>(
    override val hero: H,
    override val context: GameContext<T, B, N, P>
    ): HeroGameContext<H, T, B, N, P> {

    fun <H1: SeatNameStack, T1: SeatNameStack, B1: Board, N1: SeatName, P1: Pot<N1>> map(
        heroMapper: (H) -> H1,
        playerMapper: (T) -> T1,
        boardMapper: (B) -> B1,
        potMapper: (P) -> P1): HeroGameContextImpl<H1, T1, B1, N1, P1> {
        return HeroGameContextImpl(
            heroMapper(hero),
            context.map( playerMapper, boardMapper, potMapper ))
    }
}

fun InGameContext.nextPhase(): InGameContext =
    GameContextImpl(uuid, table, payments, board.next(), history, phasePots)

data class PlayerAction(val playerName: String, val action: Action) {
    override fun toString() = "$playerName $action"
}
infix fun SeatName.does(action: Action) = PlayerAction(this.name, action)

internal fun <H: SeatNameStack, T: SeatNameStack, B: Board, N: SeatName, P: Pot<N>,
        H1: SeatNameStack, T1: SeatNameStack, B1: Board, N1: SeatName, P1: Pot<N1>> H.with(
            context: GameContext<T, B, N, P>,
            heroMapper: (H) -> H1,
            playerMapper: (T) -> T1,
            boardMapper: (B) -> B1,
            potMapper: (P) -> P1): HeroGameContext<H1, T1, B1, N1, P1> = HeroGameContextImpl(
                heroMapper(this),
                context.map(playerMapper, boardMapper, potMapper))

infix fun InGamePlayer.heroIn(context: InGameContext)
        : ActGameContext = this.with(context,
            { it.asOwnPlayer(context.getActualPot()) },
            { it.asOpponent() },
            { it as Board },
            { it.mapKeys { entry -> entry.key as SeatName } })

infix fun InGamePlayer.statsWith(context: InGameContext)
        : ViewHeroGameContext = this.with(context,
            { OpponentHero(it.name, it.stack) },
            { it.asOpponent() as SeatNameStack },
            { it as Board },
            { it.mapKeys { entry -> entry as SeatName } })

fun InGameContext.toViewGameContext(): ViewGameContext = this.map(
        { it as SeatNameStack },
        { it as Board },
        { it.mapKeys { entry -> entry as SeatName } })

fun emptyHistory(): ActionsHistory = mapOf(
    GamePhase.PRE_FLOP to emptyList(),
    GamePhase.FLOP to emptyList(),
    GamePhase.TURN to emptyList(),
    GamePhase.RIVER to emptyList()
)

fun <T: SeatName, P: Pot<T>> emptyPhasePots(initialPotSupplier: () -> P): Map<GamePhase, P> = mapOf(
    GamePhase.PRE_FLOP to initialPotSupplier(),
    GamePhase.FLOP to initialPotSupplier(),
    GamePhase.TURN to initialPotSupplier(),
    GamePhase.RIVER to initialPotSupplier()
)
