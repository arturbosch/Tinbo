package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.commands.EditableCommands
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.nullIfEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class NoteCommands @Autowired constructor(executor: NoteExecutor) :
		EditableCommands<NoteEntry, NoteData, DummyNote>(executor) {

	override val id: String = "note"

	private val SUCCESS_MESSAGE = "Successfully added a note."

	@CliAvailabilityIndicator("note", "loadNotes", "editNote")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(NotesMode)
	}

	override fun add(): String {
		return whileNotInEditMode {
			addNote(console.readLine("Enter a message: "))
		}
	}

	@CliCommand(value = "note", help = "Adds a new note.")
	fun addNote(@CliOption(key = arrayOf("message", "m", ""), mandatory = true, help = "Summary of the task.",
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String): String {

		return whileNotInEditMode {
			if (message.isEmpty()) {
				"You need to specify a message."
			} else {
				executor.addEntry(NoteEntry(message))
				SUCCESS_MESSAGE
			}
		}
	}

	@CliCommand("editNote", help = "Edits the note entry(/entries) with given index")
	fun editNote(@CliOption(key = arrayOf("index", "i"), mandatory = true, help = "Index of the task to edit.") index: Int,
				 @CliOption(key = arrayOf("message", "m", ""), help = "Summary of the task.") message: String?): String {

		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				executor.editEntry(i, DummyNote(message))
				"Successfully edited a note."
			}
		}
	}

	override fun edit(index: Int): String {
		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val message = console.readLine("Enter a message or leave empty if unchanged: ").nullIfEmpty()
				executor.editEntry(i, DummyNote(message))
				"Successfully edited a note."
			}
		}
	}

}

