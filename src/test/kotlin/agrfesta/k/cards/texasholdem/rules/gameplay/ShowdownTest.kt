package agrfesta.k.cards.texasholdem.rules.gameplay

import agrfesta.k.cards.texasholdem.rules.CardsEvaluatorBaseImpl
import agrfesta.k.cards.playingcards.suits.frenchCardsSet
import agrfesta.k.cards.texasholdem.rules.gameplay.PlayerStatus.*
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Showdown tests")
class ShowdownTest {
    private val showdown = ShowdownImpl(CardsEvaluatorBaseImpl())

    @Test
    @DisplayName("Showdown story: Alex wins all the pot")
    fun showdownStory000() {
        val alex = aPlayer("Alex", 1000, RAISE, frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, CALL,  frenchCardsSet("Jd","7c"))
        val pot = buildPot()
        pot[alex] = 300
        pot[poly] = 300
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1600)
        assertThat(poly.stack).isEqualTo(1000)
    }
    @Test
    @DisplayName("Showdown story: Jane wins, Alex wins the remaining pot")
    fun showdownStory001() {
        val alex = aPlayer("Alex", 1000, RAISE, frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, CALL,  frenchCardsSet("Jd","7c"))
        val jane = aPlayer("Jane", 1000, CALL,  frenchCardsSet("9d","9c"))
        val pot = buildPot()
        pot[alex] = 300
        pot[poly] = 300
        pot[jane] = 100
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1400)
        assertThat(poly.stack).isEqualTo(1000)
        assertThat(jane.stack).isEqualTo(1300)
    }
    @Test
    @DisplayName("Showdown story: Jane wins, Dave wins the second pot, Alex wins the remaining pot")
    fun showdownStory002() {
        val alex = aPlayer("Alex", 1000, RAISE, frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, CALL,  frenchCardsSet("Jd","7c"))
        val jane = aPlayer("Jane", 1000, CALL,  frenchCardsSet("9d","9c"))
        val dave = aPlayer("Dave", 1000, CALL,  frenchCardsSet("Jc","9h"))
        val pot = buildPot()
        pot[alex] = 300
        pot[poly] = 300
        pot[jane] = 100
        pot[dave] = 200
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1200)
        assertThat(poly.stack).isEqualTo(1000)
        assertThat(jane.stack).isEqualTo(1400)
        assertThat(dave.stack).isEqualTo(1300)
    }
    @Test
    @DisplayName("Showdown story: Alex and Poly win")
    fun showdownStory003() {
        val alex = aPlayer("Alex", 1000, CALL,  frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, CALL,  frenchCardsSet("As","Td"))
        val jane = aPlayer("Jane", 1000, CALL,  frenchCardsSet("9d","Tc"))
        val dave = aPlayer("Dave", 1000, CALL,  frenchCardsSet("Jc","7h"))
        val pot = buildPot()
        pot[alex] = 300
        pot[poly] = 300
        pot[jane] = 100
        pot[dave] = 200
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1450)
        assertThat(poly.stack).isEqualTo(1450)
        assertThat(jane.stack).isEqualTo(1000)
        assertThat(dave.stack).isEqualTo(1000)
    }
    @Test
    @DisplayName("Showdown story: Jane and Dave win, Alex and Poly share the rest")
    fun showdownStory004() {
        val alex = aPlayer("Alex", 1000, CALL,  frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, CALL,  frenchCardsSet("As","Td"))
        val jane = aPlayer("Jane", 1000, CALL,  frenchCardsSet("Jd","9c"))
        val dave = aPlayer("Dave", 1000, CALL,  frenchCardsSet("Jh","9d"))
        val pot = buildPot()
        pot[alex] = 300
        pot[poly] = 300
        pot[jane] = 100
        pot[dave] = 200
        val board = board("Ac","Js","9s", "8c", "3d")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1100)
        assertThat(poly.stack).isEqualTo(1100)
        assertThat(jane.stack).isEqualTo(1200)
        assertThat(dave.stack).isEqualTo(1500)
    }
    @Test
    @DisplayName("Showdown story: All win")
    fun showdownStory005() {
        val alex = aPlayer("Alex", 1000, CALL,  frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, CALL,  frenchCardsSet("As","Td"))
        val jane = aPlayer("Jane", 1000, CALL,  frenchCardsSet("Jd","9c"))
        val dave = aPlayer("Dave", 1000, CALL,  frenchCardsSet("Jh","9d"))
        val pot = buildPot()
        pot[alex] = 300
        pot[poly] = 300
        pot[jane] = 100
        pot[dave] = 200
        val board = board("Ac","Jc","Qc", "Kc", "Tc")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1300)
        assertThat(poly.stack).isEqualTo(1300)
        assertThat(jane.stack).isEqualTo(1100)
        assertThat(dave.stack).isEqualTo(1200)
    }
    @Test
    @DisplayName("Showdown story: Jane wins but folded, Dave wins, Alex and Poly share the rest")
    fun showdownStory006() {
        val alex = aPlayer("Alex", 1000, RAISE,  frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, ALL_IN, frenchCardsSet("As","Td"))
        val jane = aPlayer("Jane", 1000, FOLD,   frenchCardsSet("Jd","9c"))
        val dave = aPlayer("Dave", 1000, CALL,   frenchCardsSet("Jh","9d"))
        val pot = buildPot()
        pot[alex] = 300
        pot[poly] = 300
        pot[jane] = 100
        pot[dave] = 200
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1100)
        assertThat(poly.stack).isEqualTo(1100)
        assertThat(jane.stack).isEqualTo(1000)
        assertThat(dave.stack).isEqualTo(1700)
    }
    @Test
    @DisplayName("Showdown story: Poly wins, Jane wins second but folded, Alex take the rest")
    fun showdownStory007() {
        val alex = aPlayer("Alex", 1000, RAISE,  frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, ALL_IN, frenchCardsSet("As","9d"))
        val jane = aPlayer("Jane", 1000, FOLD,   frenchCardsSet("Ah","Qc"))
        val pot = buildPot()
        pot[alex] = 500
        pot[poly] = 50
        pot[jane] = 120
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1520)
        assertThat(poly.stack).isEqualTo(1150)
        assertThat(jane.stack).isEqualTo(1000)
    }
    @Test
    @DisplayName("Showdown story: Poly wins, Jane wins second, Alex take the rest")
    fun showdownStory008() {
        val alex = aPlayer("Alex", 1000, NONE,   frenchCardsSet("Ad","Ts"))
        val poly = aPlayer("Poly", 1000, ALL_IN, frenchCardsSet("As","9d"))
        val jane = aPlayer("Jane", 1000, ALL_IN, frenchCardsSet("Ah","Qc"))
        val pot = buildPot()
        pot[alex] = 200
        pot[poly] = 50
        pot[jane] = 120
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1080)
        assertThat(poly.stack).isEqualTo(1150)
        assertThat(jane.stack).isEqualTo(1140)
    }
    @Test
    @DisplayName("Showdown story: Alex, Poly and Jane win and share a pot of 400, 133 each")
    fun showdownStory009() {
        val alex = aPlayer("Alex", 1000, CALL, frenchCardsSet("Ad","9h"))
        val poly = aPlayer("Poly", 1000, CALL, frenchCardsSet("As","9d"))
        val jane = aPlayer("Jane", 1000, CALL, frenchCardsSet("Ah","9c"))
        val dave = aPlayer("Dave", 1000, FOLD, frenchCardsSet("Qh","Qc"))
        val pot = buildPot()
        pot[alex] = 100
        pot[poly] = 100
        pot[jane] = 100
        pot[dave] = 100
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1133)
        assertThat(poly.stack).isEqualTo(1133)
        assertThat(jane.stack).isEqualTo(1133)
        assertThat(dave.stack).isEqualTo(1000)
    }
    @Test
    @DisplayName("Showdown story: Alex, Poly and Jane win and share a pot of 800, 266 each")
    fun showdownStory010() {
        val alex = aPlayer("Alex", 1000, CALL, frenchCardsSet("Ad","9h"))
        val poly = aPlayer("Poly", 1000, CALL, frenchCardsSet("As","9d"))
        val jane = aPlayer("Jane", 1000, CALL, frenchCardsSet("Ah","9c"))
        val dave = aPlayer("Dave", 1000, FOLD, frenchCardsSet("Qh","Qc"))
        val pot = buildPot()
        pot[alex] = 200
        pot[poly] = 200
        pot[jane] = 200
        pot[dave] = 200
        val board = board("Ac","Js","9s", "8c", "3h")

        showdown.execute(pot,board)

        assertThat(alex.stack).isEqualTo(1266)
        assertThat(poly.stack).isEqualTo(1266)
        assertThat(jane.stack).isEqualTo(1266)
        assertThat(dave.stack).isEqualTo(1000)
    }

}