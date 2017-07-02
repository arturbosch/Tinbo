package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboConfig
import io.gitlab.arturbosch.tinbo.config.TinboMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.plugin.BannerProvider
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TinboCommands @Autowired constructor(val bannerProvider: BannerProvider,
												val config: TinboConfig) : Command {

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
