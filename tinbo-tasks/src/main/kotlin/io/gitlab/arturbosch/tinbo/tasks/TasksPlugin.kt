package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import jline.console.ConsoleReader
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TasksPlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun registerCommands(tinboContext: TinboContext): List<Command> {
		val consoleReader = tinboContext.beanOf<ConsoleReader>()
		val tinboConfig = tinboContext.tinboConfig
		val persister = TaskPersister(tinboConfig)
		val dataHolder = TaskDataHolder(persister, tinboConfig)
		val executor = TaskExecutor(dataHolder, tinboConfig)
		val taskCommands = TaskCommands(executor, tinboConfig, consoleReader)
		tinboContext.registerSingleton("TaskCommands", taskCommands)

		val taskModeCommand = StartTaskModeCommand()
		tinboContext.registerSingleton("StartTaskModeCommand", taskModeCommand)

		tinboContext.registerSingleton("TasksPersister", persister)
		return listOf(taskCommands, taskModeCommand)
	}

}
