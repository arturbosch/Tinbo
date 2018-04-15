package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.marker.Addable
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.marker.Editable
import io.gitlab.arturbosch.tinbo.api.marker.Listable
import io.gitlab.arturbosch.tinbo.api.marker.Summarizable
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class CommandChooser @Autowired constructor(tinboContext: TinboContext,
											private val noopCommands: NoopCommands) {

	private val commands by lazy {
		tinboContext.context
				.getBeansOfType(Command::class.java)
				.values
	}

	fun forCurrentMode(): Editable {
		val currentModeId = ModeManager.current.id
		return commands.filterIsInstance<Editable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

	fun forListableMode(): Listable {
		val currentModeId = ModeManager.current.id
		return commands.filterIsInstance<Listable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

	fun forAddableMode(): Addable {
		val currentModeId = ModeManager.current.id
		return commands.filterIsInstance<Addable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

	fun forSummarizableMode(): Summarizable {
		val currentModeId = ModeManager.current.id
		return commands.filterIsInstance<Summarizable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

}
