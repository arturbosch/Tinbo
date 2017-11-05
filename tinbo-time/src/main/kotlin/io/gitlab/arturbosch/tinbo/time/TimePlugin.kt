package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.plugins.TinboPlugin
import jline.console.ConsoleReader
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TimePlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun registerCommands(tinboContext: TinboContext): List<Command> {
		val consoleReader = tinboContext.beanOf<ConsoleReader>()
		val tinboConfig = tinboContext.tinboConfig
		val persister = TimePersister(tinboConfig)
		val dataHolder = TimeDataHolder(persister, tinboConfig)
		val executor = TimeExecutor(dataHolder, consoleReader, tinboConfig)
		val financeCommands = TimeEditCommands(executor, tinboConfig, consoleReader)
		tinboContext.registerSingleton("TimeEditCommands", financeCommands)

		val financeModeCommand = StartTimeModeCommand()
		tinboContext.registerSingleton("StartTimeModeCommand", financeModeCommand)

		val summaryExecutor = WeekSummaryExecutor(dataHolder)
		val timeSummaryPluginHelper = TimeSummaryPluginSupport(dataHolder)
		val timeSummaryCommands = TimeSummaryCommands(summaryExecutor, timeSummaryPluginHelper)
		tinboContext.registerSingleton("TimeSummaryCommands", timeSummaryCommands)

		val timerCommands = TimerCommands(executor)
		tinboContext.registerSingleton("TimerCommands", timerCommands)

		tinboContext.registerSingleton("TimePersister", persister)
		val timeSummaryPluginSupport = TimeSummaryPluginSupport(dataHolder)
		tinboContext.registerSingleton("TimeSummaryPluginSupport", timeSummaryPluginSupport)
		return listOf(financeCommands, financeModeCommand, timerCommands, timeSummaryCommands)
	}

}
