package org.agrfesta.k.kards.texasholdem.testing.mothers

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.PRE_FLOP
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.RIVER
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePhase.TURN
import org.agrfesta.k.kards.texasholdem.rules.gameplay.amount
import org.agrfesta.k.kards.texasholdem.testing.mothers.PotPhasesBuilder.Companion.phasePots
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.utg
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PotPhasesBuilderTest {

    @Test
    @DisplayName("atPreFlop(): adding a generic pot amount of 300 to the pre-flop -> pre-flop pot has value 300")
    fun atPreFlop_addingAGenericPotAmountOf300ToThePreFlop_preFlopPotHasValue300() {
        val pot = phasePots {
            atPreFlop(300u)
        }

        assertThat(pot[PRE_FLOP]?.amount()).isEqualTo(300u)
    }
    @Test
    @DisplayName("""atPreFlop(): adding a generic pot amount of 300 and than 400 to the pre-flop -> 
        |pre-flop pot has value 400""")
    fun atPreFlop_addingAGenericPotAmountOf300AndThan400ToThePreFlop_preFlopPotHasValue400() {
        val pot = phasePots {
            atPreFlop(300u)
            atPreFlop(400u)
        }

        assertThat(pot[PRE_FLOP]?.amount()).isEqualTo(400u)
    }

    @Test
    @DisplayName("atPreFlop(): adding a payment of 300 from utg to the pre-flop -> at pre-flop utg payed 300")
    fun atPreFlop_addingAPaymentOf300FromUtgToThePreFlop_atPreFlopUtgPayed300() {
        val pot = phasePots {
            atPreFlop(utg to 300u)
        }

        assertThat(pot[PRE_FLOP]?.getValue(utg)).isEqualTo(300u)
    }
    @Test
    @DisplayName("""atPreFlop(): adding a payment of 300 from utg, and than 400, to the pre-flop -> 
        |at pre-flop utg payed 400""")
    fun atPreFlop_addingAPaymentOf300FromUtgAndThan400ToThePreFlop_atPreFlopUtgPayed400() {
        val pot = phasePots {
            atPreFlop(utg to 300u)
            atPreFlop(utg to 400u)
        }

        assertThat(pot[PRE_FLOP]?.getValue(utg)).isEqualTo(400u)
    }

    @Test
    @DisplayName("atFlop(): adding a payment of 300 from utg to the flop -> at flop utg payed 300")
    fun atFlop_addingAPaymentOf300FromUtgToTheFlop_atFlopUtgPayed300() {
        val pot = phasePots {
            atFlop(utg to 300u)
        }

        assertThat(pot[FLOP]?.getValue(utg)).isEqualTo(300u)
    }
    @Test
    @DisplayName("""atFlop(): adding a payment of 300 from utg, and than 400, to the flop -> 
        |at flop utg payed 400""")
    fun atFlop_addingAPaymentOf300FromUtgAndThan400ToTheFlop_atFlopUtgPayed400() {
        val pot = phasePots {
            atFlop(utg to 300u)
            atFlop(utg to 400u)
        }

        assertThat(pot[FLOP]?.getValue(utg)).isEqualTo(400u)
    }

    @Test
    @DisplayName("atTurn(): adding a payment of 300 from utg to the turn -> at turn utg payed 300")
    fun atTurn_addingAPaymentOf300FromUtgToTheTurn_atTurnUtgPayed300() {
        val pot = phasePots {
            atTurn(utg to 300u)
        }

        assertThat(pot[TURN]?.getValue(utg)).isEqualTo(300u)
    }
    @Test
    @DisplayName("""atTurn(): adding a payment of 300 from utg, and than 400, to the turn -> 
        |at turn utg payed 400""")
    fun atTurn_addingAPaymentOf300FromUtgAndThan400ToTheTurn_atTurnUtgPayed400() {
        val pot = phasePots {
            atTurn(utg to 300u)
            atTurn(utg to 400u)
        }

        assertThat(pot[TURN]?.getValue(utg)).isEqualTo(400u)
    }

    @Test
    @DisplayName("atRiver(): adding a payment of 300 from utg to the river -> at river utg payed 300")
    fun atRiver_addingAPaymentOf300FromUtgToTheTurn_atTurnUtgPayed300() {
        val pot = phasePots {
            atRiver(utg to 300u)
        }

        assertThat(pot[RIVER]?.getValue(utg)).isEqualTo(300u)
    }
    @Test
    @DisplayName("""atRiver(): adding a payment of 300 from utg, and than 400, to the river -> 
        |at river utg payed 400""")
    fun atRiver_addingAPaymentOf300FromUtgAndThan400ToTheTurn_atTurnUtgPayed400() {
        val pot = phasePots {
            atRiver(utg to 300u)
            atRiver(utg to 400u)
        }

        assertThat(pot[RIVER]?.getValue(utg)).isEqualTo(400u)
    }

}
