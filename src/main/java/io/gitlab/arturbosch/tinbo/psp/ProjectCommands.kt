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
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * @author Artur Bosch
 */
@Component
class ProjectCommands @Autowired constructor(val console: ConsoleReader,
											 val currentProject: CurrentProject) : Command {
	override val id: String = "psp"

	@CliAvailabilityIndicator("show-tasks", "new-task", "close-task")
	fun isAvailable() = currentProject.isSpecified()

	@CliCommand("show-tasks", help = "Prints tasks of current opened project.")
	fun tasks(): String {
		return currentProject.showTasks()
	}

	@CliCommand("new-task", help = "Adds new task to current opened project.")
	fun newTask(): String {
		return try {
			val name = console.readLine("Enter a name: ").orThrow { "Specify task name!" }
			val pTime = console.readLine("Enter planned time: ").orThrow { "Specify planned time!" }.toInt()
			val pUnits = console.readLine("Enter planned units: ").orThrow { "Specify planned units!" }.toInt()
			val end = console.readLine("Enter end date of task (empty if today): ")
			val date = if (end.isEmpty()) LocalDate.now() else LocalDate.parse(end, dateFormatter)
			currentProject.addTask(Task(name = name, plannedTime = Time(pTime), plannedUnits = pUnits, end = date))
			return "Successfully added task to ${currentProject.name()}"
		} catch(e: Exception) {
			e.message ?: throw e
		}
	}

	@CliCommand("close-task", help = "Marks task with given starting name as finish.")
	fun finishTask(@CliOption(key = arrayOf("")) name: String?,
				   @CliOption(key = arrayOf("time")) minutes: Int?,
				   @CliOption(key = arrayOf("units")) units: Int?): String {
		val wrong = "There was no task starting with given characters!"
		return name?.let {
			if (minutes == null || units == null) return "Actual time and unit values must be specified!"
			val (success, task) = currentProject.closeTaskWithStartingName(name, minutes, units)
			return if (success) "Successfully closed task $task" else wrong
		} ?: wrong
	}

}