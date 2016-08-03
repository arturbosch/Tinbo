package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.common.Command
import io.gitlab.arturbosch.tinbo.common.Summarizable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

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

}
