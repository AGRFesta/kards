package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.DealerObserver
import agrfesta.k.cards.texasholdem.playercontext.PlayerAction
import agrfesta.k.cards.texasholdem.playercontext.PlayerGameContext
import agrfesta.k.cards.texasholdem.playercontext.does

interface Dealer {
    fun collectPot(): Pot
}

interface DealerFactory {
    fun preFlopDealer(context: GameContext, observer: DealerObserver?): Dealer
    fun postFlopDealer(prevPot: Pot, context: GameContext, observer: DealerObserver?): Dealer
}
class DealerFactoryImpl: DealerFactory {

    override fun preFlopDealer(context: GameContext, observer: DealerObserver?): Dealer {
        return PreFlopDealer(context, observer)
    }

    override fun postFlopDealer(prevPot: Pot, context: GameContext, observer: DealerObserver?): Dealer {
        return PostFlopDealer(prevPot, context, observer)
    }

}

abstract class AbstractDealer(
        private val context: GameContext,
        private val observer: DealerObserver?) : Dealer {
    init {
        context.table.players
                .filter { it.isActive() }
                .forEach { it.status = PlayerStatus.NONE }
    }

    protected var amountRequired: Int = 0
    private var raisingPlayer: InGamePlayer? = null

    protected abstract fun prevPot(): Pot?
    protected abstract fun createPot(): Pot
    protected abstract fun playersIterator(): TableIterator<InGamePlayer>

    override fun collectPot(): Pot {
        val pot = createPot()
        val iterator = playersIterator()
        val actions: MutableList<PlayerAction> = mutableListOf()

        while (someoneHaveToAct(pot)) {
            val player = iterator.next()
            if (context.hadToAct(player, pot)) {
                val gameContext = context.add(actions)
                        .toPlayerGameContext(player.asOwnPlayer(pot), pot.amount() + (prevPot()?.amount() ?: 0))
                val action = player.act(gameContext)
                observer?.notifyAction(gameContext, player does action)
                actions.add(player does action)
                when (action.getType()) {
                    ActionType.Call -> callEffect(player, pot)
                    ActionType.Raise -> raiseEffect(player, action, pot)
                    else -> foldEffect(player)
                }
            }
        }
        observer?.notifyActions(context.board.info().phase, actions)
        return pot
    }

    private fun someoneHaveToAct(pot: Pot): Boolean = hadToAct(pot).isNotEmpty()
    private fun hadToAct(pot: Pot): List<InGamePlayer> {
        return context.table.players.filter { context.hadToAct(it, pot) }
    }

    private fun callEffect(player: InGamePlayer, pot: Pot) {
        val payed: Int = pot.payedBy(player)
        player.status = PlayerStatus.CALL
        pot.receiveFrom(player, amountRequired - payed)
    }

    private fun foldEffect(player: InGamePlayer) {
        player.status = PlayerStatus.FOLD
    }

    private fun raiseEffect(player: InGamePlayer, action: Action, pot: Pot) {
        val payed: Int = pot.payedBy(player)
        val minimumRaise = context.payments.bb()
        if (raisingPlayer != null && (amountRequired - payed < minimumRaise)) {
            callEffect(player, pot)
        } else {
            player.status = PlayerStatus.RAISE
            raisingPlayer = player
            val raiseAmount: Int = action.getAmount()?.coerceAtLeast(minimumRaise) ?: minimumRaise
            val amount: Int = pot.receiveFrom(player, amountRequired - payed + raiseAmount)
            val effectiveRaiseAmount: Int = amount - (amountRequired - payed)
            amountRequired += if (effectiveRaiseAmount > 0) effectiveRaiseAmount else 0
        }
    }
}

private fun GameContext.toPlayerGameContext(me: OwnPlayer, potAmount: Int) = PlayerGameContext(
        me, this.payments, this.board.info(), potAmount, this.table.map { it.asOpponent() }, this.history)

private fun GameContext.hadToAct(player: InGamePlayer, pot: Pot): Boolean {
    val hadToPay = player.calculateAmountToCall(pot) > 0
    return player.isActive()
            && (!theOnlyActive(player) || hadToPay)
            && (player.status == PlayerStatus.NONE || hadToPay)
}

private fun GameContext.theOnlyActive(player: InGamePlayer): Boolean = this.table.players
        .filter { player !== it }
        .none { it.isActive() }

class PostFlopDealer(
        private val prevPot: Pot,
        private val context: GameContext,
        observer: DealerObserver? = null )
    : AbstractDealer(context, observer) {
    override fun prevPot(): Pot? = prevPot
    override fun createPot() = buildPot()
    override fun playersIterator(): TableIterator<InGamePlayer> = context.table.iterateFromSB()
}

class PreFlopDealer(
        private val context: GameContext,
        observer: DealerObserver? = null )
    : AbstractDealer(context, observer) {

    override fun createPot(): Pot {
        val pot = buildPot()
        pot.receiveFrom(context.table.getPlayerByPosition(Position.SMALL_BLIND), context.payments.sb())
        pot.receiveFrom(context.table.getPlayerByPosition(Position.BIG_BLIND), context.payments.bb())
        context.payments.ante()?.let { ante -> context.table.players.forEach { pot.receiveFrom(it, ante) } }
        amountRequired = context.payments.bb()
        return pot
    }

    override fun prevPot(): Pot? = null
    override fun playersIterator(): TableIterator<InGamePlayer> = context.table.iterateFromUTG()
}
