package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.config.TinboMode
import io.gitlab.arturbosch.tinbo.api.utils.printlnInfo
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class StartTaskModeCommand : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("tasks")
	fun onlyModeCommands(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("tasks", help = "Switch to tasks mode to write down tasks.")
	fun tasksMode() {
		ModeManager.current = TasksMode
		printlnInfo("Entering tasks mode...")
	}

}
