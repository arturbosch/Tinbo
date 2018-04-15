package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.PluginRegistry
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class HelpCommand @Autowired constructor(
		private val registry: PluginRegistry) : Command {

	override val id: String = "share"

	@CliCommand(value = ["helpme"], help = "List all commands usage")
	fun obtainHelp(
			@CliOption(
					key = ["", "command"],
					help = "Command name to provide help for")
			buffer: String?): String {

		val helpIds = ModeManager.current.helpIds

		val allowedCommands = registry.commands
				.filter { it.id in helpIds }

		return HelpParser(allowedCommands).obtainHelp(buffer)
	}

}
