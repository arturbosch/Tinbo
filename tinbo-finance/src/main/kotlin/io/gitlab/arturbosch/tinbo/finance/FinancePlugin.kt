package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.plugins.SpringContext
import io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
import jline.console.ConsoleReader
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class FinancePlugin : TiNBoPlugin {

	override fun registerCommands(tinboContext: SpringContext): List<Command> {
		val consoleReader = tinboContext.beanOf<ConsoleReader>()
		val tinboConfig = tinboContext.tinboConfig
		val configProvider = ConfigProvider(tinboConfig)
		val persister = FinancePersister(tinboConfig)
		val dataHolder = FinanceDataHolder(persister, tinboConfig)
		val executor = FinanceExecutor(dataHolder, configProvider, tinboConfig)
		val financeCommands = FinanceCommands(executor, configProvider, consoleReader)
		tinboContext.registerSingleton("FinanceCommands", financeCommands)

		val financeModeCommand = StartFinanceModeCommand()
		tinboContext.registerSingleton("StartFinanceModeCommand", financeModeCommand)

		tinboContext.registerSingleton("FinancePersister", persister)
		return listOf(financeCommands, financeModeCommand)
	}

}