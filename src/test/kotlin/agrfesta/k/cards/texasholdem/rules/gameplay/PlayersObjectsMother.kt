package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.kcards.playingcards.cards.Card

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
fun aPlayer() = GamePlayer("APlayer", 1000) { aStrategy() }
fun aPlayer(name: String) = GamePlayer(name, 1000) { aStrategy() }
fun aPlayer(stack: Int) = GamePlayer("APlayer", stack) { aStrategy() }
fun aPlayer(name: String, stack: Int) = GamePlayer(name, stack) { aStrategy() }
fun aPlayer(name: String, stack: Int, status: PlayerStatus, cards: Set<Card>) = PlayerTestBuilder()
            .name(name).stack(stack).status(status).cards(cards)
        .build()
fun aPlayer(name: String, stack: Int, strategy: PlayerStrategyInterface) = GamePlayer(name, stack) { strategy }
fun aPlayer(name: String, stack: Int, status: PlayerStatus, strategy: PlayerStrategyInterface) = PlayerTestBuilder()
                .name(name).stack(stack).status(status).strategy { strategy }
            .build()

fun foldedPlayer(): GamePlayer = PlayerTestBuilder()
        .name("FoldedPlayer").stack(1000).status(PlayerStatus.FOLD)
        .build()
fun allInPlayer(): GamePlayer = PlayerTestBuilder()
        .name("AllInPlayer").stack(1000).status(PlayerStatus.ALL_IN)
        .build()
fun callingPlayer(): GamePlayer = PlayerTestBuilder()
        .name("CallingPlayer").stack(1000).status(PlayerStatus.CALL)
        .build()
fun raisingPlayer(): GamePlayer = PlayerTestBuilder()
        .name("RaisingPlayer").stack(1000).status(PlayerStatus.RAISE)
        .build()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////