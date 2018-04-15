package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class NotesPlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun registerCommands(tinbo: TinboContext): List<Command> {
		val console = tinbo.beanOf<TinboTerminal>()
		val tinboConfig = tinbo.tinboConfig
		val persister = NotePersister(tinboConfig)
		val dataHolder = NoteDataHolder(persister, tinboConfig)
		val executor = NoteExecutor(dataHolder, tinboConfig)
		val noteCommands = NoteCommands(executor, console)
		tinbo.registerSingleton(noteCommands)

		val notesModeCommand = StartNotesModeCommand()
		tinbo.registerSingleton(notesModeCommand)

		tinbo.registerSingleton(persister)
		return listOf(noteCommands, notesModeCommand)
	}
}
