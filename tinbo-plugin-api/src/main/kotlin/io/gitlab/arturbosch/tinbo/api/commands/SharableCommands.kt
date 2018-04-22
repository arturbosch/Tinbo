package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.config.Defaults
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Cancelable
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.marker.Datable
import io.gitlab.arturbosch.tinbo.api.marker.Deletable
import io.gitlab.arturbosch.tinbo.api.marker.Loadable
import io.gitlab.arturbosch.tinbo.api.marker.Saveable
import io.gitlab.arturbosch.tinbo.api.marker.UnsupportedMarker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class SharableCommands @Autowired constructor(private val commandChooser: CommandChooser,
											  private val terminal: TinboTerminal) : Command {

	override val id: String = "edit"

	@CliAvailabilityIndicator("save", "cancel", "remove", "changeCategory", "data", "last", "edit", "load")
	fun basicsAvailable(): Boolean {
		return ModeManager.isEditAllowed()
	}

	@CliCommand("load", help = "Loads/Creates an other data set. Data sets are stored under ~/TiNBo/<MODE>/<NAME.")
	fun load(@CliOption(key = ["name", ""], mandatory = true,
			specifiedDefaultValue = Defaults.NOTES_NAME,
			unspecifiedDefaultValue = Defaults.NOTES_NAME) name: String): String {
		return commandChooser.forModeWithInterface<Loadable>().load(name)
	}

	@CliCommand("cancel", help = "Cancels edit mode.")
	fun cancel(): String {
		return commandChooser.forModeWithInterface<Cancelable>().cancel()
	}

	@CliCommand("save", help = "Saves current editing if list command was used.")
	fun save(@CliOption(key = ["name", "n"], help = "Saves notes under a new data set (also a new filename).",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") name: String): String {
		return commandChooser.forModeWithInterface<Saveable>().save(name)
	}

	@CliCommand("edit", help = "Edits entry with specified index.")
	fun edit(@CliOption(key = ["", "i", "index"], mandatory = true,
			help = "Index of entry to edit.") index: Int): String {
		return commandChooser.forCurrentMode().edit(index) + SAVE_OR_CANCEL_CHANGES
	}

	@CliCommand("remove", "delete", help = "Deletes entries from storage.")
	fun delete(@CliOption(key = ["indices", "index", "i"], mandatory = true,
			help = "Indices pattern, allowed are numbers with space in between or intervals like 1-5 e.g. '1 2 3-5 6'.")
			   indexPattern: String): String {
		return commandChooser.forModeWithInterface<Deletable>().delete(indexPattern) + SAVE_OR_CANCEL_CHANGES
	}

	@CliCommand("last", help = "Deletes the last entry from storage.")
	fun delete(): String {
		return commandChooser.forModeWithInterface<Deletable>().delete("-1") + SAVE_OR_CANCEL_CHANGES
	}

	@CliCommand("changeCategory", help = "Changes a categories name with the side effect that all entries of this category get updated.")
	fun changeCategory(@CliOption(key = ["old"], help = "Old category name.",
			unspecifiedDefaultValue = "", specifiedDefaultValue = "") old: String,
					   @CliOption(key = ["new"], help = "Old category name.",
							   unspecifiedDefaultValue = "", specifiedDefaultValue = "") new: String): String {

		val commandsForCurrentMode = commandChooser.forCurrentMode()
		if (commandsForCurrentMode is UnsupportedMarker || commandsForCurrentMode is NoopCommands) {
			return "Changing category is not supported for this mode!"
		}

		var oldName = old
		var newName = new
		if (old.isEmpty()) {
			oldName = terminal.readLine("Enter a old category name to replace: ")
		}
		if (new.isEmpty()) {
			newName = terminal.readLine("Enter a new category name: ")
		}

		if (oldName.isEmpty() || newName.isEmpty()) {
			return "Specify old and new category name"
		}

		return commandsForCurrentMode.changeCategory(oldName, newName)
	}

	@CliCommand("data", help = "prints all available data sets")
	fun data(): String {
		return commandChooser.forModeWithInterface<Datable>().data()
	}

	companion object {
		private const val SAVE_OR_CANCEL_CHANGES = " (Remember to 'save' or 'cancel' to apply changes!)"
	}
}
