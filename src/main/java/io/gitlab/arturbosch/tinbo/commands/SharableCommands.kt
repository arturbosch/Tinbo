package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.UnsupportedMarker
import io.gitlab.arturbosch.tinbo.config.Defaults
import io.gitlab.arturbosch.tinbo.config.ModeManager
import jline.console.ConsoleReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class SharableCommands @Autowired constructor(val commandChooser: CommandChooser,
												   val consoleReader: ConsoleReader) : Command {

	override val id: String = "edit"

	@CliAvailabilityIndicator("save", "cancel", "remove", "changeCategory", "data", "last", "edit", "load")
	fun basicsAvailable(): Boolean {
		return ModeManager.isEditAllowed()
	}

	@CliCommand("load", help = "Loads/Creates an other data set. Data sets are stored under ~/TiNBo/<MODE>/<NAME.")
	fun load(@CliOption(key = arrayOf("name", ""), mandatory = true,
			specifiedDefaultValue = Defaults.NOTES_NAME,
			unspecifiedDefaultValue = Defaults.NOTES_NAME) name: String): String {
		return commandChooser.forCurrentMode().load(name)
	}

	@CliCommand("cancel", help = "Cancels edit mode.")
	fun cancel(): String {
		return commandChooser.forCurrentMode().cancel()
	}

	@CliCommand("save", help = "Saves current editing if list command was used.")
	fun save(@CliOption(key = arrayOf("name", "n"), help = "Saves notes under a new data set (also a new filename).",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") name: String): String {
		return commandChooser.forCurrentMode().save(name)
	}

	private val SAVE_OR_CANCEL_CHANGES = " (Remember to 'save' or 'cancel' to apply changes!)"

	@CliCommand("edit", help = "Edits entry with specified index.")
	fun edit(@CliOption(key = arrayOf("", "i", "index"), mandatory = true,
			help = "Index of entry to edit.") index: Int): String {
		return commandChooser.forCurrentMode().edit(index) + SAVE_OR_CANCEL_CHANGES
	}

	@CliCommand("remove", "delete", help = "Deletes entries from storage.")
	fun delete(@CliOption(key = arrayOf("indices", "index", "i"), mandatory = true,
			help = "Indices pattern, allowed are numbers with space in between or intervals like 1-5 e.g. '1 2 3-5 6'.")
			   indexPattern: String): String {
		return commandChooser.forCurrentMode().delete(indexPattern) + SAVE_OR_CANCEL_CHANGES
	}

	@CliCommand("last", help = "Deletes the last entry from storage.")
	fun delete(): String {
		return commandChooser.forCurrentMode().delete("-1") + SAVE_OR_CANCEL_CHANGES
	}

	@CliCommand("changeCategory", help = "Changes a categories name with the side effect that all entries of this category get updated.")
	fun changeCategory(@CliOption(key = arrayOf("old"), help = "Old category name.",
			unspecifiedDefaultValue = "", specifiedDefaultValue = "") old: String,
					   @CliOption(key = arrayOf("new"), help = "Old category name.",
							   unspecifiedDefaultValue = "", specifiedDefaultValue = "") new: String): String {

		val commandsForCurrentMode = commandChooser.forCurrentMode()
		if (commandsForCurrentMode is UnsupportedMarker || commandsForCurrentMode is NoopCommands) {
			return "Changing category is not supported for this mode!"
		}

		var oldName = old
		var newName = new
		if (old.isEmpty()) {
			oldName = consoleReader.readLine("Enter a old category name to replace: ")
		}
		if (new.isEmpty()) {
			newName = consoleReader.readLine("Enter a new category name: ")
		}

		if (oldName.isEmpty() || newName.isEmpty()) {
			return "Specify old and new category name"
		}

		return commandsForCurrentMode.changeCategory(oldName, newName)
	}

	@CliCommand("data", help = "prints all available data sets")
	fun data(): String {
		return commandChooser.forCurrentMode().data()
	}
}
