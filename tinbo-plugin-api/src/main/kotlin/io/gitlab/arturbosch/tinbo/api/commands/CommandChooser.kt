package io.gitlab.arturbosch.tinbo.api.commands

import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Addable
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.marker.Editable
import io.gitlab.arturbosch.tinbo.api.marker.Listable
import io.gitlab.arturbosch.tinbo.api.marker.Summarizable
import io.gitlab.arturbosch.tinbo.api.plugins.PluginSupport
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class CommandChooser @Autowired constructor(tinboContext: TinboContext,
											val noopCommands: NoopCommands) {

	val commands by lazy {
		tinboContext.context
				.getBeansOfType(Command::class.java)
				.values
	}

	fun forCurrentMode(): Editable = forModeWithInterface()

	fun forListableMode(): Listable = forModeWithInterface()

	fun forAddableMode(): Addable = forModeWithInterface()

	fun forSummarizableMode(): Summarizable = forModeWithInterface()
	
	@PluginSupport
	inline fun <reified T> forModeWithInterface(): T {
		val currentModeId = ModeManager.current.id
		return commands.filterIsInstance<T>()
				.find { (it as Command).id == currentModeId } ?: noopCommands as T
	}
}
