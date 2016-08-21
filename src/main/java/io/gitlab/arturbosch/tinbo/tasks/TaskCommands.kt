package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.TiNBo
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.commands.EditableCommands
import io.gitlab.arturbosch.tinbo.config.Defaults
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
import io.gitlab.arturbosch.tinbo.nullIfEmpty
import io.gitlab.arturbosch.tinbo.orDefault
import io.gitlab.arturbosch.tinbo.utils.DateTimeFormatters
import io.gitlab.arturbosch.tinbo.utils.dateTimeFormatter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

/**
 * @author artur
 */
@Component
open class TaskCommands @Autowired constructor(executor: TaskExecutor) :
		EditableCommands<TaskEntry, TaskData, DummyTask>(executor), Command {

	override val id: String = "task"

	private val SUCCESS_MESSAGE = "Successfully added a task."

	@CliAvailabilityIndicator("task", "loadTasks", "editTasks")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTasksMode()
	}

	override fun add(): String {
		return whileNotInEditMode {
			val category = console.readLine("Enter a category: ").orDefault(TiNBo.config.getCategoryName())
			val message = console.readLine("Enter a message: ")
			val location = console.readLine("Enter a location: ")
			val description = console.readLine("Enter a description: ")
			val startTime = console.readLine("Enter a start time (yyyy-MM-dd HH:mm): ")
					.orDefault(LocalDateTime.now().format(dateTimeFormatter))
			val endTime = console.readLine("Enter a end time (yyyy-MM-dd HH:mm): ")
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
	fun editTask(@CliOption(key = arrayOf("index", "i"), mandatory = true,
			help = "Index of the task to edit.") index: Int,
				 @CliOption(key = arrayOf("message", "msg", "m"),
						 help = "Summary of the task.") message: String?,
				 @CliOption(key = arrayOf("category", "cat", "c"),
						 help = "Category for the task") category: String?,
				 @CliOption(key = arrayOf("location", "loc", "l"),
						 help = "Specify a location for this task.") location: String?,
				 @CliOption(key = arrayOf("description", "des", "d"),
						 help = "Specify a description for this task.") description: String?,
				 @CliOption(key = arrayOf("start", "s"), unspecifiedDefaultValue = "", specifiedDefaultValue = "",
						 help = "Specify a end time for this task. Format: yyyy-MM-dd HH:mm") startTime: String,
				 @CliOption(key = arrayOf("end", "e"), unspecifiedDefaultValue = "", specifiedDefaultValue = "",
						 help = "Specify a start time for this task. Format: yyyy-MM-dd HH:mm") endTime: String): String {

		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val pair = DateTimeFormatters.parseDateTimeOrDefault(endTime, startTime)
				executor.editEntry(i, DummyTask(message, category, location, description, pair.first, pair.second))
				"Successfully edited a task."
			}
		}
	}

	override fun edit(index: Int): String {
		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val category = console.readLine("Enter a category (leave empty if unchanged): ").nullIfEmpty()
				val message = console.readLine("Enter a message (leave empty if unchanged): ").nullIfEmpty()
				val location = console.readLine("Enter a location (leave empty if unchanged): ").nullIfEmpty()
				val description = console.readLine("Enter a description (leave empty if unchanged): ").nullIfEmpty()
				val startTime = console.readLine("Enter a start time (yyyy-MM-dd HH:mm) (leave empty if unchanged): ").orEmpty()
				val endTime = console.readLine("Enter a end time (yyyy-MM-dd HH:mm) (leave empty if unchanged): ").orEmpty()

				val pair = DateTimeFormatters.parseDateTimeOrDefault(endTime, startTime)
				executor.editEntry(i, DummyTask(message, category, location, description, pair.first, pair.second))
				"Successfully edited a task."
			}
		}
	}
}
