package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.common.EditableCommands
import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.orThrow
import com.gitlab.artismarti.tinbo.utils.DateTimeFormatters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.format.DateTimeParseException

/**
 * @author artur
 */
@Component
open class TaskCommands @Autowired constructor(executor: TaskExecutor) :
		EditableCommands<TaskEntry, TaskData, DummyTask>(executor), CommandMarker {

	private val SUCCESS_MESSAGE = "Successfully added a task."

	@CliAvailabilityIndicator("task", "loadTasks", "editTasks")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTasksMode()
	}

	@CliCommand("loadTasks", help = "Loads/Creates an other data set. Task data sets are stored under ~/tinbo/tasks/*.")
	fun loadTasks(@CliOption(key = arrayOf("name", "n"), mandatory = true,
			specifiedDefaultValue = Defaults.TASKS_NAME,
			unspecifiedDefaultValue = Defaults.TASKS_NAME) name: String) {
		executor.loadData(name)
	}

	override fun add(): String {
		return whileNotInEditMode {
			val category = console.readLine("Enter a category: ").orThrow()
			val message = console.readLine("Enter a message: ")
			val location = console.readLine("Enter a location: ")
			val description = console.readLine("Enter a description: ")
			val startTime = console.readLine("Enter a start time (yyyy-MM-dd HH:mm): ").orThrow()
			val endTime = console.readLine("Enter a end time (yyyy-MM-dd HH:mm): ").orThrow()
			addTask(message, category, location, description, startTime, endTime)
		}
	}

	@CliCommand(value = "task", help = "Adds a new task.")
	fun addTask(@CliOption(key = arrayOf("message", "msg", "m"), mandatory = true, help = "Summary of the task.",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
	            @CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
			            specifiedDefaultValue = Defaults.MAIN_CATEGORY_NAME,
			            unspecifiedDefaultValue = Defaults.MAIN_CATEGORY_NAME) category: String,
	            @CliOption(key = arrayOf("location", "loc", "l"), help = "Specify a location for this task.",
			            specifiedDefaultValue = "", unspecifiedDefaultValue = "") location: String,
	            @CliOption(key = arrayOf("description", "des", "d"), help = "Specify a description for this task.",
			            specifiedDefaultValue = "", unspecifiedDefaultValue = "") description: String,
	            @CliOption(key = arrayOf("start", "s"), help = "Specify a end time for this task. Format: yyyy-MM-dd HH:mm",
			            specifiedDefaultValue = "", unspecifiedDefaultValue = "") startTime: String,
	            @CliOption(key = arrayOf("end", "e"), help = "Specify a start time for this task. Format: yyyy-MM-dd HH:mm",
			            specifiedDefaultValue = "", unspecifiedDefaultValue = "") endTime: String): String {

		return whileNotInEditMode {

			if (startTime.isNotEmpty()) {
				try {
					val pair = DateTimeFormatters.parseDateTime(endTime, startTime)
					val formattedStartTime = pair.first
					val formattedEndTime = pair.second
					executor.addEntry(TaskEntry(message, description, location, category, formattedStartTime, formattedEndTime))
					SUCCESS_MESSAGE
				} catch(e: DateTimeParseException) {
					"Could not parse date, use format: yyyy-MM-dd HH:mm"
				}
			} else {
				executor.addEntry(TaskEntry(message, description, location, category))
				SUCCESS_MESSAGE
			}

		}
	}

	@CliCommand("editTask", "editTasks", help = "Edits the task entry with given index")
	fun editTask(@CliOption(key = arrayOf("index", "i"), mandatory = true, help = "Index of the task to edit.") index: Int,
	             @CliOption(key = arrayOf("message", "msg", "m"), help = "Summary of the task.",
			             specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
	             @CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
			             specifiedDefaultValue = "", unspecifiedDefaultValue = "") category: String,
	             @CliOption(key = arrayOf("location", "loc", "l"), help = "Specify a location for this task.",
			             specifiedDefaultValue = "", unspecifiedDefaultValue = "") location: String,
	             @CliOption(key = arrayOf("description", "des", "d"), help = "Specify a description for this task.",
			             specifiedDefaultValue = "", unspecifiedDefaultValue = "") description: String,
	             @CliOption(key = arrayOf("start", "s"), help = "Specify a end time for this task. Format: yyyy-MM-dd HH:mm",
			             specifiedDefaultValue = "", unspecifiedDefaultValue = "") startTime: String,
	             @CliOption(key = arrayOf("end", "e"), help = "Specify a start time for this task. Format: yyyy-MM-dd HH:mm",
			             specifiedDefaultValue = "", unspecifiedDefaultValue = "") endTime: String): String {

		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val pair = DateTimeFormatters.parseDateTimeOrDefault(endTime, startTime)
				executor.editEntry(i, DummyTask(message, category, location, description, pair.first, pair.second))
				"Successfully edited a task."
			}
		}
	}

}
