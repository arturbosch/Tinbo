package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.model.WeekEntry
import io.gitlab.arturbosch.tinbo.api.model.WeekSummary
import io.gitlab.arturbosch.tinbo.api.plugins.PluginHelper
import io.gitlab.arturbosch.tinbo.api.plugins.PluginSupport
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * Creates summaries for days, weeks or months.
 * Needs default parameterless methods for charts plugin.
 *
 * @author Artur Bosch
 */
@Component
class TimeSummaryPluginSupport @Autowired constructor(private val timeDataHolder: TimeDataHolder) : PluginHelper {

	@PluginSupport
	fun day(): WeekSummary {
		return day(LocalDate.now())
	}

	fun day(date: LocalDate): WeekSummary {
		val entriesOfDay = timeDataHolder.getEntries()
				.asSequence()
				.filter { it.date == date }
				.map { it.category to it.timeAsMinutes() }
				.groupBy({ it.first }, { it.second })
				.map { it.key to it.value.sum() }
				.map { WeekEntry(it.first, it.second) }
		return WeekSummary(date to date, entriesOfDay)

	}

	@PluginSupport
	fun week(): WeekSummary {
		return week(LocalDate.now())
	}

	fun week(date: LocalDate): WeekSummary {
		val (from, to) = weekRange(date)
		val entriesOfWeek = timeDataHolder.getEntries()
				.asSequence()
				.filter { it.date in from..to }
				.map { it.category to it.timeAsMinutes() }
				.groupBy({ it.first }, { it.second })
				.map { it.key to it.value.sum() }
				.map { WeekEntry(it.first, it.second) }
		return WeekSummary(from to to, entriesOfWeek)
	}

	@PluginSupport
	fun month(): WeekSummary {
		return month(LocalDate.now())
	}

	fun month(date: LocalDate): WeekSummary {
		val month = date.month
		val year = date.year
		val entriesOfWeek = timeDataHolder.getEntries()
				.asSequence()
				.filter { it.date.month == month && it.date.year == year }
				.map { it.category to it.timeAsMinutes() }
				.groupBy({ it.first }, { it.second })
				.map { it.key to it.value.sum() }
				.map { WeekEntry(it.first, it.second) }
		return WeekSummary(LocalDate.of(year, month, 1) to
				LocalDate.of(year, month, month.minLength()), entriesOfWeek)
	}

}
