package agrfesta.k.cards.texasholdem.rules.gameplay


interface Dealer {
    fun collectPot(): MutableMap<Player,Int>
}

abstract class AbstractDealer(private val context: GameContext): Dealer {
    init {
        context.table.players
                .filter { it.isActive() }
                .forEach { it.status=PlayerStatus.NONE }
    }

    protected var amountRequired: Int = 0
    private var raisingPlayer: Player? = null

    protected abstract fun createPot(): MutableMap<Player,Int>
    protected abstract fun playersIterator(): TableIterator

    override fun collectPot(): MutableMap<Player,Int> {
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

    private fun someoneHaveToAct(pot: MutableMap<Player,Int>): Boolean = hadToAct(pot).isNotEmpty()
    private fun hadToAct(pot: MutableMap<Player,Int>): List<Player> {
        return context.table.players.filter { hadToAct(it,pot) }
    }
    private fun theOnlyActive(player: Player): Boolean = context.table.players
            .filter { player !== it }
            .none { it.isActive() }
    private fun hadToPay(player: Player, pot: MutableMap<Player,Int>): Boolean {
        val payed: Int = pot.payedBy(player)
        return payed!=pot.maxContribution()?.amount?:0
    }
    private fun hadToAct(player: Player, pot: MutableMap<Player,Int>): Boolean {
        val hadToPay = hadToPay(player, pot)
        return player.isActive()
                && (!theOnlyActive(player) || hadToPay)
                && (player.status==PlayerStatus.NONE || hadToPay)
    }

    private fun callEffect(player: Player, pot: MutableMap<Player,Int>) {
        val payed: Int = pot.payedBy(player)
        player.status = PlayerStatus.CALL
        pot.receiveFrom(player, amountRequired-payed)
    }
    private fun foldEffect(player: Player) {
        player.status = PlayerStatus.FOLD
    }
    private fun raiseEffect(player: Player, action: RaiseAction, pot: MutableMap<Player,Int>) {
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
        private val prevPot: MutableMap<Player,Int>,
        private val context: GameContext
    ): AbstractDealer(context) {

    override fun createPot() = buildPot()
    override fun playersIterator(): TableIterator = context.table.iterateFromSB()
    override fun collectPot(): MutableMap<Player,Int> = super.collectPot() + prevPot
}

class PreFlopDealer (private val context: GameContext): AbstractDealer(context) {

    override fun createPot(): MutableMap<Player, Int> {
        val pot = buildPot()
        pot.receiveFrom(context.table.getSB(),context.payments.sb())
        pot.receiveFrom(context.table.getBB(),context.payments.bb())
        context.payments.ante()?.let { ante -> context.table.players.forEach { pot.receiveFrom(it,ante) }  }
        amountRequired = context.payments.bb()
        return pot
    }

    override fun playersIterator(): TableIterator = context.table.iterateFromUTG()
}
