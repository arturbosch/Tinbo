package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class StartTimeModeCommand : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("time")
	fun onlyModeCommands(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("time", help = "Switch to time mode where you can start timers and list previous timings.")
	fun timerMode() {
		ModeManager.current = TimeMode
		printlnInfo("Entering time mode...")
	}

}