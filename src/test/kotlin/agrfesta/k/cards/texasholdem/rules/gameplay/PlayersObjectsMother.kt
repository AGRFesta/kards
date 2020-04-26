package agrfesta.k.cards.texasholdem.rules.gameplay

///// Strategies ///////////////////////////////////////////////////////////////////////////////////////////////////////
class PlayerStrategyInterfaceCyclicFixedListImpl(private val actions: List<Action>): PlayerStrategyInterface {
    private var pos: Int = 0
    override fun act(context: GameContext): Action = actions[pos++%actions.size]
    override fun toString(): String = "CyclicFixedStrategy"
}

fun aStrategy(): PlayerStrategyInterface = object : PlayerStrategyInterface {
    override fun act(context: GameContext): Action = anAction()
    override fun toString(): String = "aStrategy"
}

fun strategy(vararg actions: Action): PlayerStrategyInterface =
        PlayerStrategyInterfaceCyclicFixedListImpl(actions.toList())
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Actions //////////////////////////////////////////////////////////////////////////////////////////////////////////
fun anAction(): Action = object : Action {
    override fun getAmount(): Int? = null
}

fun call(): Action = CallAction()
fun raise(amount: Int): Action = RaiseAction(amount)
fun fold(): Action = FoldAction()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///// Players //////////////////////////////////////////////////////////////////////////////////////////////////////////
fun aPlayer() = Player("APlayer", 1000) { aStrategy() }
fun aPlayer(name: String) = Player(name, 1000) { aStrategy() }
fun aPlayer(stack: Int) = Player("APlayer", stack) { aStrategy() }
fun aPlayer(name: String, stack: Int, strategy: PlayerStrategyInterface) = Player(name, stack) { strategy }
fun aPlayer(name: String, stack: Int, status: PlayerStatus, strategy: PlayerStrategyInterface): Player {
    val player = Player(name, stack) { strategy }
    player.status = status
    return player
}

fun foldedPlayer(): Player {
    val player = Player("FoldedPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.FOLD
    return player
}
fun allInPlayer(): Player {
    val player = Player("AllInPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.ALL_IN
    return player
}
fun callingPlayer(): Player {
    val player = Player("CallingPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.CALL
    return player
}
fun raisingPlayer(): Player {
    val player = Player("RaisingPlayer", 1000) { aStrategy() }
    player.status = PlayerStatus.RAISE
    return player
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////