package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.WeekEntry
import io.gitlab.arturbosch.tinbo.WeekSummary
import io.gitlab.arturbosch.tinbo.time.TimeDataHolder
import io.gitlab.arturbosch.tinbo.time.timeAsMinutes
import io.gitlab.arturbosch.tinbo.time.weekRange
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author Artur Bosch
 */
@Component
class TimeSummaryPluginHelper @Autowired constructor(val timeDataHolder: TimeDataHolder) : PluginHelper {

	@PluginSupport
	fun week(): WeekSummary {
		return week(LocalDate.now())
	}

	fun week(date: LocalDate): WeekSummary {
		val data = timeDataHolder.getEntries()
		val (from, to) = weekRange(date)
		val entriesOfWeek = data.asSequence()
				.filter { it.date >= from && it.date <= to }
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
		val data = timeDataHolder.getEntries()
		val month = date.month
		val year = date.year
		val entriesOfWeek = data.asSequence()
				.filter { it.date.month == month && it.date.year == year }
				.map { it.category to it.timeAsMinutes() }
				.groupBy({ it.first }, { it.second })
				.map { it.key to it.value.sum() }
				.map { WeekEntry(it.first, it.second) }
		return WeekSummary(LocalDate.of(year, month, 1) to
				LocalDate.of(year, month, month.minLength()), entriesOfWeek)
	}

}