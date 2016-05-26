package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.config.Mode
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.notes.NoteCommands
import com.gitlab.artismarti.tinbo.tasks.TaskCommands
import com.gitlab.artismarti.tinbo.time.TimeEditCommands
import jline.console.ConsoleReader
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
open class SharableCommands @Autowired constructor(val timeEditCommands: TimeEditCommands,
                                                   val noteCommands: NoteCommands,
                                                   val taskCommands: TaskCommands,
                                                   val noopCommands: NoopCommands,
                                                   val consoleReader: ConsoleReader) : CommandMarker {

	@CliAvailabilityIndicator("ls", "save", "cancel", "remove", "changeCategory")
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

	@CliCommand("changeCategory", help = "Changes a categories name with the side effect that all entries of this category get updated.")
	fun changeCategory(@CliOption(key = arrayOf("old"), help = "Old category name.",
			unspecifiedDefaultValue = "", specifiedDefaultValue = "") old: String,
	                   @CliOption(key = arrayOf("new"), help = "Old category name.",
			                   unspecifiedDefaultValue = "", specifiedDefaultValue = "") new: String): String {

		val commandsForCurrentMode = getCommandsForCurrentMode()
		if (commandsForCurrentMode is NoteCommands || commandsForCurrentMode is NoopCommands) {
			return "Changing category is not yet supported for notes."
		}

		var oldName = old
		var newName = new
		if (old.isEmpty()) {
			oldName = consoleReader.readLine("Enter a old category name to replace: ")
		}
		if (new.isEmpty()) {
			newName = consoleReader.readLine("Enter a new category name: ")
		}

		if(oldName.isEmpty() || newName.isEmpty()) {
			return "Specify old and new category name"
		}

		return commandsForCurrentMode.changeCategory(oldName, newName)
	}

}
