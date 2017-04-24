package io.gitlab.arturbosch.tinbo.commands

import io.gitlab.arturbosch.tinbo.api.Addable
import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.api.Editable
import io.gitlab.arturbosch.tinbo.api.Listable
import io.gitlab.arturbosch.tinbo.api.Summarizable
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.providers.StateProvider
import io.gitlab.arturbosch.tinbo.psp.PSPCommands
import io.gitlab.arturbosch.tinbo.psp.ProjectCommands
import io.gitlab.arturbosch.tinbo.psp.ProjectsMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class CommandChooser @Autowired constructor(val commands: List<Command>,
											val pspCommands: PSPCommands,
											val projectCommands: ProjectCommands,
											val noopCommands: NoopCommands,
											val stateProvider: StateProvider) {

	fun forCurrentMode(): Editable {
		val currentModeId = ModeManager.current.id
		return commands.filterIsInstance<Editable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

	fun forListableMode(): Listable {
		return when (ModeManager.current) {
			ProjectsMode -> if (stateProvider.isProjectOpen()) projectCommands else pspCommands
			else -> forCurrentMode()
		}
	}

	fun forAddableMode(): Addable {
		return when (ModeManager.current) {
			ProjectsMode -> if (stateProvider.isProjectOpen()) projectCommands else pspCommands
			else -> forCurrentMode()
		}
	}

	fun forSummarizableMode(): Summarizable {
		val currentModeId = ModeManager.current.id
		return commands.filterIsInstance<Summarizable>()
				.find { (it as Command).id == currentModeId } ?: noopCommands
	}

}
