package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TimePlugin : TinboPlugin() {

	override fun version(): String = "1.0.0"

	override fun registerCommands(tinbo: TinboContext): List<Command> {
		val terminal = tinbo.beanOf<TinboTerminal>()
		val tinboConfig = tinbo.tinboConfig
		val persister = TimePersister(tinboConfig)
		val dataHolder = TimeDataHolder(persister, tinboConfig)
		val executor = TimeExecutor(dataHolder, terminal, tinboConfig)
		val financeCommands = TimeEditCommands(executor, tinboConfig, terminal)
		tinbo.registerSingleton(financeCommands)

		val financeModeCommand = StartTimeModeCommand()
		tinbo.registerSingleton(financeModeCommand)

		val summaryExecutor = WeekSummaryExecutor(dataHolder)
		val timeSummaryPluginHelper = TimeSummaryPluginSupport(dataHolder)
		val timeSummaryCommands = TimeSummaryCommands(summaryExecutor, timeSummaryPluginHelper)
		tinbo.registerSingleton(timeSummaryCommands)

		val timerCommands = TimerCommands(executor)
		tinbo.registerSingleton(timerCommands)

		tinbo.registerSingleton(persister)
		val timeSummaryPluginSupport = TimeSummaryPluginSupport(dataHolder)
		tinbo.registerSingleton(timeSummaryPluginSupport)
		return listOf(financeCommands, financeModeCommand, timerCommands, timeSummaryCommands)
	}

}
