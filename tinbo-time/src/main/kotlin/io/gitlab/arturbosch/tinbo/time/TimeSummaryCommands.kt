package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.WeekSummary
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.Summarizable
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.toTimeString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author artur
 */
@Component
open class TimeSummaryCommands @Autowired constructor(val summaryExecutor: WeekSummaryExecutor,
													  val timeSummaryPluginSupport: TimeSummaryPluginSupport) :
		Summarizable, Command {

	override val id: String = "time"

	@CliAvailabilityIndicator("week", "month", "day")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(TimeMode)
	}

	override fun sum(categories: List<String>): String {
		if (categories.isEmpty()) {
			return summaryExecutor.sumAllCategories()
		} else {
			return summaryExecutor.sumForCategories(categories)
		}
	}

	@CliCommand("day", help = "Summarizes current days activities.")
	fun daySummary(): String {
		val now = LocalDate.now()
		val summaryString = monthSummaryAsString { timeSummaryPluginSupport.day(now) }
		val summaryLastString = monthSummaryAsString { timeSummaryPluginSupport.day(now.minusDays(1)) }
		return summaryLastString + "\n\n" + summaryString
	}

	@CliCommand("week", help = "Summarizes last and this week's time spending on categories.")
	fun weekSummary(): String {
		val now = LocalDate.now()
		val summaryString = monthSummaryAsString { timeSummaryPluginSupport.week(now) }
		val summaryLastString = monthSummaryAsString { timeSummaryPluginSupport.week(now.minusDays(7)) }
		return summaryLastString + "\n\n" + summaryString
	}

	@CliCommand("month", help = "Summarizes last and this month's time spending on categories.")
	fun monthSummary(): String {
		val now = LocalDate.now()
		val summaryString = monthSummaryAsString { timeSummaryPluginSupport.month(now) }
		val summaryLastString = monthSummaryAsString { timeSummaryPluginSupport.month(now.minusMonths(1)) }
		return summaryLastString + "\n\n" + summaryString
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

