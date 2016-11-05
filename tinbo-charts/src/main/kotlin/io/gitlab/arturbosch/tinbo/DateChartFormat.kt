package io.gitlab.arturbosch.tinbo

import java.time.LocalDate

/**
 * Used to label date values in axis of charts in a space efficient way.
 *
 * @author Artur Bosch
 */
class DateChartFormat(val date: LocalDate) : Comparable<DateChartFormat> {
	override fun compareTo(other: DateChartFormat): Int {
		return date.compareTo(other.date)
	}

	override fun toString(): String {
		return "${date.dayOfMonth}.${date.monthValue}"
	}

	override fun equals(other: Any?): Boolean {
		return if (other is DateChartFormat) date == other.date else false
	}

	override fun hashCode(): Int {
		return date.hashCode()
	}
}

/**
 * Converts a local date to a date format.
 */
fun LocalDate.toDateFormat(): Comparable<DateChartFormat> = DateChartFormat(this)