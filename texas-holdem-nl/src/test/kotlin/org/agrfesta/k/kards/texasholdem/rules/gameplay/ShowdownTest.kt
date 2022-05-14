package org.agrfesta.k.kards.texasholdem.rules.gameplay

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.agrfesta.k.cards.playingcards.suits.frenchCardsSet
import org.agrfesta.k.kards.texasholdem.rules.CardsEvaluatorBaseImpl
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.ALL_IN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.FOLD
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.NONE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.RAISE
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.BIG_BLIND
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.BUTTON
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.SMALL_BLIND
import org.agrfesta.k.kards.texasholdem.rules.gameplay.Position.UNDER_THE_GUN
import org.agrfesta.k.kards.texasholdem.testing.mothers.anInGamePlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.board
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.aTenInGamePlayerTable
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.bigB
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.button
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.smallB
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.utg
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Showdown tests")
class ShowdownTest {
    private val showdown = ShowdownImpl(CardsEvaluatorBaseImpl())

    @Test
    @DisplayName("Showdown story: Alex wins all the pot")
    fun showdownStory000() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","7c")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1600u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Jane wins, Alex wins the remaining pot")
    fun showdownStory001() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","7c")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("9d","9c")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        pot[bigB] = 100u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1400u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1000u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1300u)
    }
    @Test
    @DisplayName("Showdown story: Jane wins, Dave wins the second pot, Alex wins the remaining pot")
    fun showdownStory002() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","7c")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("9d","9c")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jc","9h")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        pot[bigB] = 100u
        pot[utg] = 200u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1200u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1000u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1400u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(1300u)
    }
    @Test
    @DisplayName("Showdown story: Alex and Poly win")
    fun showdownStory003() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("As","Td")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("9d","Tc")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jc","7h")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        pot[bigB] = 100u
        pot[utg] = 200u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1450u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1450u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1000u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Jane and Dave win, Alex and Poly share the rest")
    fun showdownStory004() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("As","Td")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","9c")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jh","9d")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        pot[bigB] = 100u
        pot[utg] = 200u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1100u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1100u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1200u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(1500u)
    }
    @Test
    @DisplayName("Showdown story: All win")
    fun showdownStory005() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("As","Td")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","9c")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = CALL,  cards = frenchCardsSet("Jh","9d")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        pot[bigB] = 100u
        pot[utg] = 200u
        val board = board("Ac","Jc","Qc", "Kc", "Tc")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1300u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1300u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1100u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(1200u)
    }
    @Test
    @DisplayName("Showdown story: Jane wins but folded, Dave wins, Alex and Poly share the rest")
    fun showdownStory006() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = RAISE,  cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = ALL_IN, cards = frenchCardsSet("As","Td")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = FOLD,   cards = frenchCardsSet("Jd","9c")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = CALL,   cards = frenchCardsSet("Jh","9d")) }
        }
        val pot = buildMutablePot()
        pot[button] = 300u
        pot[smallB] = 300u
        pot[bigB] = 100u
        pot[utg] = 200u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1100u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1100u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1000u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(1700u)
    }
    @Test
    @DisplayName("Showdown story: Poly wins, Jane wins second but folded, Alex take the rest")
    fun showdownStory007() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = RAISE,  cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = ALL_IN, cards = frenchCardsSet("As","9d")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = FOLD,   cards = frenchCardsSet("Ah","Qc")) }
        }
        val pot = buildMutablePot()
        pot[button] = 500u
        pot[smallB] = 50u
        pot[bigB] = 120u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1520u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1150u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Poly wins, Jane wins second, Alex take the rest")
    fun showdownStory008() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = NONE,   cards = frenchCardsSet("Ad","Ts")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = ALL_IN, cards = frenchCardsSet("As","9d")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = ALL_IN, cards = frenchCardsSet("Ah","Qc")) }
        }
        val pot = buildMutablePot()
        pot[button] = 200u
        pot[smallB] = 50u
        pot[bigB] = 120u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1080u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1150u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1140u)
    }
    @Test
    @DisplayName("Showdown story: Alex, Poly and Jane win and share a pot of 400, 133 each")
    fun showdownStory009() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("Ad","9h")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("As","9d")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("Ah","9c")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = FOLD, cards = frenchCardsSet("Qh","Qc")) }
        }
        val pot = buildMutablePot()
        pot[button] = 100u
        pot[smallB] = 100u
        pot[bigB] = 100u
        pot[utg] = 100u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1133u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1134u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1133u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Alex, Poly and Jane win and share a pot of 800, 266 each")
    fun showdownStory010() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("Ad","9h")) }
            smallB with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("As","9d")) }
            bigB with { anInGamePlayer(it, stack = 1000u, status = CALL, cards = frenchCardsSet("Ah","9c")) }
            utg with { anInGamePlayer(it, stack = 1000u, status = FOLD, cards = frenchCardsSet("Qh","Qc")) }
        }
        val pot = buildMutablePot()
        pot[button] = 200u
        pot[smallB] = 200u
        pot[bigB] = 200u
        pot[utg] = 200u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board,table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(1266u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(1268u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(1266u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(1000u)
    }

    @Test
    @DisplayName("""execute(): share winning pot has a spare chip -> 
        |spare chip is assigned to the first winner at button left""")
    fun execute_shareWinningPotHasASpareChip_spareChipIsAssignedToTheFirstWinnerAtButtonLeft() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Ad","9h")) }
            smallB with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("As","9d")) }
            bigB with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Ah","9c")) }
            utg with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Qh","Qc")) }
        }
        val pot = buildMutablePot()
        pot[button] = 10u
        pot[smallB] = 10u
        pot[bigB] = 10u
        pot[utg] = 1u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot, board, table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(10u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(11u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(10u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("""execute(): share winning pot has two spare chips -> 
        |spare chip is assigned to the first winner at button left""")
    fun execute_shareWinningPotHasTwoSpareChips_spareChipIsAssignedToTheFirstWinnerAtButtonLeft() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Ad","9h")) }
            smallB with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("As","9d")) }
            bigB with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Ah","9c")) }
            utg with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Qh","Qc")) }
        }
        val pot = buildMutablePot()
        pot[button] = 10u
        pot[smallB] = 10u
        pot[bigB] = 10u
        pot[utg] = 2u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot, board, table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(10u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(12u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(10u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(0u)
    }

    @Test
    @DisplayName("""execute(): first player at button left is not a winner -> 
        |spare chips are assigned to the first winner at button left""")
    fun execute_firstPlayerAtButtonLeftIsNotAWinner_spareChipsAreAssignedToTheFirstWinnerAtButtonLeft() {
        val table = aTenInGamePlayerTable {
            button with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Ad","9h")) }
            smallB with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Qh","Qc")) }
            bigB with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("Ah","9c")) }
            utg with { anInGamePlayer(it, stack = 0u, status = NONE, cards = frenchCardsSet("As","9d")) }
        }
        val pot = buildMutablePot()
        pot[button] = 10u
        pot[utg] = 10u
        pot[bigB] = 10u
        pot[smallB] = 2u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot, board, table)

        assertThat(table.getPlayerFrom(BUTTON).stack).isEqualTo(10u)
        assertThat(table.getPlayerFrom(UNDER_THE_GUN).stack).isEqualTo(10u)
        assertThat(table.getPlayerFrom(BIG_BLIND).stack).isEqualTo(12u)
        assertThat(table.getPlayerFrom(SMALL_BLIND).stack).isEqualTo(0u)
    }

}
