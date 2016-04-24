package com.gitlab.artismarti.tinbo.time

import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class TimeSummaryCommands @Autowired constructor(val executor: TimeExecutor) : CommandMarker {

	@CliAvailabilityIndicator("sum")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTimerMode()
	}

	@CliCommand(value = "sum", help = "Sums up times of all or specified categories.")
	fun sumCategories(@CliOption(key = arrayOf("categories", "cat", "c"),
			help = "Specify categories to show sum for. Default: for all.",
			unspecifiedDefaultValue = "",
			specifiedDefaultValue = "") categories: String): String {

		if (categories.isEmpty()) {
			return executor.sumAllCategories()
		} else {
			return executor.sumForCategories(categories)
		}
	}

}
