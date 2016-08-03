package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.common.AbstractExecutor
import io.gitlab.arturbosch.tinbo.utils.printlnInfo
import org.joda.money.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.RoundingMode
import java.time.LocalDate

/**
 * @author artur
 */

@Component
class FinanceExecutor @Autowired constructor(val dataHolder: FinanceDataHolder) :
		AbstractExecutor<FinanceEntry, FinanceData, DummyFinance>(dataHolder) {

	override val TABLE_HEADER: String
		get() = "No.;Month;Category;Notice;Spend;Time"

	override fun newEntry(index: Int, dummy: DummyFinance): FinanceEntry {
		val entry = entriesInMemory[index]
		return entry.copy(dummy.month, dummy.category, dummy.message, dummy.moneyValue, dummy.dateTime)
	}

	fun sumCategories(categories: List<String>): String {
		val currentMonth = LocalDate.now().month
		printlnInfo("Summary for current month: $currentMonth")
		val summaries =
				if (categories.isNotEmpty()) {
					dataHolder.getEntries().asSequence()
							.filter { it.month.equals(currentMonth) }
							.filter { categories.contains(it.category) }
							.toSummaryStringList()
				} else {
					dataHolder.getEntries().asSequence()
							.filter { it.month.equals(currentMonth) }
							.toSummaryStringList()
				}
		return tableAsString(summaries, "No.;Category;Spent")
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
							.reduce { money, money2 -> money.plus(money2) }
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
								.reduce { money, money2 -> money.plus(money2) }
					}

					val mean = monthToMoney.values.reduce { money, money2 -> money.plus(money2) }
							.dividedBy(times.toLong(), RoundingMode.DOWN)

					val deviation = Math.sqrt(monthToMoney.values.map { Math.pow((it.minus(mean)).amount.toDouble(), 2.0) }
							.sum()
							.div(times))
					Money.parse(currencyUnit.code + " " + String.format("%.2f", deviation))
				})

		return tableAsString(entries, "No.;Category;Deviation")
	}

	private fun byYear(date: LocalDate, it: FinanceEntry) = it.dateTime.toLocalDate().year.equals(date.year)

}
