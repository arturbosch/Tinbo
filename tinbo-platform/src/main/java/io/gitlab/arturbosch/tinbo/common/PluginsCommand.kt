package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.model.util.CSVTablePrinter
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import io.gitlab.arturbosch.tinbo.api.plusElementAtBeginning
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
