package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TasksPlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun providesMode() = TasksMode

	override fun registerCommands(tinbo: TinboContext): List<Command> {
		val terminal = tinbo.beanOf<TinboTerminal>()
		val tinboConfig = tinbo.config
		val persister = TaskPersister(tinboConfig)
		val dataHolder = TaskDataHolder(persister, tinboConfig)
		val executor = TaskExecutor(dataHolder, tinboConfig)
		val taskCommands = TaskCommands(executor, tinboConfig, terminal)
		tinbo.registerSingleton(taskCommands)

		val taskModeCommand = StartTaskModeCommand()
		tinbo.registerSingleton(taskModeCommand)

		tinbo.registerSingleton(persister)
		return listOf(taskCommands, taskModeCommand)
	}

}
