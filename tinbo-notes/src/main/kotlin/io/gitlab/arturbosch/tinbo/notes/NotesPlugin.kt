package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class NotesPlugin : TiNBoPlugin {

	override fun registerCommands(tinboContext: SpringContext): List<Command> {
		val tinboConfig = tinboContext.tinboConfig
		val persister = NotePersister(tinboConfig)
		val dataHolder = NoteDataHolder(persister, tinboConfig)
		val executor = NoteExecutor(dataHolder, tinboConfig)
		val noteCommands = NoteCommands(executor)
		val notesModeCommand = StartNotesModeCommand()
		tinboContext.registerSingleton("NoteCommands", noteCommands)
		tinboContext.registerSingleton("StartNoteModeCommand", notesModeCommand)
		return listOf(noteCommands, notesModeCommand)
	}

}