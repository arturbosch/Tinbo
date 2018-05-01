package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.marker.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.plugin.BannerProvider
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TinboCommands @Autowired constructor(
		private val bannerProvider: BannerProvider,
		private val config: TinboConfig) : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("welcome")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("welcome", help = "Shows the banner and welcome message.")
	fun welcome(): String {
		return "\n${bannerProvider.banner}\n${bannerProvider.welcomeMessage}"
	}

	@CliCommand("reloadConfig", help = "Reloads tinbo.config file.")
	fun reloadConfig() {
		config.reloadConfigFile()
	}
}
