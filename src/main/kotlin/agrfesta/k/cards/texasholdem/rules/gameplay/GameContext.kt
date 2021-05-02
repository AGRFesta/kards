package agrfesta.k.cards.texasholdem.rules.gameplay

import java.util.*

typealias InGameContext = GameContext<InGamePlayer, BoardInSequence>
typealias ViewGameContext = GameContext<SeatNameStack, Board>
typealias ActGameContext = HeroGameContext<OwnPlayer, Opponent, Board>
typealias ViewHeroGameContext = HeroGameContext<OpponentHero, SeatNameStack, Board>

interface GameContext<T: SeatNameStack, B: Board> {
    val uuid: UUID
    val table: Table<T>
    val payments: GamePayments
    val board: B
    val history: Map<GamePhase, List<PlayerAction>>
    val phasePots: Map<GamePhase, MutablePot>

    fun getActualPot() = phasePots[board.phase()]
        ?: throw IllegalStateException("Pot not initialized at ${board.phase()}")
    fun getGlobalPot() = phasePots.values.reduce { a, b -> a + b }
    fun getActions(phase: GamePhase) = history.getOrElse(phase){ emptyList() }
    fun getActualActions() = history.getOrElse(board.phase()){ emptyList() }
}
interface HeroGameContext<H: SeatNameStack, T: SeatNameStack, B: Board>: GameContext<T, B> {
    val hero: H
}

class GameContextImpl<T: SeatNameStack, B: Board>(
    override val uuid: UUID,
    override val table: Table<T>,
    override val payments: GamePayments,
    override val board: B,
    override val history: Map<GamePhase, List<PlayerAction>> = mapOf(
        GamePhase.PRE_FLOP to emptyList(),
        GamePhase.FLOP to emptyList(),
        GamePhase.TURN to emptyList(),
        GamePhase.RIVER to emptyList()
    ),
    override val phasePots: Map<GamePhase, MutablePot> = mapOf(
        GamePhase.PRE_FLOP to buildMutablePot(),
        GamePhase.FLOP to buildMutablePot(),
        GamePhase.TURN to buildMutablePot(),
        GamePhase.RIVER to buildMutablePot()
    )): GameContext<T, B>

class HeroGameContextImpl<H: SeatNameStack, T: SeatNameStack, B: Board>(
    override val uuid: UUID,
    override val hero: H,
    override val table: Table<T>,
    override val payments: GamePayments,
    override val board: B,
    override val history: Map<GamePhase, List<PlayerAction>> = mapOf(
        GamePhase.PRE_FLOP to emptyList(),
        GamePhase.FLOP to emptyList(),
        GamePhase.TURN to emptyList(),
        GamePhase.RIVER to emptyList()
    ),
    override val phasePots: Map<GamePhase, MutablePot> = mapOf(
        GamePhase.PRE_FLOP to buildMutablePot(),
        GamePhase.FLOP to buildMutablePot(),
        GamePhase.TURN to buildMutablePot(),
        GamePhase.RIVER to buildMutablePot()
    )): HeroGameContext<H, T, B> {

    fun <J: SeatNameStack, N: SeatNameStack, M: Board> map(
        heroMapper: ((H) -> J)? = null,
        tablePlayerMapper: ((T) -> N)? = null,
        boardMapper: ((B) -> M)? = null )
    : HeroGameContext<J, N, M> = HeroGameContextImpl(
        uuid,
        heroMapper?.invoke(hero) ?: hero as J,
        table.map { tablePlayerMapper?.invoke(it) ?: it as N },
        payments,
        boardMapper?.invoke(board) ?: board as M,
        history,
        phasePots )

}

fun InGameContext.nextPhase(): InGameContext =
    GameContextImpl(uuid, table, payments, board.next(), history, phasePots)

data class PlayerAction(val playerName: String, val action: Action) {
    override fun toString() = "$playerName $action"
}
infix fun SeatName.does(action: Action) = PlayerAction(this.name, action)

fun <H: SeatNameStack, T: SeatNameStack, B: Board, H1: SeatNameStack, T1: SeatNameStack, B1: Board> H.with(
    context: GameContext<T, B>,
    heroMapper: ((H) -> H1)? = null,
    tablePlayerMapper: ((T) -> T1)? = null,
    boardMapper: ((B) -> B1)? = null ): HeroGameContext<H1, T1, B1> = HeroGameContextImpl(
        context.uuid,
        heroMapper?.invoke(this) ?: this as H1,
        context.table.map { tablePlayerMapper?.invoke(it) ?: it as T1 },
        context.payments,
        boardMapper?.invoke(context.board) ?: context.board as B1,
        context.history,
        context.phasePots)

infix fun InGamePlayer.heroIn(context: InGameContext)
        : ActGameContext = this.with(context,
            { it.asOwnPlayer(context.getActualPot()) },
            { it.asOpponent() },
            { it as Board } )

infix fun InGamePlayer.statsWith(context: InGameContext)
        : ViewHeroGameContext = this.with(context,
            { OpponentHero(it.name, it.stack) },
            { it.asOpponent() },
            { it as Board })

fun <T: SeatNameStack, B: Board, T1: SeatNameStack, B1: Board> GameContext<T, B>.map(
    tablePlayerMapper: ((T) -> T1)? = null,
    boardMapper: ((B) -> B1)? = null )
        : GameContext<T1, B1> = GameContextImpl(
            uuid,
            table.map { tablePlayerMapper?.invoke(it) ?: it as T1 },
            payments,
            boardMapper?.invoke(board) ?: board as B1,
            history,
            phasePots )

fun InGameContext.toViewGameContext(): ViewGameContext = this.map(
        { it.asOpponent() },
        { it as Board })
