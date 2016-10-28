package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.Summarizable
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TimeSummaryCommands @Autowired constructor(val summaryExecutor: WeekSummaryExecutor) :
		Summarizable, Command {

	override val id: String = "time"

	@CliAvailabilityIndicator("week")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTimerMode()
	}

	override fun sum(categories: List<String>): String {
		if (categories.isEmpty()) {
			return summaryExecutor.sumAllCategories()
		} else {
			return summaryExecutor.sumForCategories(categories)
		}
	}

	@CliCommand("week", help = "Summarizes last and this week's time spending on categories.")
	fun weekSummary(): String {
		return summaryExecutor.twoWeekSummary()
	}

}

