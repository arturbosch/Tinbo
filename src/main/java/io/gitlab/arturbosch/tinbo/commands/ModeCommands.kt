package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.psp.ProjectsMode
import io.gitlab.arturbosch.tinbo.tasks.TasksMode
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
open class ModeCommands : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("tasks", "projects")
	fun onlyModeCommands(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("tasks", help = "Switch to tasks mode to write down tasks.")
	fun tasksMode() {
		ModeManager.current = TasksMode
		printlnInfo("Entering tasks mode...")
	}

	@CliCommand("projects", help = "Switch to projects mode, managing projects like in PSP.")
	fun projectsMode() {
		ModeManager.current = ProjectsMode
		printlnInfo("Entering projects mode...")
	}

}
