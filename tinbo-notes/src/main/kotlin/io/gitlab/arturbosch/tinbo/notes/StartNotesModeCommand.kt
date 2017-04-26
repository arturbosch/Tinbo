package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class StartNotesModeCommand : Command {

	override val id: String = "start"

	@CliAvailabilityIndicator("notes")
	fun onlyModeCommands(): Boolean {
		return ModeManager.isCurrentMode(TinboMode.START)
	}

	@CliCommand("notes", help = "Switch to notes mode to write down tasks.")
	fun notesMode() {
		ModeManager.current = NotesMode
		printlnInfo("Entering notes mode...")
	}

}