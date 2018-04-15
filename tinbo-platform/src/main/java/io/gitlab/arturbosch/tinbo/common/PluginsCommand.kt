package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.model.util.CSVTablePrinter
import io.gitlab.arturbosch.tinbo.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.plugins.TinboPlugin
import io.gitlab.arturbosch.tinbo.plusElementAtBeginning
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class PluginsCommand(private val context: TinboContext) : Command {

	override val id: String = "plugins"
	private val csv = CSVTablePrinter()

	@CliAvailabilityIndicator("plugins")
	fun onlyInStartMode() = ModeManager.isCurrentMode(TinboMode.START)

	@CliCommand("plugins", help = "Lists all used plugins with their specified versions.")
	fun plugins(): String {
		val plugins = context.beansOf<TinboPlugin>().values
		return if (plugins.isEmpty()) {
			"No plugins registered."
		} else {
			val entries = plugins
					.map { "${it.name()};${it.version()}" }
					.plusElementAtBeginning("Name;Version")
			return csv.asTable(entries).joinToString("\n")
		}
	}

}
