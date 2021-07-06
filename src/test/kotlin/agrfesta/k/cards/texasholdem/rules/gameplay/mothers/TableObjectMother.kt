package agrfesta.k.cards.texasholdem.rules.gameplay.mothers

import agrfesta.k.cards.texasholdem.rules.gameplay.Player
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStack
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStrategyInterface
import agrfesta.k.cards.texasholdem.rules.gameplay.Table
import agrfesta.k.cards.texasholdem.rules.gameplay.buildTable

//val utg = OpponentTestBuilder().withName("UTG").build()
//val utg1 = OpponentTestBuilder().withName("UTG +1").build()
//val utg2 = OpponentTestBuilder().withName("UTG +2").build()
//val middle = OpponentTestBuilder().withName("Middle").build()
//val middle1 = OpponentTestBuilder().withName("Middle +1").build()
//val middle2 = OpponentTestBuilder().withName("Middle +2").build()
//val late = OpponentTestBuilder().withName("Late").build()
private const val BUTTON_POS = 0
private const val SMALL_BLIND_POS = 1
private const val BIG_BLIND_POS = 2
private const val UNDER_THE_GUN_POS = 3

const val BUTTON = "Button"
const val SMALL_BLIND = "Small Blind"
const val BIG_BLIND = "Big Blind"
const val UNDER_THE_GUN = "UTG"

class TestTableBuilder {
    val players: MutableMap<Int,PlayerStack> = mutableMapOf()

    fun button(stack: Int, strategy: PlayerStrategyInterface): TestTableBuilder {
        players[BUTTON_POS] = PlayerStack(Player(BUTTON, strategy), stack)
        return this
    }
    fun smallBlind(stack: Int, strategy: PlayerStrategyInterface): TestTableBuilder {
        players[SMALL_BLIND_POS] = PlayerStack(Player(SMALL_BLIND, strategy), stack)
        return this
    }
    fun bigBlind(stack: Int, strategy: PlayerStrategyInterface): TestTableBuilder {
        players[BIG_BLIND_POS] = PlayerStack(Player(BIG_BLIND, strategy), stack)
        return this
    }
    fun underTheGun(stack: Int, strategy: PlayerStrategyInterface): TestTableBuilder {
        players[UNDER_THE_GUN_POS] = PlayerStack(Player(UNDER_THE_GUN, strategy), stack)
        return this
    }

    fun build(): Table<PlayerStack> = buildTable {
            withPlayers(
                players.entries
                    .sortedBy { it.key }
                    .map { it.value }
            )
            withButtonInPosition(0)
        }

}

fun buildTestTable(
    setup: TestTableBuilder.() -> Unit
): Table<PlayerStack> {
    val builder = TestTableBuilder()
    builder.setup()
    return builder.build()
}


