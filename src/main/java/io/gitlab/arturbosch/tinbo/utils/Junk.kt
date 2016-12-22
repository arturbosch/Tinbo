package io.gitlab.arturbosch.tinbo.utils

import java.util.HashSet

/**
 * @author Artur Bosch
 */

fun parseIndices(indexPattern: String): Set<Int> {
	val result = HashSet<Int>()
	val indices = indexPattern.split(" ")
	val regex = Regex("[1-9][0-9]*")
	val regex2 = Regex("[1-9]+-[0-9]+")
	for (index in indices) {
		if (regex.matches(index)) {
			result.add(index.toInt() - 1)
		} else if (regex2.matches(index)) {
			val interval = index.split("-")
			if (interval.size == 2) {
				val (i1, i2) = Pair(interval[0].toInt(), interval[1].toInt())
				IntRange(i1 - 1, i2 - 1)
						.forEach { result.add(it) }
			} else {
				throw IllegalArgumentException()
			}
		} else {
			throw IllegalArgumentException()
		}
	}
	return result
}