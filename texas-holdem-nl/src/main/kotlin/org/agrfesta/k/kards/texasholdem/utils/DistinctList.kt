package org.agrfesta.k.kards.texasholdem.utils

import org.agrfesta.k.kards.texasholdem.utils.DistinctList.Companion.distinctListOf

class DistinctList<T> private constructor(private val list: List<T>): List<T> by list {
    companion object {
        fun <T> distinctListOf(vararg entries: T) = DistinctList(entries.distinct())
        fun <T> distinctListOf(list: List<T>) = DistinctList(list.distinct())
        fun <T> distinctListOf(set: Set<T>) = DistinctList(set.toList())
    }
    fun <R> map(transform: (T) -> R): DistinctList<R> = distinctListOf(list.map(transform))
    fun filter(predicate: (T) -> Boolean): DistinctList<T> = DistinctList(list.filter(predicate))
}

fun <T> Set<T>.toDistinctList(): DistinctList<T> = distinctListOf(this)

