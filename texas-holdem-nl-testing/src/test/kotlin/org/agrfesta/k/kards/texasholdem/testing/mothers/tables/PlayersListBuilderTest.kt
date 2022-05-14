package org.agrfesta.k.kards.texasholdem.testing.mothers.tables

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasClass
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNotEqualTo
import org.agrfesta.k.kards.texasholdem.rules.gameplay.InGamePlayer
import org.agrfesta.k.kards.texasholdem.rules.gameplay.PlayerStatus.CALL
import org.agrfesta.k.kards.texasholdem.testing.mothers.anInGamePlayer
import org.agrfesta.k.kards.texasholdem.testing.mothers.tables.PlayersListBuilder.Companion.replacing
import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PlayersListBuilderTest {
    private val hero = anInGamePlayer()
    private val hero2 = anInGamePlayer()

    @Test
    @DisplayName("sittingAt(): replacing missing placeholder -> throws an exception")
    fun sittingAt_replacingMissingPlaceholder_throwsAnException() {
        val list = distinctListOf(button, smallB, bigB)

        assertThat {
                list.replacing {
                    hero sittingAt utg
                }
        }.isFailure().all {
            hasClass(IllegalArgumentException::class)
            hasMessage("Unable to find '${utg.name}' placeholder in initial list: ${list.map { it.name }.toList()}")
        }
    }

    @Test
    @DisplayName("sittingAt(): replacing button with hero -> resulting list has hero instead of button")
    fun sittingAt_replacingButtonWithHero_resultingListHasHeroInsteadOfButton() {
        val list = distinctListOf(button, smallB, bigB)

        val result = list.replacing {
            hero sittingAt button
        }

        assertThat(result).containsExactly(hero, smallB, bigB)
    }

    @Test
    @DisplayName("""sittingAt(): replacing button with hero and after with hero2 -> 
        |resulting list has hero2 instead of button, hero is missing""")
    fun sittingAt_replacingButtonWithHeroAndAfterWithHero2_resultingListHasHero2InsteadOfButton() {
        val list = distinctListOf(button, smallB, bigB)

        val result = list.replacing {
            hero sittingAt button
            hero2 sittingAt button
        }

        assertThat(result).containsExactly(hero2, smallB, bigB)
    }

    @Test
    @DisplayName("with(): modifying missing placeholder -> throws an exception")
    fun with_modifyingMissingPlaceholder_throwsAnException() {
        val list = distinctListOf(button, smallB, bigB)

        assertThat {
            list.replacing {
                utg with { hero }
            }
        }.isFailure().all {
            hasClass(IllegalArgumentException::class)
            hasMessage("Unable to find '${utg.name}' placeholder in initial list: ${list.map { it.name }.toList()}")
        }
    }

    @Test
    @DisplayName("with(): modifying big blind stack -> returns same list where big blind has new stack")
    fun with_modifyingBigBlindStack_returnsSameListWhereBigBlindHasNewStack() {
        val list = distinctListOf(button, smallB, bigB)

        val result = list.replacing {
            bigB with { anInGamePlayer(it, stack = 2345u) }
        }

        assertThat(result).containsExactly(button, smallB, bigB)
        val bigBStack = (result[2] as InGamePlayer).stack
        assertThat(bigBStack).isEqualTo(2345u)
    }

    @Test
    @DisplayName("""with(): modifying big blind stack twice -> 
        |returns same list where big blind has only last modifications""")
    fun with_modifyingBigBlindStackTwice_returnsSameListWhereBigBlindHasOnlyLastModifications() {
        val list = distinctListOf(button, smallB, bigB)

        val result = list.replacing {
            bigB with { anInGamePlayer(it, stack = 2345u) }
            bigB with { anInGamePlayer(it, status = CALL) }
        }

        assertThat(result).containsExactly(button, smallB, bigB)
        val bigStack = (result[2] as InGamePlayer)
        assertThat(bigStack.status).isEqualTo(CALL)
        assertThat(bigStack.stack).isNotEqualTo(2345u)
    }

}
