package io.gitlab.arturbosch.tinbo.charts

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class ChartsPlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun registerCommands(tinbo: TinboContext): List<Command> {
		return listOf(BurndownChart(tinbo), WeekMonthChart(tinbo))
	}
}
