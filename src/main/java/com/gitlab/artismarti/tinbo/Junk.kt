package com.gitlab.artismarti.tinbo

import com.gitlab.artismarti.tinbo.persistence.Entry
import java.time.LocalDateTime
import java.util.ArrayList

/**
 * @author artur
 */

fun String.spaceIfEmpty(): String =
        when (this) {
            "" -> " "
            else -> this
        }

fun <E> List<E>.plusElementAtBeginning(element: E): List<E> {
    val result = ArrayList<E>(size + 1)
    result.add(element)
    result.addAll(this)
    return result
}

fun List<String>.withIndexedColumn(): List<String> = this.withIndex().map { "${it.index + 1};${it.value}" }

fun <E> List<E>.applyToString(): List<String> = this.map { it.toString() }

fun List<Entry>.toSortedWithIndexedColumnStrings(): List<String> = this.sorted().applyToString().withIndexedColumn()

fun <E> List<E>.replaceAt(index: Int, element: E): List<E> {
    val list = this.toMutableList()
    list[index] = element
    return list.toList()
}

fun LocalDateTime?.orValue(dateTime: LocalDateTime): LocalDateTime = if (this == null) dateTime else this

fun String.orValue(value: String): String = if (this.isEmpty()) value else this

