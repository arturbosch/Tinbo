package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.ModeManager
import org.joda.money.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author Artur Bosch
 */
@Component
class SubscriptionsCommand @Autowired constructor(private val executor: FinanceExecutor,
												  private val configProvider: ConfigProvider) : Command {
	override val id: String = FinanceMode.id

	private val currency = configProvider.currencyUnit

	@CliAvailabilityIndicator("subscribe")
	fun isAvailable() = ModeManager.isCurrentMode(FinanceMode)

	@CliCommand("subscribe", help = "Adds all your subscriptions specified in the config to your finance.")
	fun subscribe() {
		val subscriptions = configProvider.subscriptions
				?: throw IllegalArgumentException("No subscriptions found.")
		for (subscription in subscriptions) {
			val (category, message, money) = subscription.split(",")
			val now = LocalDateTime.now()
			executor.addEntry(
					FinanceEntry(now.month, category, message, Money.of(currency, money.toDouble()), now))
		}
	}
}
