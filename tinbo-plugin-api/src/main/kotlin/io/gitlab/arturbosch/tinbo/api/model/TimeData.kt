package io.gitlab.arturbosch.tinbo.api.model

import java.time.LocalDate

/**
 * @author Artur Bosch
 */
data class WeekSummary(private val range: Pair<LocalDate, LocalDate>, val entries: List<WeekEntry>) {

	fun asDateRangeString(): String = "${range.first} - ${range.second}"

	fun totalMinutes(): Int {
		return entries.map { it.minutes }.sum()
	}

	fun totalHourMinutes(): Pair<Int, Int> {
		val totalMinutes = totalMinutes()
		return totalMinutes / 60 to totalMinutes % 60
	}
}

data class WeekEntry(val category: String, val minutes: Int) {
	fun asHourMinutes(): Pair<Int, Int> {
		return minutes / 60 to minutes % 60
	}
}
