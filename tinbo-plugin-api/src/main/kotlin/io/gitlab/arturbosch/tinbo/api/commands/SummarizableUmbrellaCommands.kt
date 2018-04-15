package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class SummarizableUmbrellaCommands @Autowired constructor(private val commandChooser: CommandChooser) : Command {

	override val id: String = "sum"

	@CliAvailabilityIndicator("sum")
	fun isAvailable(): Boolean {
		return ModeManager.isSumAllowed()
	}

	@CliCommand(value = ["sum"], help = "Sums up entries of all or specified categories.")
	fun sumCategories(@CliOption(key = ["", "categories"],
			help = "Specify categories to show sum for. Default: for all.",
			unspecifiedDefaultValue = "",
			specifiedDefaultValue = "") categories: String): String {

		val filters =
				if (categories.isEmpty()) listOf()
				else categories.split(Regex("[,; ]+")).map { it.trim().toLowerCase() }

		return commandChooser.forSummarizableMode().sum(filters)
	}
}
