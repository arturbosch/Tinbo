package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.plugin.BannerProvider
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TinboCommands @Autowired constructor(private val bannerProvider: BannerProvider) : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("welcome")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("welcome", help = "Shows the banner and welcome message.")
	fun welcome(): String {
		return "\n${bannerProvider.banner}\n${bannerProvider.welcomeMessage}"
	}

}
