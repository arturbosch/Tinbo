package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.plugins.TinboContext
import io.gitlab.arturbosch.tinbo.api.plugins.TinboPlugin
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class FinancePlugin : TinboPlugin() {

	override fun version(): String = "1.0.4"

	override fun registerCommands(tinbo: TinboContext): List<Command> {
		val terminal = tinbo.beanOf<TinboTerminal>()
		val tinboConfig = tinbo.tinboConfig
		val configProvider = ConfigProvider(tinboConfig)
		val persister = FinancePersister(tinboConfig)
		val dataHolder = FinanceDataHolder(persister, tinboConfig)
		val executor = FinanceExecutor(dataHolder, configProvider, tinboConfig)
		val financeCommands = FinanceCommands(executor, configProvider, terminal)
		tinbo.registerSingleton(financeCommands)

		val financeModeCommand = StartFinanceModeCommand()
		tinbo.registerSingleton(financeModeCommand)

		val listAllSums = ListAllSums(executor, dataHolder, configProvider)
		tinbo.registerSingleton(listAllSums)
		tinbo.registerSingleton(persister)
		val subscriptionsCommand = SubscriptionsCommand(executor, configProvider)
		tinbo.registerSingleton(subscriptionsCommand)
		return listOf(financeCommands, financeModeCommand, listAllSums, subscriptionsCommand)
	}

}
