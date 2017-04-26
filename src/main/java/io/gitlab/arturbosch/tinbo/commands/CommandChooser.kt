package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Addable
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.Editable
import io.gitlab.arturbosch.tinbo.api.Listable
import io.gitlab.arturbosch.tinbo.api.Summarizable
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class CommandChooser @Autowired constructor(springContext: SpringContext,
											val noopCommands: NoopCommands) {

	private var _commands = lazy {
		springContext.context
				.getBeansOfType(Command::class.java)
				.values.toList()
	}

	private fun commands(): List<Command> = _commands.value

	fun forCurrentMode(): Editable {
		val currentModeId = ModeManager.current.id
		return commands().filterIsInstance<Editable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

	fun forListableMode(): Listable {
		val currentModeId = ModeManager.current.id
		return commands().filterIsInstance<Listable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

	fun forAddableMode(): Addable {
		val currentModeId = ModeManager.current.id
		return commands().filterIsInstance<Addable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

	fun forSummarizableMode(): Summarizable {
		val currentModeId = ModeManager.current.id
		return commands().filterIsInstance<Summarizable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

}
