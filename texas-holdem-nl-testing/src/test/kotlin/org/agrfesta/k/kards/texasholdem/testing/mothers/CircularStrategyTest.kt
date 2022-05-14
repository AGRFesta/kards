package org.agrfesta.k.kards.texasholdem.testing.mothers

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus
import org.agrfesta.k.kards.texasholdem.testing.mothers.CircularStrategy.Companion.aStrategy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CircularStrategyTest {

    @Test
    @DisplayName("aStrategy(): an empty action list -> returns a strategy that returns always the same generic action")
    fun aTest() {
        val context = aGameContext()
        val ownPlayerA = anOwnPlayer(status = PlayerStatus.ALL_IN)

        val strategy = aStrategy()

        assertThat(strategy.invoke(ownPlayerA, context)).isEqualTo(anAction)
    }

}