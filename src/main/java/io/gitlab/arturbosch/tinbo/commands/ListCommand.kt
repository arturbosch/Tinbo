package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class ListCommand @Autowired constructor(val commandChooser: CommandChooser) : Command {

	override val id: String = "share"

	@CliAvailabilityIndicator("ls")
	fun basicsAvailable(): Boolean {
		return ModeManager.isEditAllowed()
	}

	@CliCommand("ls", "list", help = "Lists all entries.")
	fun list(
			@CliOption(
					key = arrayOf("category", "c", ""),
					unspecifiedDefaultValue = "",
					specifiedDefaultValue = "",
					help = "Name to filter only for this specific category.") categoryName: String,
			@CliOption(
					key = arrayOf("all"),
					help = "Should all entries be printed?",
					unspecifiedDefaultValue = "false",
					specifiedDefaultValue = "true") all: Boolean): String {

		return commandChooser.forListableMode().list(categoryName, all)
	}

}