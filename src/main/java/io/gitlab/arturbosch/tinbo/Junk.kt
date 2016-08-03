package io.gitlab.arturbosch.tinbo

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.ArrayList

/**
 * @author artur
 */

fun Long.toTimeString(): String {
	return toString().apply {
		if (length == 1) {
			return "0$this"
		}
	}
}

fun String.spaceIfEmpty(): String =
		when (this) {
			"" -> " "
			else -> this
		}


fun String.orThrow(): String = if (this.isEmpty())
	throw IllegalArgumentException("Empty value not allowed!") else this

inline fun String.toLongOrDefault(long: () -> Long): Long {
	return try {
		this.toLong()
	} catch (e: NumberFormatException) {
		long.invoke()
	}
}

inline fun String.toIntOrDefault(int: () -> Int): Int {
	return try {
		this.toInt()
	} catch (e: NumberFormatException) {
		int.invoke()
	}
}

fun <E> List<E>.plusElementAtBeginning(element: E): List<E> {
	val result = ArrayList<E>(size + 1)
	result.add(element)
	result.addAll(this)
	return result
}

fun List<String>.withIndexedColumn(): List<String> = this.withIndex().map { "${it.index + 1};${it.value}" }

fun <E> List<E>.applyToString(): List<String> = this.map { it.toString() }

fun <E> List<E>.replaceAt(index: Int, element: E): List<E> {
	val list = this.toMutableList()
	list[index] = element
	return list.toList()
}

fun LocalDateTime?.orValue(dateTime: LocalDateTime): LocalDateTime = if (this == null) dateTime else this
fun LocalDate?.orValue(dateTime: LocalDate): LocalDate = if (this == null) dateTime else this

fun String.orValue(value: String): String = if (this.isEmpty()) value else this
fun String?.orDefault(value: String): String = if (this.isNullOrEmpty()) value else this!!
fun Long.orValue(hours: Long): Long = if (this.equals(-1L)) hours else this

fun String.replaceSeparator(): String {
	return this.replace(";", ".,")
}

fun String.orDefaultMonth(): Int = this.toIntOrDefault { LocalDate.now().month.value }
fun String?.toLongOrNull(): Long? = if (this.isNullOrEmpty()) null else this?.toLong()
fun String?.nullIfEmpty(): String? = if (this.isNullOrEmpty()) null else this