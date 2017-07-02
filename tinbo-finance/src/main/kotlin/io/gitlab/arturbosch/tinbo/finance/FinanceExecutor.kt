package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.config.TinboConfig
import io.gitlab.arturbosch.tinbo.ifNotEmpty
import io.gitlab.arturbosch.tinbo.model.AbstractExecutor
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.joda.money.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.RoundingMode
import java.time.LocalDate
import java.time.Month

/**
 * @author Artur Bosch
 */

@Component
class FinanceExecutor @Autowired constructor(val dataHolder: FinanceDataHolder,
											 val configProvider: ConfigProvider,
											 tinboConfig: TinboConfig) :
		AbstractExecutor<FinanceEntry, FinanceData, DummyFinance>(dataHolder, tinboConfig) {

	override val TABLE_HEADER: String
		get() = "No.;Month;Category;Notice;Spend;Time"

	override fun newEntry(index: Int, dummy: DummyFinance): FinanceEntry {
		val entry = entriesInMemory[index]
		return entry.copy(dummy.month, dummy.category, dummy.message, dummy.moneyValue, dummy.dateTime)
	}

	fun sumCategories(categories: List<String>): String {
		val (currentMonth, currentYear) = LocalDate.now().run { month to year }
		var summariesReturnString = ""

		val beforeLastMonth = currentMonth.minus(2)
		summaryForMonth(categories, beforeLastMonth, currentYear).ifNotEmpty {
			val financeSequence = this.asSequence()
			val summaryStringList = financeSequence.toSummaryStringList()
			summariesReturnString += tableAsString(summaryStringList, "No.;Category;Spent") +
					"\n\nTotal sum: ${financeSequence.sum()} for month $beforeLastMonth" + "\n\n"
		}

		val lastMonth = currentMonth.minus(1)
		summaryForMonth(categories, lastMonth, currentYear).ifNotEmpty {
			val financeSequence = this.asSequence()
			val summaryStringList = financeSequence.toSummaryStringList()
			summariesReturnString += tableAsString(summaryStringList, "No.;Category;Spent") +
					"\n\nTotal sum: ${financeSequence.sum()} for month $lastMonth" + "\n\n"
		}

		val summaryCurrent = summaryForMonth(categories, currentMonth, currentYear).asSequence()
		summariesReturnString += tableAsString(summaryCurrent.toSummaryStringList(), "No.;Category;Spent")
		return summariesReturnString + "\n\nTotal sum: ${summaryCurrent.sum()} for month $currentMonth"
	}

	private fun Sequence<FinanceEntry>.sum() = this.map { it.moneyValue }
			.fold(Money.zero(configProvider.currencyUnit), Money::plus)

	private fun summaryForMonth(categories: List<String>, currentMonth: Month, currentYear: Int): List<FinanceEntry> {
		return if (categories.isNotEmpty()) {
			dataHolder.getEntries().asSequence()
					.filter { it.month == currentMonth }
					.filter { it.dateTime.year == currentYear }
					.filter { categories.contains(it.category) }
					.toList()
		} else {
			dataHolder.getEntries().asSequence()
					.filter { it.month == currentMonth }
					.filter { it.dateTime.year == currentYear }
					.toList()
		}
	}

	fun yearSummary(date: LocalDate): String {
		printlnInfo("Summary for year: ${date.year}")
		val entries = dataHolder.getEntries().asSequence()
				.filter { byYear(date, it) }
		return tableAsString(entries.toSummaryStringList { it.month }, "No.;Month;Spent")
	}

	fun yearSummaryMean(date: LocalDate): String {
		printlnInfo("Mean expenditure per month for year: ${date.year}")
		val entries = dataHolder.getEntries().asSequence()
				.filter { byYear(date, it) }
				.toSummaryStringList({ it.category }, {
					val value = it.value
					val times = value.groupBy { it.month }.keys.size
					val money = value.map { it.moneyValue }
							.reduce(Money::plus)
					money.dividedBy(times.toLong(), RoundingMode.DOWN)
				})

		return tableAsString(entries, "No.;Category;Mean")
	}

	fun yearSummaryDeviation(date: LocalDate): String {
		printlnInfo("Expenditure deviation per month for year: ${date.year}")
		val entries = dataHolder.getEntries().asSequence()
				.filter { byYear(date, it) }
				.toSummaryStringList({ it.category }, {
					val monthToFinances = it.value.groupBy { it.month }
					val times = monthToFinances.keys.size
					val monthToMoney = monthToFinances.mapValues {
						it.value.map { it.moneyValue }
								.reduce(Money::plus)
					}

					val mean = monthToMoney.values.reduce(Money::plus)
							.dividedBy(times.toLong(), RoundingMode.DOWN)

					val deviation = Math.sqrt(monthToMoney.values.map { Math.pow((it.minus(mean)).amount.toDouble(), 2.0) }
							.sum()
							.div(times))
					Money.parse(configProvider.currencyUnit.code + " " + String.format("%.2f", deviation))
				})

		return tableAsString(entries, "No.;Category;Deviation")
	}

	private fun byYear(date: LocalDate, it: FinanceEntry) = it.dateTime.toLocalDate().year == date.year

}
