package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.marker.Summarizable
import io.gitlab.arturbosch.tinbo.api.model.WeekEntry
import io.gitlab.arturbosch.tinbo.api.model.WeekSummary
import io.gitlab.arturbosch.tinbo.api.toTimeString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author artur
 */
@Component
open class TimeSummaryCommands @Autowired constructor(private val summaryExecutor: WeekSummaryExecutor,
													  private val timeSummaryPluginSupport: TimeSummaryPluginSupport) :
		Summarizable, Command {

	override val id: String = "time"

	@CliAvailabilityIndicator("week", "month", "day")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(TimeMode)
	}

	override fun sum(categories: Set<String>, categoryFilters: Set<String>): String {
		return if (categories.isEmpty()) {
			summaryExecutor.sumAllCategories(categoryFilters)
		} else {
			summaryExecutor.sumForCategories(categories, categoryFilters)
		}
	}

	@CliCommand("day", help = "Summarizes current days activities.")
	fun daySummary(): String {
		val now = LocalDate.now()
		val summaryString = monthSummaryAsString { timeSummaryPluginSupport.day(now) }
		val summaryLastString = monthSummaryAsString { timeSummaryPluginSupport.day(now.minusDays(1)) }
		return summaryLastString + "\n\n" + summaryString +
				"\nMean time spent per day: ${formatTime(meanWorkPerDay(), true)}"
	}

	private fun meanWorkPerDay(): Pair<Int, Int> {
		val now = LocalDate.now()
		val daySummaries = (1..7)
				.map { timeSummaryPluginSupport.day(now.minusDays(it.toLong())).totalMinutes() }
				.filter { it > 0 }
		val workForLastSevenDaysInMinutes = daySummaries
				.fold(0) { acc, i -> acc + i } / daySummaries.size
		return WeekEntry("Day", workForLastSevenDaysInMinutes).asHourMinutes()
	}

	@CliCommand("week", help = "Summarizes last and this week's time spending on categories.")
	fun weekSummary(): String {
		val now = LocalDate.now()
		val summaryString = monthSummaryAsString { timeSummaryPluginSupport.week(now) }
		val summaryLastString = monthSummaryAsString { timeSummaryPluginSupport.week(now.minusDays(7)) }
		return summaryLastString + "\n\n" + summaryString +
				"\nMean time spent per week: ${formatTime(meanWorkPerWeek(), true)}"
	}

	private fun meanWorkPerWeek(): Pair<Int, Int> {
		val now = LocalDate.now()
		val weekSummaries = (1..7)
				.map { timeSummaryPluginSupport.week(now.minusDays((it * 7).toLong())).totalMinutes() }
				.filter { it > 0 }
		val workForLastSevenWeeksInMinutes = weekSummaries
				.fold(0) { acc, i -> acc + i } / weekSummaries.size
		return WeekEntry("Week", workForLastSevenWeeksInMinutes).asHourMinutes()
	}

	@CliCommand("month", help = "Summarizes last and this month's time spending on categories.")
	fun monthSummary(): String {
		val now = LocalDate.now()
		val summaryString = monthSummaryAsString { timeSummaryPluginSupport.month(now) }
		val summaryLastString = monthSummaryAsString { timeSummaryPluginSupport.month(now.minusMonths(1)) }
		return summaryLastString + "\n\n" + summaryString +
				"\nMean time spent per month: ${formatTime(meanWorkPerMonth(), true)}"
	}

	private fun meanWorkPerMonth(): Pair<Int, Int> {
		val now = LocalDate.now()
		val monthSummaries = (1..7)
				.map { timeSummaryPluginSupport.month(now.minusMonths(it.toLong())).totalMinutes() }
				.filter { it > 0 }
		val workForLastSevenMonthsInMinutes = monthSummaries
				.fold(0) { acc, i -> acc + i } / monthSummaries.size
		return WeekEntry("Month", workForLastSevenMonthsInMinutes).asHourMinutes()
	}

	private fun monthSummaryAsString(now: () -> WeekSummary): String {
		val month = now.invoke()
		val monthString = month.entries.map {
			val pair = it.asHourMinutes()
			"${it.category};${formatTime(pair)}"
		}
		return summaryExecutor.asTable(monthString) +
				"\n\nTotal time spent: ${formatTime(month.totalHourMinutes(), prefix = true)} - ${month.asDateRangeString()}"
	}

	private fun formatTime(pair: Pair<Int, Int>, prefix: Boolean = false): String {
		val h = if (prefix) "h" else ""
		val m = if (prefix) "m" else ""
		return "${pair.first.toTimeString()}$h:${pair.second.toTimeString()}$m"
	}

}

