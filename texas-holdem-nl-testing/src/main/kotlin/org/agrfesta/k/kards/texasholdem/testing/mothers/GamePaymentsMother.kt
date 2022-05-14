package org.agrfesta.k.kards.texasholdem.testing.mothers

import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePayments
import org.agrfesta.k.kards.texasholdem.rules.gameplay.GamePaymentsFixedImpl
import org.agrfesta.k.kards.texasholdem.tournaments.IncreasingGamePayments
import org.agrfesta.k.kards.texasholdem.tournaments.IncreasingGamePaymentsDefinition
import org.agrfesta.k.kards.texasholdem.tournaments.LevelPayments

val aGamePayments: GamePayments = aGamePayments(10u, 20u)
val anIncreasingGamePaymentsDef = IncreasingGamePaymentsDefinition(listOf(LevelPayments(1u, 2u)), 1u)
val anIncreasingGamePayments = IncreasingGamePayments(anIncreasingGamePaymentsDef)

fun blinds(sb: UInt, bb: UInt): GamePayments = GamePaymentsFixedImpl(sb, bb)
fun aGamePayments(sb: UInt = 10u, bb: UInt = 20u, ante: UInt? = null): GamePayments =
    GamePaymentsFixedImpl(sb, bb, ante)