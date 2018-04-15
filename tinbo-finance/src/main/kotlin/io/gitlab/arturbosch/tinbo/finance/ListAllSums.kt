package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.marker.Command
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import org.joda.money.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.YearMonth

/**
 * @author Artur Bosch
 */
@Component
class ListAllSums @Autowired constructor(private val executor: FinanceExecutor,
										 private val dataHolder: FinanceDataHolder,
										 private val configProvider: ConfigProvider) : Command {

	override val id: String = "finance"

	@CliAvailabilityIndicator("sumAll")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(FinanceMode)
	}

	@CliCommand("sumAll", help = "Sums all monthly expenses for a category and displays them in a table.")
	fun loadTasks(@CliOption(key = arrayOf("", "category"), mandatory = true) category: String?): String {
		if (category == null || category.isEmpty()) return "Enter a category, to list all expenses for all months."

		val entries = dataHolder.getEntriesFilteredBy(category)
				.groupBy { YearMonth.of(it.dateTime.year, it.dateTime.month) }
				.toSortedMap()
				.mapValues {
					it.value.fold(Money.zero(configProvider.currencyUnit)) { acc, entry ->
						acc.plus(entry.moneyValue)
					}
				}
				.map { "${it.key};${it.value}" }

		val result = executor.tableAsString(entries, "No.;Date;Expenses")

		return "Summary data for whole lifetime of $category:\n\n$result"
	}

}
