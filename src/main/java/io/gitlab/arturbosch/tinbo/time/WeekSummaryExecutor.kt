package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.model.AbstractExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author Artur Bosch
 */
@Component
open class WeekSummaryExecutor @Autowired constructor(val timeDataHolder: TimeDataHolder) :
		AbstractExecutor<TimeEntry, TimeData, DummyTime>(timeDataHolder) {

	override val TABLE_HEADER: String
		get() = "No.;Category;Spent"

	override fun newEntry(index: Int, dummy: DummyTime): TimeEntry {
		throw UnsupportedOperationException("not implemented")
	}

	fun twoWeekSummary(): String {
		val data = timeDataHolder.getEntries()
		val today = LocalDate.now()
		return printWeekSummary(data, today.minusDays(7)) + "\n\n" + printWeekSummary(data, today)
	}

	private fun printWeekSummary(data: List<TimeEntry>, today: LocalDate): String {
		val (from, to) = weekRange(today)
		val entriesOfWeek = data.filter { it.date >= from && it.date <= to }
		val summary = timeDataHolder.summariesInternal(entriesOfWeek)
		val sum = timeDataHolder.sumTimesAsString(entriesOfWeek)
		return tableAsString(summary, TABLE_HEADER) + "\n\nTotal time spent: $sum - ($from - $to)"
	}

	private fun weekRange(today: LocalDate): Pair<LocalDate, LocalDate> {
		val dayOfWeek = today.dayOfWeek
		val from = dayOfWeek.value - 1
		val to = 7 - dayOfWeek.value
		return today.minusDays(from.toLong()) to today.plusDays(to.toLong())
	}
}