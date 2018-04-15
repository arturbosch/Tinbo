package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import io.gitlab.arturbosch.tinbo.api.orDefault
import org.joda.money.CurrencyUnit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class ConfigProvider @Autowired constructor(config: TinboConfig) {

	val currencyUnit: CurrencyUnit = CurrencyUnit.of(
			config.getKey(ConfigDefaults.DEFAULTS)[ConfigDefaults.CURRENCY].orDefault("EUR"))

	val categoryName = config.getCategoryName()

	@Suppress("UNCHECKED_CAST")
	val subscriptions by lazy { config.getKeySafe(FinanceMode.id)["subscriptions"] as? List<String> }
}
