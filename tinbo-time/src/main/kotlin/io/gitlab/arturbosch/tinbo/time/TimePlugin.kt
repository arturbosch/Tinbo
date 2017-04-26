package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import jline.console.ConsoleReader
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TimePlugin : TiNBoPlugin {

	override fun registerCommands(tinboContext: SpringContext): List<Command> {
		val consoleReader = tinboContext.bean<ConsoleReader>()
		val tinboConfig = tinboContext.tinboConfig
		val persister = TimePersister(tinboConfig)
		val dataHolder = TimeDataHolder(persister, tinboConfig)
		val executor = TimeExecutor(dataHolder, consoleReader, tinboConfig)
		val financeCommands = TimeEditCommands(executor, tinboConfig)
		tinboContext.registerSingleton("TimeEditCommands", financeCommands)

		val financeModeCommand = StartTimeModeCommand()
		tinboContext.registerSingleton("StartTimeModeCommand", financeModeCommand)

		val summaryExecutor = WeekSummaryExecutor(dataHolder)
		val timeSummaryPluginHelper = TimeSummaryPluginHelper(dataHolder)
		val timeSummaryCommands = TimeSummaryCommands(summaryExecutor, timeSummaryPluginHelper)
		tinboContext.registerSingleton("TimeSummaryCommands", timeSummaryCommands)

		val timerCommands = TimerCommands(executor)
		tinboContext.registerSingleton("TimerCommands", timerCommands)

		return listOf(financeCommands, financeModeCommand, timerCommands, timeSummaryCommands)
	}

}