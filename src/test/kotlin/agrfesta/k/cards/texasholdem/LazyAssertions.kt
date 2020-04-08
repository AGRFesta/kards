package agrfesta.k.cards.texasholdem

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.DynamicTest

interface LazyAssertion {
    fun assert()
}

fun createDynamicTest(assertion: LazyAssertion): DynamicTest =
        DynamicTest.dynamicTest(assertion.toString(), assertion::assert)

class LazyAssertionEqual<T>(private val actual: T, private val other: T): LazyAssertion {
    override fun assert() = assertThat(actual).isEqualTo(other)
    override fun toString() = "$actual == $other"
}
class LazyAssertionNotEqual<T>(private val actual: T, private val other: T): LazyAssertion {
    override fun assert() = assertThat(actual).isNotEqualTo(other)
    override fun toString() = "$actual != $other"
}
class LazyAssertionGreaterThan<A: Comparable<B>, B>(private val actual: A, private val other: B): LazyAssertion {
    override fun assert() = assertThat(actual).isGreaterThan(other)
    override fun toString() = "$actual > $other"
}
class LazyAssertionLessThan<A: Comparable<B>, B>(private val actual: A, private val other: B): LazyAssertion {
    override fun assert() = assertThat(actual).isLessThan(other)
    override fun toString() = "$actual < $other"
}

class LazyAssertionBuilder<T>(val actual: T)
fun <A: Comparable<B>, B> LazyAssertionBuilder<A>.isGreaterThan(other: B): LazyAssertion = LazyAssertionGreaterThan(actual, other)
fun <A: Comparable<B>, B> LazyAssertionBuilder<A>.isLessThan(other: B): LazyAssertion = LazyAssertionLessThan(actual, other)
fun <T> LazyAssertionBuilder<T>.isEqualTo(other: T): LazyAssertion = LazyAssertionEqual(actual, other)
fun <T> LazyAssertionBuilder<T>.isNotEqualTo(other: T): LazyAssertion = LazyAssertionNotEqual(actual, other)
fun <T> willAssertThat(actual: T): LazyAssertionBuilder<T> = LazyAssertionBuilder(actual)

//// LazyFunctionAssertion /////////////////////////////////////////////////////////////////////////////////////////////
interface LazyFunctionAssertion<I,O> {
    fun assert(func: (input: I) -> O)
}
class LazyFunctionExecutionAssertion<I,O>(private val input: I, private val output: O): LazyFunctionAssertion<I,O> {
    override fun assert(func: (input: I) -> O) = assertThat(func(input)).isEqualTo(output)
    override fun toString() = "$input -> $output"
}
fun <I,O> LazyAssertionBuilder<I>.result(output: O) = LazyFunctionExecutionAssertion(actual, output)
fun <I,O> createDynamicTest(assertion: LazyFunctionAssertion<I,O>, func: (input: I) -> O): DynamicTest =
        DynamicTest.dynamicTest(assertion.toString()) { assertion.assert(func) }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

