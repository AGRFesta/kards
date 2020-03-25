package agrfesta.k.cards.texasholdem.rules.hands

import agrfesta.k.cards.texasholdem.rules.CardsEvaluation
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan

class HECompareAssertionData(
        private val evaluationA: CardsEvaluation,
        private val evaluationB: CardsEvaluation,
        private val expected: Int ) {

    fun assertComparison() {
        when {
            expected<0 -> assertThat(evaluationA).isLessThan(evaluationB)
            expected>0 -> assertThat(evaluationA).isGreaterThan(evaluationB)
            else -> assertThat(evaluationA).isEqualTo(evaluationB)
        }
    }

    fun getTitle(): String {
        return "$evaluationA ${relation()} $evaluationB"
    }
    private fun relation(): String {
        if (expected < 0) return "<"
        return if (expected > 0) ">" else "="
    }
}