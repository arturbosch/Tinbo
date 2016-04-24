package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.config.Mode
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.notes.NoteCommands
import com.gitlab.artismarti.tinbo.tasks.TaskCommands
import com.gitlab.artismarti.tinbo.time.TimeEditCommands
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class SharableCommands @Autowired constructor(val timeEditCommands: TimeEditCommands,
                                              val noteCommands: NoteCommands,
                                              val taskCommands: TaskCommands) : CommandMarker {

	@CliAvailabilityIndicator("ls", "save", "cancel", "remove")
	fun basicsAvailable(): Boolean {
		return ModeAdvisor.isTimerMode() || ModeAdvisor.isNotesMode() || ModeAdvisor.isTasksMode()
	}

	@CliCommand("ls", "list", help = "Lists all entries.")
	fun list(@CliOption(
			key = arrayOf("category", "cat"),
			unspecifiedDefaultValue = "",
			specifiedDefaultValue = "",
			help = "Name to filter only for this specific category.") categoryName: String): String {

		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands.list(categoryName)
			Mode.TASKS -> taskCommands.list(categoryName)
			Mode.TIMER -> timeEditCommands.list(categoryName)
			else -> "Wrong mode"
		}
	}

	@CliCommand("cancel", help = "Cancels edit mode.")
	fun cancel(): String {

		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands.cancel()
			Mode.TASKS -> taskCommands.cancel()
			Mode.TIMER -> timeEditCommands.cancel()
			else -> "Wrong mode"
		}
	}

	@CliCommand("save", help = "Saves current editing if list command was used.")
	fun save(@CliOption(key = arrayOf("name", "n"), help = "Saves notes under a new data set (also a new filename).",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") name: String): String {

		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands.save(name)
			Mode.TASKS -> taskCommands.save(name)
			Mode.TIMER -> timeEditCommands.save(name)
			else -> "Wrong mode"
		}
	}

	@CliCommand("remove", "delete", help = "Deletes entries from storage.")
	fun delete(@CliOption(key = arrayOf("indices", "index", "i"), mandatory = true,
			help = "Indices pattern, allowed are numbers with space in between or intervals like 1-5 e.g. '1 2 3-5 6'.")
	           indexPattern: String): String {

		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands.delete(indexPattern)
			Mode.TASKS -> taskCommands.delete(indexPattern)
			Mode.TIMER -> timeEditCommands.delete(indexPattern)
			else -> "Wrong mode"
		}
	}

}
