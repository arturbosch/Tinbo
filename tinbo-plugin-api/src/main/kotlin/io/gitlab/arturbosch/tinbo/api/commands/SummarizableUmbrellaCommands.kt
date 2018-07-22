package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class SummarizableUmbrellaCommands @Autowired constructor(
		private val commandChooser: CommandChooser) : Command {

	override val id: String = "sum"

	@CliAvailabilityIndicator("sum")
	fun isAvailable(): Boolean {
		return ModeManager.isSumAllowed()
	}

	private val regex = Regex("[,; ]+")

	@CliCommand(value = ["sum"], help = "Sums up entries of all or specified categories.")
	fun sumCategories(
			@CliOption(key = ["", "categories"],
					help = "Specify categories to show sum for. Default: for all.",
					unspecifiedDefaultValue = "",
					specifiedDefaultValue = "") categories: String,
			@CliOption(key = ["filters"],
					help = "Specify which categories should not be considered in the summary.",
					unspecifiedDefaultValue = "",
					specifiedDefaultValue = "") categoryFilters: String): String {

		val categoriesToSum = categories.toSplittedList()
		val categoriesToFilter = categoryFilters.toSplittedList()
		return commandChooser.forSummarizableMode().sum(categoriesToSum, categoriesToFilter)
	}

	private fun String.toSplittedList() =
			if (isEmpty()) emptySet()
			else split(regex).map { it.trim().toLowerCase() }
					.filter { it.isNotEmpty() }
					.toSet()
}
