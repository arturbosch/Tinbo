package io.gitlab.arturbosch.tinbo.weather

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class WeatherPlugin : TinboPlugin(), Command {

	override fun version(): String = "1.0.0"
	override val id: String = "start"

	override fun registerCommands(tinbo: TinboContext): List<Command> {
		return listOf(WeatherCommand(tinbo))
	}
}
