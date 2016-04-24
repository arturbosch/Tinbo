package com.gitlab.artismarti.tinbo.start

import com.gitlab.artismarti.tinbo.config.Notification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.shell.plugin.BannerProvider
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class TinboCommands @Autowired constructor(val bannerProvider: BannerProvider) : CommandMarker {

	@CliCommand("welcome", help = "Shows the banner and welcome message.")
	fun welcome(): String {
		return "\n${bannerProvider.banner}\n${bannerProvider.welcomeMessage}"
	}

	@CliCommand("weather", help = "Shows the weather for following three days inclusive today's.")
	fun weather(@CliOption(
			key = arrayOf("city", "c"),
			help = "Provide a existing city name.",
			specifiedDefaultValue = "Bremen",
			unspecifiedDefaultValue = "Bremen") city: String): String {

		if (city.matches(Regex("[a-zA-Z]+"))) {
			return Notification.weather(city)
		} else {
			return "The given city name must consist of only letters."
		}
	}
}
