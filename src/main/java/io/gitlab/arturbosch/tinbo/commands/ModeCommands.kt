package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.providers.PromptProvider
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
open class ModeCommands @Autowired constructor(val promptProvider: PromptProvider) : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("time", "tasks", "notes", "finance", "projects")
	fun onlyModeCommands(): Boolean {
		return ModeAdvisor.isStartMode()
	}

	@CliCommand("time", help = "Switch to time mode where you can start timers and list previous timings.")
	fun timerMode() {
		ModeAdvisor.setTimerMode()
		promptProvider.promptText = "time"
		printlnInfo("Entering time mode...")
	}

	@CliCommand("finance", help = "Switch to finance mode where you can manage your finances.")
	fun financeMode() {
		ModeAdvisor.setFinanceMode()
		promptProvider.promptText = "finance"
		printlnInfo("Entering finance mode...")
	}

	@CliCommand("tasks", help = "Switch to tasks mode to write down tasks.")
	fun tasksMode() {
		ModeAdvisor.setTasksMode()
		promptProvider.promptText = "tasks"
		printlnInfo("Entering tasks mode...")
	}

	@CliCommand("notes", help = "Switch to notes mode to write down tasks.")
	fun notesMode() {
		ModeAdvisor.setNotesMode()
		promptProvider.promptText = "notes"
		printlnInfo("Entering notes mode...")
	}

	@CliCommand("projects", help = "Switch to projects mode, managing projects like in PSP.")
	fun projectsMode() {
		ModeAdvisor.setProjectsMode()
		promptProvider.promptText = "projects"
		printlnInfo("Entering projects mode...")
	}

}
