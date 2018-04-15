package io.gitlab.arturbosch.tinbo.weather

import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class WeatherCommand(private val tinbo: TinboContext) : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("weather")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("weather", help = "Shows the weather for following three days inclusive today's.")
	fun weather(@CliOption(
			key = ["", "city"],
			help = "Provide an existing city name.",
			specifiedDefaultValue = "",
			unspecifiedDefaultValue = "") city: String): String {

		var cityName = city
		if (cityName.isEmpty()) {
			cityName = tinbo.config.getCity()
		}

		return if (cityName.matches(Regex("[a-zA-Z]+"))) {
			Weather.curl(cityName)
		} else {
			"The given city name must consist of only letters."
		}
	}

}
