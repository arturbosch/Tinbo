package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.utils.printlnInfo
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class BackCommand : Command {

	override val id: String = "mode"

	@CliAvailabilityIndicator("back")
	fun noExitCommand(): Boolean {
		return !ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("back", "..",
			help = "Exits current mode and enters start mode where you have access to all other modes.")
	fun startMode() {
		if (ModeManager.isBackCommandBlocked) {
			printlnInfo("Can't change mode when editing... use save or cancel.")
		} else {
			ModeManager.current = TinboMode.START
			printlnInfo("Entering tinbo mode...")
		}
	}
}
