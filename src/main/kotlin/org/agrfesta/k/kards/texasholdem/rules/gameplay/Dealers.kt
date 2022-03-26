package org.agrfesta.k.kards.texasholdem.rules.gameplay

import org.agrfesta.k.cards.playingcards.utils.CircularIterator
import org.agrfesta.k.kards.texasholdem.observers.DealerObserver
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.BIG_BLIND
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.SMALL_BLIND
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.UNDER_THE_GUN

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

    protected var amountRequired: UInt = 0u
    private var raisingPlayer: InGamePlayer? = null

    protected abstract fun initPot(pot: InGamePot)
    protected abstract fun playersIterator(): CircularIterator<InGamePlayer>

    override fun collectPot() {
        val pot = context.getPhasePot()
        initPot(pot)
        val iterator = playersIterator()
        while (someoneHaveToAct(pot)) {
            val player = iterator.next()
            if (context.hadToAct(player, pot)) {
                val action = player( player heroIn context )
                val effectiveAction: Action = when (action.type) {
                    ActionType.Call -> callEffect(player, pot)
                    ActionType.Raise -> raiseEffect(player, action, pot)
                    else -> foldEffect(player)
                }
                context.addPlayerActionToPhaseHistory(player does effectiveAction)
                observer?.notifyAction(player statsWith context, player does effectiveAction)
            }
        }
        observer?.notifyActions(context.board.phase, context.getPhaseHistory())
    }

    private fun someoneHaveToAct(pot: InGamePot): Boolean = hadToAct(pot).isNotEmpty()
    private fun hadToAct(pot: InGamePot): List<InGamePlayer> {
        return context.table.players.filter { context.hadToAct(it, pot) }
    }

    private fun callEffect(player: InGamePlayer, pot: InGamePot): Action {
        val payed: UInt = pot.payedBy(player)
        player.status = PlayerStatus.CALL
        pot.receiveFrom(player, amountRequired - payed)
        return call()
    }

    private fun foldEffect(player: InGamePlayer): Action {
        player.status = PlayerStatus.FOLD
        return fold()
    }

    private fun isRaiseAmountLessThanRequired(amount: UInt): Boolean {
        return (raisingPlayer != null && (amount <= amountRequired))
                || amount <= context.payments.bb()
    }

    private fun raiseEffect(player: InGamePlayer, action: Action, pot: InGamePot): Action {
        val payed: UInt = pot.payedBy(player)
        val minimumRaise = context.payments.bb()
        val limitedAmount = action.amount?.coerceAtMost(player.stack) ?: 0u
        return if (isRaiseAmountLessThanRequired(limitedAmount)) {
            callEffect(player, pot)
        } else {
            player.status = PlayerStatus.RAISE
            raisingPlayer = player
            val raiseAmount: UInt = action.amount?.coerceAtLeast(minimumRaise) ?: minimumRaise
            val amount: UInt = pot.receiveFrom(player, raiseAmount)
            val effectiveRaiseAmount: UInt = amount - (amountRequired - payed)
            amountRequired += if (effectiveRaiseAmount > 0u) effectiveRaiseAmount else 0u
            raise(amount)
        }
    }
}

private fun MutableGameContextImpl.hadToAct(player: InGamePlayer, pot: InGamePot): Boolean {
    val hadToPay = player.calculateAmountToCall(pot) > 0u
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
    override fun playersIterator(): CircularIterator<InGamePlayer> = context.table.iterateFrom(SMALL_BLIND)
}

class PreFlopDealer(
    private val context: MutableGameContextImpl,
    observer: DealerObserver? = null )
    : AbstractDealer(context, observer) {
    override fun initPot(pot: InGamePot) {
        pot.receiveFrom(context.table.getPlayerFrom(SMALL_BLIND), context.payments.sb())
        pot.receiveFrom(context.table.getPlayerFrom(BIG_BLIND), context.payments.bb())
        context.payments.ante()?.let { ante -> context.table.players.forEach { pot.receiveFrom(it, ante) } }
        amountRequired = context.payments.bb()
    }
    override fun playersIterator(): CircularIterator<InGamePlayer> = context.table.iterateFrom(UNDER_THE_GUN)
}
