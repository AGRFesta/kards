package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.observers.DealerObserver

interface Dealer {
    fun collectPot()
}

interface DealerFactory {
    fun preFlopDealer(context: GameContext<InGamePlayer, BoardInSequence>, observer: DealerObserver?): Dealer
    fun postFlopDealer(context: GameContext<InGamePlayer, BoardInSequence>, observer: DealerObserver?): Dealer
}
class DealerFactoryImpl: DealerFactory {

    override fun preFlopDealer(context: GameContext<InGamePlayer, BoardInSequence>, observer: DealerObserver?): Dealer {
        return PreFlopDealer(context, observer)
    }

    override fun postFlopDealer(context: GameContext<InGamePlayer, BoardInSequence>, observer: DealerObserver?): Dealer {
        return PostFlopDealer(context, observer)
    }

}

abstract class AbstractDealer(
        private val context: GameContext<InGamePlayer, BoardInSequence>,
        private val observer: DealerObserver?) : Dealer {
    init {
        context.table.players
                .filter { it.isActive() }
                .forEach { it.status = PlayerStatus.NONE }
    }

    protected var amountRequired: Int = 0
    private var raisingPlayer: InGamePlayer? = null

    protected abstract fun initPot(pot: Pot)
    protected abstract fun playersIterator(): TableIterator<InGamePlayer>

    override fun collectPot() {
        val pot = context.getActualPot()
        initPot(pot)
        val iterator = playersIterator()
        val actions: MutableList<PlayerAction> = mutableListOf()

        while (someoneHaveToAct(pot)) {
            val player = iterator.next()
            if (context.hadToAct(player, pot)) {
                val action = player.act( context.map { it } )
                actions.add(player does action)
                when (action.getType()) {
                    ActionType.Call -> callEffect(player, pot)
                    ActionType.Raise -> raiseEffect(player, action, pot)
                    else -> foldEffect(player)
                }
                observer?.notifyAction(context.map { it }, player does action)
            }
        }
        observer?.notifyActions(context.board.phase(), actions)
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
            val amount: Int = pot.receiveFrom(player, raiseAmount)
            val effectiveRaiseAmount: Int = amount - (amountRequired - payed)
            amountRequired += if (effectiveRaiseAmount > 0) effectiveRaiseAmount else 0
        }
    }
}

private fun GameContext<InGamePlayer, BoardInSequence>.hadToAct(player: InGamePlayer, pot: Pot): Boolean {
    val hadToPay = player.calculateAmountToCall(pot) > 0
    return player.isActive()
            && (!theOnlyActive(player) || hadToPay)
            && (player.status == PlayerStatus.NONE || hadToPay)
}

private fun GameContext<InGamePlayer, BoardInSequence>.theOnlyActive(player: InGamePlayer): Boolean = this.table.players
        .filter { player !== it }
        .none { it.isActive() }

class PostFlopDealer(
        private val context: GameContext<InGamePlayer, BoardInSequence>,
        observer: DealerObserver? = null )
    : AbstractDealer(context, observer) {
    override fun initPot(pot: Pot) {}
    override fun playersIterator(): TableIterator<InGamePlayer> = context.table.iterateFromSB()
}

class PreFlopDealer(
        private val context: GameContext<InGamePlayer, BoardInSequence>,
        observer: DealerObserver? = null )
    : AbstractDealer(context, observer) {
    override fun initPot(pot: Pot) {
        pot.receiveFrom(context.table.getPlayerByPosition(Position.SMALL_BLIND), context.payments.sb())
        pot.receiveFrom(context.table.getPlayerByPosition(Position.BIG_BLIND), context.payments.bb())
        context.payments.ante()?.let { ante -> context.table.players.forEach { pot.receiveFrom(it, ante) } }
        amountRequired = context.payments.bb()
    }
    override fun playersIterator(): TableIterator<InGamePlayer> = context.table.iterateFromUTG()
}
