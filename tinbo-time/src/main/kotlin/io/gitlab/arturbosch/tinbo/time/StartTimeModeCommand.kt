package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.utils.printlnInfo
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
		if (ExitedTimers.exists()) {
			printlnInfo("Unexpected exited timer found. Use 'continue' to restart it.")
		}
	}

}
