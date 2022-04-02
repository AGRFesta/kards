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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Showdown tests")
class ShowdownTest {
    private val showdown = ShowdownImpl(CardsEvaluatorBaseImpl())

    @Test
    @DisplayName("Showdown story: Alex wins all the pot")
    fun showdownStory000() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","7c"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1600u)
        assertThat(poly.stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Jane wins, Alex wins the remaining pot")
    fun showdownStory001() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","7c"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL,  cards = frenchCardsSet("9d","9c"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        pot[jane] = 100u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1400u)
        assertThat(poly.stack).isEqualTo(1000u)
        assertThat(jane.stack).isEqualTo(1300u)
    }
    @Test
    @DisplayName("Showdown story: Jane wins, Dave wins the second pot, Alex wins the remaining pot")
    fun showdownStory002() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = RAISE, cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","7c"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL,  cards = frenchCardsSet("9d","9c"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jc","9h"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        pot[jane] = 100u
        pot[dave] = 200u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1200u)
        assertThat(poly.stack).isEqualTo(1000u)
        assertThat(jane.stack).isEqualTo(1400u)
        assertThat(dave.stack).isEqualTo(1300u)
    }
    @Test
    @DisplayName("Showdown story: Alex and Poly win")
    fun showdownStory003() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = CALL,  cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL,  cards = frenchCardsSet("As","Td"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL,  cards = frenchCardsSet("9d","Tc"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jc","7h"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        pot[jane] = 100u
        pot[dave] = 200u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1450u)
        assertThat(poly.stack).isEqualTo(1450u)
        assertThat(jane.stack).isEqualTo(1000u)
        assertThat(dave.stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Jane and Dave win, Alex and Poly share the rest")
    fun showdownStory004() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = CALL,  cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL,  cards = frenchCardsSet("As","Td"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","9c"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jh","9d"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        pot[jane] = 100u
        pot[dave] = 200u
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1100u)
        assertThat(poly.stack).isEqualTo(1100u)
        assertThat(jane.stack).isEqualTo(1200u)
        assertThat(dave.stack).isEqualTo(1500u)
    }
    @Test
    @DisplayName("Showdown story: All win")
    fun showdownStory005() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = CALL,  cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL,  cards = frenchCardsSet("As","Td"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jd","9c"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = CALL,  cards = frenchCardsSet("Jh","9d"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        pot[jane] = 100u
        pot[dave] = 200u
        val board = board("Ac","Jc","Qc", "Kc", "Tc")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1300u)
        assertThat(poly.stack).isEqualTo(1300u)
        assertThat(jane.stack).isEqualTo(1100u)
        assertThat(dave.stack).isEqualTo(1200u)
    }
    @Test
    @DisplayName("Showdown story: Jane wins but folded, Dave wins, Alex and Poly share the rest")
    fun showdownStory006() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = RAISE,  cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = ALL_IN, cards = frenchCardsSet("As","Td"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = FOLD,   cards = frenchCardsSet("Jd","9c"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = CALL,   cards = frenchCardsSet("Jh","9d"))
        val pot = buildMutablePot()
        pot[alex] = 300u
        pot[poly] = 300u
        pot[jane] = 100u
        pot[dave] = 200u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1100u)
        assertThat(poly.stack).isEqualTo(1100u)
        assertThat(jane.stack).isEqualTo(1000u)
        assertThat(dave.stack).isEqualTo(1700u)
    }
    @Test
    @DisplayName("Showdown story: Poly wins, Jane wins second but folded, Alex take the rest")
    fun showdownStory007() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = RAISE,  cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = ALL_IN, cards = frenchCardsSet("As","9d"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = FOLD,   cards = frenchCardsSet("Ah","Qc"))
        val pot = buildMutablePot()
        pot[alex] = 500u
        pot[poly] = 50u
        pot[jane] = 120u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1520u)
        assertThat(poly.stack).isEqualTo(1150u)
        assertThat(jane.stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Poly wins, Jane wins second, Alex take the rest")
    fun showdownStory008() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = NONE,   cards = frenchCardsSet("Ad","Ts"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = ALL_IN, cards = frenchCardsSet("As","9d"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = ALL_IN, cards = frenchCardsSet("Ah","Qc"))
        val pot = buildMutablePot()
        pot[alex] = 200u
        pot[poly] = 50u
        pot[jane] = 120u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1080u)
        assertThat(poly.stack).isEqualTo(1150u)
        assertThat(jane.stack).isEqualTo(1140u)
    }
    @Test
    @DisplayName("Showdown story: Alex, Poly and Jane win and share a pot of 400, 133 each")
    fun showdownStory009() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = CALL, cards = frenchCardsSet("Ad","9h"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL, cards = frenchCardsSet("As","9d"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL, cards = frenchCardsSet("Ah","9c"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = FOLD, cards = frenchCardsSet("Qh","Qc"))
        val pot = buildMutablePot()
        pot[alex] = 100u
        pot[poly] = 100u
        pot[jane] = 100u
        pot[dave] = 100u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1133u)
        assertThat(poly.stack).isEqualTo(1133u)
        assertThat(jane.stack).isEqualTo(1133u)
        assertThat(dave.stack).isEqualTo(1000u)
    }
    @Test
    @DisplayName("Showdown story: Alex, Poly and Jane win and share a pot of 800, 266 each")
    fun showdownStory010() {
        val alex = anInGamePlayer(name = "Alex", stack = 1000u, status = CALL, cards = frenchCardsSet("Ad","9h"))
        val poly = anInGamePlayer(name = "Poly", stack = 1000u, status = CALL, cards = frenchCardsSet("As","9d"))
        val jane = anInGamePlayer(name = "Jane", stack = 1000u, status = CALL, cards = frenchCardsSet("Ah","9c"))
        val dave = anInGamePlayer(name = "Dave", stack = 1000u, status = FOLD, cards = frenchCardsSet("Qh","Qc"))
        val pot = buildMutablePot()
        pot[alex] = 200u
        pot[poly] = 200u
        pot[jane] = 200u
        pot[dave] = 200u
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1266u)
        assertThat(poly.stack).isEqualTo(1266u)
        assertThat(jane.stack).isEqualTo(1266u)
        assertThat(dave.stack).isEqualTo(1000u)
    }

}
