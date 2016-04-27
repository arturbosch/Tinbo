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
                                              val taskCommands: TaskCommands,
                                              val noopCommands: NoopCommands) : CommandMarker {

	@CliAvailabilityIndicator("ls", "save", "cancel", "remove")
	fun basicsAvailable(): Boolean {
		return ModeAdvisor.isTimerMode() || ModeAdvisor.isNotesMode() || ModeAdvisor.isTasksMode()
	}

	private fun getCommandsForCurrentMode(): Editable {
		return when (ModeAdvisor.getMode()) {
			Mode.NOTES -> noteCommands
			Mode.TASKS -> taskCommands
			Mode.TIMER -> timeEditCommands
			else -> noopCommands
		}
	}

	@CliCommand("ls", "list", help = "Lists all entries.")
	fun list(@CliOption(
			key = arrayOf("category", "cat"),
			unspecifiedDefaultValue = "",
			specifiedDefaultValue = "",
			help = "Name to filter only for this specific category.") categoryName: String): String {

		return getCommandsForCurrentMode().list(categoryName)
	}

	@CliCommand("cancel", help = "Cancels edit mode.")
	fun cancel(): String {
		return getCommandsForCurrentMode().cancel()
	}

	@CliCommand("save", help = "Saves current editing if list command was used.")
	fun save(@CliOption(key = arrayOf("name", "n"), help = "Saves notes under a new data set (also a new filename).",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") name: String): String {

		return getCommandsForCurrentMode().save(name)
	}

	@CliCommand("remove", "delete", help = "Deletes entries from storage.")
	fun delete(@CliOption(key = arrayOf("indices", "index", "i"), mandatory = true,
			help = "Indices pattern, allowed are numbers with space in between or intervals like 1-5 e.g. '1 2 3-5 6'.")
	           indexPattern: String): String {

		return getCommandsForCurrentMode().delete(indexPattern)
	}

}
