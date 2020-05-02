package agrfesta.k.cards.texasholdem.rules.gameplay


interface Dealer {
    fun collectPot(): MutableMap<GamePlayer,Int>
}

abstract class AbstractDealer(private val context: GameContext): Dealer {
    init {
        context.table.players
                .filter { it.isActive() }
                .forEach { it.status=PlayerStatus.NONE }
    }

    protected var amountRequired: Int = 0
    private var raisingPlayer: GamePlayer? = null

    protected abstract fun createPot(): MutableMap<GamePlayer,Int>
    protected abstract fun playersIterator(): TableIterator

    override fun collectPot(): MutableMap<GamePlayer,Int> {
        val pot = createPot()
        val iterator = playersIterator()

        while ( someoneHaveToAct(pot) ) {
            val player = iterator.next()
            if (hadToAct(player, pot)) {
                when (val action = player.act(context)) {
                    is CallAction -> callEffect(player,pot)
                    is RaiseAction -> raiseEffect(player, action, pot)
                    else -> foldEffect(player)
                }
            }
        }

        return pot
    }

    private fun someoneHaveToAct(pot: MutableMap<GamePlayer,Int>): Boolean = hadToAct(pot).isNotEmpty()
    private fun hadToAct(pot: MutableMap<GamePlayer,Int>): List<GamePlayer> {
        return context.table.players.filter { hadToAct(it,pot) }
    }
    private fun theOnlyActive(player: GamePlayer): Boolean = context.table.players
            .filter { player !== it }
            .none { it.isActive() }
    private fun hadToPay(player: GamePlayer, pot: MutableMap<GamePlayer,Int>): Boolean {
        val payed: Int = pot.payedBy(player)
        return payed!=pot.maxContribution()?.amount?:0
    }
    private fun hadToAct(player: GamePlayer, pot: MutableMap<GamePlayer,Int>): Boolean {
        val hadToPay = hadToPay(player, pot)
        return player.isActive()
                && (!theOnlyActive(player) || hadToPay)
                && (player.status==PlayerStatus.NONE || hadToPay)
    }

    private fun callEffect(player: GamePlayer, pot: MutableMap<GamePlayer,Int>) {
        val payed: Int = pot.payedBy(player)
        player.status = PlayerStatus.CALL
        pot.receiveFrom(player, amountRequired-payed)
    }
    private fun foldEffect(player: GamePlayer) {
        player.status = PlayerStatus.FOLD
    }
    private fun raiseEffect(player: GamePlayer, action: RaiseAction, pot: MutableMap<GamePlayer,Int>) {
        val payed: Int = pot.payedBy(player)
        val minimumRaise = context.payments.bb()
        if (raisingPlayer!=null && (amountRequired-payed < minimumRaise)) {
            callEffect(player,pot)
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

class PostFlopDealer (
        private val prevPot: MutableMap<GamePlayer,Int>,
        private val context: GameContext
    ): AbstractDealer(context) {

    override fun createPot() = buildPot()
    override fun playersIterator(): TableIterator = context.table.iterateFromSB()
    override fun collectPot(): MutableMap<GamePlayer,Int> = super.collectPot() + prevPot
}

class PreFlopDealer (private val context: GameContext): AbstractDealer(context) {

    override fun createPot(): MutableMap<GamePlayer, Int> {
        val pot = buildPot()
        pot.receiveFrom(context.table.getSB(),context.payments.sb())
        pot.receiveFrom(context.table.getBB(),context.payments.bb())
        context.payments.ante()?.let { ante -> context.table.players.forEach { pot.receiveFrom(it,ante) }  }
        amountRequired = context.payments.bb()
        return pot
    }

    override fun playersIterator(): TableIterator = context.table.iterateFromUTG()
}
