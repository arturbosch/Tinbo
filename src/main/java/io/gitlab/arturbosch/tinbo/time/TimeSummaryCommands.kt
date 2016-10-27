package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.Summarizable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TimeSummaryCommands @Autowired constructor(val executor: TimeExecutor,
													  val summaryExecutor: WeekSummaryExecutor) :
		Command, Summarizable {

	override val id: String = "time"

	override fun sum(categories: List<String>): String {
		if (categories.isEmpty()) {
			return executor.sumAllCategories()
		} else {
			return executor.sumForCategories(categories)
		}
	}

	@CliCommand("week", help = "Summarizes last and this week's time spending on categories.")
	fun weekSummary(): String {
		return summaryExecutor.twoWeekSummary()
	}

}
