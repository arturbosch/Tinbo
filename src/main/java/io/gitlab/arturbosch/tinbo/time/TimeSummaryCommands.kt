package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.Summarizable
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author artur
 */
@Component
open class TimeSummaryCommands @Autowired constructor(val executor: TimeExecutor) : Command, Summarizable {

	override val id: String = "time"

	override fun sum(categories: List<String>): String {
		if (categories.isEmpty()) {
			return executor.sumAllCategories()
		} else {
			return executor.sumForCategories(categories)
		}
	}

	@CliCommand("week", help = "Hi")
	fun weekSummary() {
		val data = executor.timeDataHolder.getEntries()
		val today = today()
		printWeekSummary(data, today.minusDays(7))
		printlnInfo("\n")
		printWeekSummary(data, today)
	}

	private fun printWeekSummary(data: List<TimeEntry>, today: LocalDate) {
		val (from, to) = weekRange(today)
		val entriesOfWeek = data.filter { it.date >= from && it.date <= to }
		val summary = executor.timeDataHolder.summariesInternal(entriesOfWeek)
		val sum = executor.timeDataHolder.sumTimesAsString(entriesOfWeek)
		printlnInfo(executor.tableAsString(summary, executor.SUMMARY_HEADER) +
				"\n\nTotal time spent: $sum - ($from - $to)")
	}

}

fun weekRange(today: LocalDate): Pair<LocalDate, LocalDate> {
	val dayOfWeek = today.dayOfWeek
	val from = dayOfWeek.value - 1
	val to = 7 - dayOfWeek.value
	return today.minusDays(from.toLong()) to today.plusDays(to.toLong())
}

fun today(): LocalDate = LocalDate.now()
