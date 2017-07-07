package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.plugins.TinboPlugin
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class PluginsCommand(val context: TinboContext) : Command {

	override val id: String = "plugins"

	@CliAvailabilityIndicator("plugins")
	fun onlyInStartMode() = ModeManager.isCurrentMode(TinboMode.START)

	@CliCommand("plugins", help = "Lists all used plugins with their specified versions.")
	fun plugins() {
		val plugins = context.beansOf<TinboPlugin>()
		plugins.forEach { _, plugin ->
			printlnInfo("${plugin.name}: ${plugin.version}")
		}
	}
}