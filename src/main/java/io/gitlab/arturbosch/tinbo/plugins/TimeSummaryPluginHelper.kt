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

	fun week(): WeekSummary {
		val data = timeDataHolder.getEntries()
		val (from, to) = weekRange(LocalDate.now())
		val entriesOfWeek = data.asSequence()
				.filter { it.date >= from && it.date <= to }
				.map { it.category to it.timeAsMinutes() }
				.groupBy({ it.first }, { it.second })
				.map { it.key to it.value.sum() }
				.map { WeekEntry(it.first, it.second) }
		return WeekSummary(from to to, entriesOfWeek)
	}

	fun month(): WeekSummary {
		val data = timeDataHolder.getEntries()
		val month = LocalDate.now().month
		val year = LocalDate.now().year
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