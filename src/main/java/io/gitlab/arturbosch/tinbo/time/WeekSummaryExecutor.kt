package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.model.CSVAwareExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author Artur Bosch
 */
@Component
open class WeekSummaryExecutor @Autowired constructor(val timeDataHolder: TimeDataHolder) :
		CSVAwareExecutor() {

	override val TABLE_HEADER: String
		get() = "No.;Category;Time"

	fun asTable(summaries: List<String>): String = tableAsString(summaries, TABLE_HEADER)

	fun sumAllCategories(): String {
		val summaries = timeDataHolder.createSummaries()
		return asTable(summaries)
	}

	fun sumForCategories(filters: List<String>): String {
		val summaries = timeDataHolder.createFilteredSummaries(filters)
		return asTable(summaries)
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
		return asTable(summary) + "\n\nTotal time spent: $sum - ($from - $to)"
	}

}