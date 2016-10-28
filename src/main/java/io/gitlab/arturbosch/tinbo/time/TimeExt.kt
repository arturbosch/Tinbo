package io.gitlab.arturbosch.tinbo.time

import java.time.LocalDate

/**
 * @author Artur Bosch
 */

fun weekRange(today: LocalDate): Pair<LocalDate, LocalDate> {
	val dayOfWeek = today.dayOfWeek
	val from = dayOfWeek.value - 1
	val to = 7 - dayOfWeek.value
	return today.minusDays(from.toLong()) to today.plusDays(to.toLong())
}

fun TimeEntry.timeAsMinutes(): Int = (hours * 60 + minutes).toInt()