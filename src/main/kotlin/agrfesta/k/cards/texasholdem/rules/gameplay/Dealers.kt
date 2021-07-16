package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.DealerObserver

interface Dealer {
    fun collectPot()
}

interface DealerFactory {
    fun preFlopDealer(context: MutableGameContextImpl, observer: DealerObserver?): Dealer
    fun postFlopDealer(context: MutableGameContextImpl, observer: DealerObserver?): Dealer
}
class DealerFactoryImpl: DealerFactory {

    override fun preFlopDealer(context: MutableGameContextImpl, observer: DealerObserver?): Dealer {
        return PreFlopDealer(context, observer)
    }

    override fun postFlopDealer(context: MutableGameContextImpl, observer: DealerObserver?): Dealer {
        return PostFlopDealer(context, observer)
    }

}

abstract class AbstractDealer(
    private val context: MutableGameContextImpl,
    private val observer: DealerObserver?) : Dealer {
    init {
        context.table.players
                .filter { it.isActive() }
                .forEach { it.status = PlayerStatus.NONE }
    }

    protected var amountRequired: Int = 0
    private var raisingPlayer: InGamePlayer? = null

    protected abstract fun initPot(pot: InGamePot)
    protected abstract fun playersIterator(): TableIterator<InGamePlayer>

    override fun collectPot() {
        val pot = context.getPhasePot()
        initPot(pot)
        val iterator = playersIterator()
        val actions: MutableList<PlayerAction> = mutableListOf()
        while (someoneHaveToAct(pot)) {
            val player = iterator.next()
            if (context.hadToAct(player, pot)) {
                val action = player( player heroIn context )
                val effectiveAction: Action = when (action.type) {
                    ActionType.Call -> callEffect(player, pot)
                    ActionType.Raise -> raiseEffect(player, action, pot)
                    else -> foldEffect(player)
                }
                actions.add(player does effectiveAction)
                observer?.notifyAction(player statsWith context, player does effectiveAction)
            }
        }
        observer?.notifyActions(context.board.phase, actions)
    }

    private fun someoneHaveToAct(pot: InGamePot): Boolean = hadToAct(pot).isNotEmpty()
    private fun hadToAct(pot: InGamePot): List<InGamePlayer> {
        return context.table.players.filter { context.hadToAct(it, pot) }
    }

    private fun callEffect(player: InGamePlayer, pot: InGamePot): Action {
        val payed: Int = pot.payedBy(player)
        player.status = PlayerStatus.CALL
        pot.receiveFrom(player, amountRequired - payed)
        return call()
    }

    private fun foldEffect(player: InGamePlayer): Action {
        player.status = PlayerStatus.FOLD
        return fold()
    }

    private fun isRaiseAmountLessThanRequired(amount: Int): Boolean {
        return (raisingPlayer != null && (amount <= amountRequired))
                || amount <= context.payments.bb()
    }

    private fun raiseEffect(player: InGamePlayer, action: Action, pot: InGamePot): Action {
        val payed: Int = pot.payedBy(player)
        val minimumRaise = context.payments.bb()
        val limitedAmount = action.amount?.coerceAtMost(player.stack) ?: 0
        return if (isRaiseAmountLessThanRequired(limitedAmount)) {
            callEffect(player, pot)
        } else {
            player.status = PlayerStatus.RAISE
            raisingPlayer = player
            val raiseAmount: Int = action.amount?.coerceAtLeast(minimumRaise) ?: minimumRaise
            val amount: Int = pot.receiveFrom(player, raiseAmount)
            val effectiveRaiseAmount: Int = amount - (amountRequired - payed)
            amountRequired += if (effectiveRaiseAmount > 0) effectiveRaiseAmount else 0
            raise(amount)
        }
    }
}

private fun MutableGameContextImpl.hadToAct(player: InGamePlayer, pot: InGamePot): Boolean {
    val hadToPay = player.calculateAmountToCall(pot) > 0
    return player.isActive()
            && (!theOnlyActive(player) || hadToPay)
            && (player.status == PlayerStatus.NONE || hadToPay)
}

private fun MutableGameContextImpl.theOnlyActive(player: InGamePlayer): Boolean = table.players
        .filter { player !== it }
        .none { it.isActive() }

class PostFlopDealer(
    private val context: MutableGameContextImpl,
    observer: DealerObserver? = null )
    : AbstractDealer(context, observer) {
    override fun initPot(pot: InGamePot) {/**/}
    override fun playersIterator(): TableIterator<InGamePlayer> = context.table.iterateFromSB()
}

class PreFlopDealer(
    private val context: MutableGameContextImpl,
    observer: DealerObserver? = null )
    : AbstractDealer(context, observer) {
    override fun initPot(pot: InGamePot) {
        pot.receiveFrom(context.table.getPlayerByPosition(Position.SMALL_BLIND), context.payments.sb())
        pot.receiveFrom(context.table.getPlayerByPosition(Position.BIG_BLIND), context.payments.bb())
        context.payments.ante()?.let { ante -> context.table.players.forEach { pot.receiveFrom(it, ante) } }
        amountRequired = context.payments.bb()
    }
    override fun playersIterator(): TableIterator<InGamePlayer> = context.table.iterateFromUTG()
}
