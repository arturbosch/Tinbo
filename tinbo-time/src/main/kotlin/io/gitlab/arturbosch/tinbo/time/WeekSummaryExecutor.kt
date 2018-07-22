package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.model.CSVAwareExecutor
import io.gitlab.arturbosch.tinbo.api.toTimeString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
open class WeekSummaryExecutor @Autowired constructor(private val timeDataHolder: TimeDataHolder) :
		CSVAwareExecutor() {

	override val tableHeader: String
		get() = "No.;Category;Time"

	fun asTable(summaries: List<String>): String = tableAsString(summaries, tableHeader)

	fun sumAllCategories(categoryFilters: Set<String>): String {
		val entries = timeDataHolder.getEntries()
				.filter { it.category !in categoryFilters }
		val (summaries, total) = summariesInternal(entries)
		return asTable(summaries) + "\n\n Total time: $total"
	}

	fun sumForCategories(filters: Set<String>, categoryFilters: Set<String>): String {
		val entries = timeDataHolder.getEntries()
				.filter { filters.contains(it.category.toLowerCase()) }
				.filter { it.category.toLowerCase() !in categoryFilters }
		val (summaries, total) = summariesInternal(entries)
		return asTable(summaries) + "\n\n Total time: $total"
	}

	private fun summariesInternal(entries: List<TimeEntry>): Pair<List<String>, String> {
		val categoryToTimeString = entries
				.groupBy { it.category }
				.mapValues { sumTimeSpendForEntries(it.value) }
		val totalTime = categoryToTimeString.values
				.map { TimeEntry(hours = it.first, minutes = it.second, seconds = it.third) }
				.let { sumTimeSpendForEntries(it) }
				.asTimeString()
		return categoryToTimeString.map { it.key + ";" + it.value.asTimeString() } to totalTime
	}

	private fun sumTimeSpendForEntries(value: List<TimeEntry>): Triple<Long, Long, Long> {
		val seconds = value.map { it.seconds }.sum()
		val (extraMinutes, realSeconds) = divAndMod(seconds)
		val minutes = value.map { it.minutes }.sum().plus(extraMinutes)
		val (extraHours, realMinutes) = divAndMod(minutes)
		val hours = value.map { it.hours }.sum().plus(extraHours)

		return Triple(hours, realMinutes, realSeconds)
	}

	private fun Triple<Long, Long, Long>.asTimeString(): String {
		return "${first.toTimeString()}:${second.toTimeString()}:${third.toTimeString()}"
	}

	private fun divAndMod(value: Long) = value.div(60) to value.rem(60)

}
