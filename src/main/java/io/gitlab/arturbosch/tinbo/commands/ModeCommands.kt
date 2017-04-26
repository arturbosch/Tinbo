package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.notes.NotesMode
import io.gitlab.arturbosch.tinbo.psp.ProjectsMode
import io.gitlab.arturbosch.tinbo.tasks.TasksMode
import io.gitlab.arturbosch.tinbo.time.TimeMode
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

	@CliAvailabilityIndicator("time", "tasks", "notes", "projects")
	fun onlyModeCommands(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("time", help = "Switch to time mode where you can start timers and list previous timings.")
	fun timerMode() {
		ModeManager.current = TimeMode
		printlnInfo("Entering time mode...")
	}

	@CliCommand("tasks", help = "Switch to tasks mode to write down tasks.")
	fun tasksMode() {
		ModeManager.current = TasksMode
		printlnInfo("Entering tasks mode...")
	}

	@CliCommand("notes", help = "Switch to notes mode to write down tasks.")
	fun notesMode() {
		ModeManager.current = NotesMode
		printlnInfo("Entering notes mode...")
	}

	@CliCommand("projects", help = "Switch to projects mode, managing projects like in PSP.")
	fun projectsMode() {
		ModeManager.current = ProjectsMode
		printlnInfo("Entering projects mode...")
	}

}
