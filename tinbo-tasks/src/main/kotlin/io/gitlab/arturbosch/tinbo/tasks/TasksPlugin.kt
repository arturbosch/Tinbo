package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import jline.console.ConsoleReader
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TasksPlugin : TiNBoPlugin {

	override fun registerCommands(tinboContext: SpringContext): List<Command> {
		val consoleReader = tinboContext.beanOf<ConsoleReader>()
		val tinboConfig = tinboContext.tinboConfig
		val persister = TaskPersister(tinboConfig)
		val dataHolder = TaskDataHolder(persister, tinboConfig)
		val executor = TaskExecutor(dataHolder, tinboConfig)
		val taskCommands = TaskCommands(executor, tinboConfig, consoleReader)
		tinboContext.registerSingleton("TaskCommands", taskCommands)

		val taskModeCommand = StartTaskModeCommand()
		tinboContext.registerSingleton("StartTaskModeCommand", taskModeCommand)

		return listOf(taskCommands, taskModeCommand)
	}

}