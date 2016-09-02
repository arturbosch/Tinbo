package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.TiNBo
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.config.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.shell.plugin.BannerProvider
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TinboCommands @Autowired constructor(val bannerProvider: BannerProvider) : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("welcome", "weather")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isStartMode()
	}

	@CliCommand("welcome", help = "Shows the banner and welcome message.")
	fun welcome(): String {
		return "\n${bannerProvider.banner}\n${bannerProvider.welcomeMessage}"
	}

	@CliCommand("weather", help = "Shows the weather for following three days inclusive today's.")
	fun weather(@CliOption(
			key = arrayOf("", "city"),
			help = "Provide a existing city name.",
			specifiedDefaultValue = "",
			unspecifiedDefaultValue = "") city: String): String {

		var cityName = city
		if (cityName.isEmpty()) {
			cityName = TiNBo.config.getCity()
		}

		if (cityName.matches(Regex("[a-zA-Z]+"))) {
			return Notification.weather(cityName)
		} else {
			return "The given city name must consist of only letters."
		}
	}
}