package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.utils.DateTimeFormatters
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.time.format.DateTimeParseException
import java.util.HashSet

/**
 * @author artur
 */
@Component
class TaskCommands(val executor: TaskExecutor = Injekt.get()) : CommandMarker {

	private val NEED_EDIT_MODE_TEXT = "Before adding or list tasks exit edit mode with 'save' or 'cancel'."
	private val SUCCESS_MESSAGE = "Successfully added a task."

	private var isListMode: Boolean = false
	private var isEditMode: Boolean = false

	@CliAvailabilityIndicator("task", "loadTasks", "editTasks", "listTasks", "deleteTask", "saveTasks")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isTasksMode()
	}

	@CliCommand(value = "task", help = "Adds a new task.")
	fun newTask(@CliOption(key = arrayOf("message", "msg", "m"), mandatory = true, help = "Summary of the task.",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
				@CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
						specifiedDefaultValue = Default.MAIN_CATEGORY_NAME,
						unspecifiedDefaultValue = Default.MAIN_CATEGORY_NAME) category: String,
				@CliOption(key = arrayOf("location", "loc", "l"), help = "Specify a location for this task.",
						specifiedDefaultValue = "", unspecifiedDefaultValue = "") location: String,
				@CliOption(key = arrayOf("description", "des", "d"), help = "Specify a description for this task.",
						specifiedDefaultValue = "", unspecifiedDefaultValue = "") description: String,
				@CliOption(key = arrayOf("start", "s"), help = "Specify a end time for this task. Format: yyyy-MM-dd HH:mm",
						specifiedDefaultValue = "", unspecifiedDefaultValue = "") startTime: String,
				@CliOption(key = arrayOf("end", "e"), help = "Specify a start time for this task. Format: yyyy-MM-dd HH:mm",
						specifiedDefaultValue = "", unspecifiedDefaultValue = "") endTime: String): String {

		var result = SUCCESS_MESSAGE

		if (isEditMode) {
			result = NEED_EDIT_MODE_TEXT
		} else if (startTime.isEmpty() && endTime.isEmpty()) {
			executor.addEntry(TaskEntry(message, description, location, category))
		} else if (startTime.isNotEmpty()) {
			try {
				val pair = DateTimeFormatters.parseDateTime(endTime, startTime)
				val formattedStartTime = pair.first
				var formattedEndTime = pair.second
				executor.addEntry(TaskEntry(message, description, location, category, formattedStartTime, formattedEndTime))
			} catch(e: DateTimeParseException) {
				result = "Could not parse date, use format: yyyy-MM-dd HH:mm"
			}
		}

		return result
	}

	@CliCommand("loadTasks", help = "Loads/Creates an other data set. Task data sets are stored under ~/tinbo/tasks/*.")
	fun loadTasks(@CliOption(key = arrayOf("name", "n"), mandatory = true,
			specifiedDefaultValue = Default.TASKS_NAME,
			unspecifiedDefaultValue = Default.TASKS_NAME) name: String) {
		executor.loadData(name)
	}

	@CliCommand("listTasks", help = "Lists all tasks.")
	fun listTasks(): String {
		if (isEditMode) {
			return NEED_EDIT_MODE_TEXT
		} else {
			isListMode = true
			return executor.listData()
		}
	}

	@CliCommand("cancel", help = "Cancels edit mode.")
	fun cancelTaskEditing(): String {
		if (isEditMode) {
			isEditMode = false
			isListMode = false
			return "Cancelled edit mode.    "
		} else {
			return "You need to be in edit mode to use cancel."
		}
	}

	@CliCommand("saveTasks", help = "Saves current editing if list command was used.")
	fun saveTasks(@CliOption(key = arrayOf("name", "n"), help = "Saves notes under a new data set (also a new filename).",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") name: String): String {
		if (isListMode && isEditMode) {
			isListMode = false
			isEditMode = false
			return executor.saveEntries(name)
		} else {
			return "You need to be in edit mode to use save."
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
		if (isListMode) {
			val i = index - 1
			if (executor.indexExists(i)) {
				isEditMode = true
				val pair = DateTimeFormatters.parseDateTimeOrDefault(endTime, startTime)
				executor.editEntry(i, DummyTask(message, category, location, description, pair.first, pair.second))
				return SUCCESS_MESSAGE
			} else {
				return "This index doesn't exist"
			}
		} else {
			return "Before editing tasks you have to 'list' them to get indices to work on."
		}
	}

	@CliCommand("deleteTask", "deleteTasks", "removeTask", "removeTasks", help = "Deletes tasks from storage.")
	fun deleteTask(@CliOption(key = arrayOf("indices", "index", "i"), mandatory = true,
			help = "Indices pattern, allowed are numbers with space in between or intervals like 1-5 e.g. '1 2 3-5 6'.") indexPattern: String): String {

		if (isListMode) {
			try {
				val indices = parseIndices(indexPattern)
				isEditMode = true
				executor.deleteEntries(indices)
				return "Successfully deleted task(s)."
			} catch(e: IllegalArgumentException) {
				return "Could not parse the indices pattern. Use something like '1 2 3-5 6'."
			}
		} else {
			return "Before deleting tasks you have to 'list' them to get indices to work on."
		}
	}

	private fun parseIndices(indexPattern: String): Set<Int> {
		val result = HashSet<Int>()
		val indices = indexPattern.split(" ")
		val regex = Regex("[1-9][0-9]*")
		val regex2 = Regex("[1-9]+-[0-9]+")
		for (index in indices) {
			if (regex.matches(index)) {
				result.add(index.toInt() - 1)
			} else if (regex2.matches(index)) {
				val interval = index.split("-")
				if (interval.size == 2) {
					val (i1, i2) = Pair(interval[0].toInt(), interval[1].toInt())
					IntRange(i1 - 1, i2 - 1)
							.forEach { result.add(it) }
				} else {
					throw IllegalAccessException()
				}
			} else {
				throw IllegalArgumentException()
			}
		}
		return result
	}
}
