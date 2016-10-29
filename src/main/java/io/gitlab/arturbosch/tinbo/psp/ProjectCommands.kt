package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.Task
import io.gitlab.arturbosch.tinbo.Time
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.orThrow
import io.gitlab.arturbosch.tinbo.utils.dateFormatter
import jline.console.ConsoleReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeParseException

/**
 * @author Artur Bosch
 */
@Component
class ProjectCommands @Autowired constructor(val console: ConsoleReader,
											 val currentProject: CurrentProject) : Command {
	override val id: String = "psp"

	@CliAvailabilityIndicator("show-tasks", "new-task", "show-project")
	fun isAvailable() = currentProject.isSpecified()

	@CliCommand("show-tasks", help = "Prints tasks of current opened project.")
	fun tasks(): String {
		return currentProject.showTasks()
	}

	@CliCommand("new-task", help = "Adds new task to current opened project.")
	fun newTask(): String {
		val name = console.readLine("Enter a name: ").orThrow { "Specify task name!" }
		val pTime = console.readLine("Enter planned time: ").orThrow { "Specify planned time!" }.toInt()
		val pUnits = console.readLine("Enter planned units: ").orThrow { "Specify planned units!" }.toInt()
		return try {
			val end = console.readLine("Enter end date of task (empty if today): ")
			val date = if (end.isEmpty()) LocalDate.now() else LocalDate.parse(end, dateFormatter)
			currentProject.addTask(Task(name = name, plannedTime = Time(pTime), plannedUnits = pUnits, end = date))
			return "Successfully added task to ${currentProject.name()}"
		} catch(e: DateTimeParseException) {
			"Could not parse given date."
		}
	}

}