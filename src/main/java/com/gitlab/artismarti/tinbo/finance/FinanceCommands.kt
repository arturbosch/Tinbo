package com.gitlab.artismarti.tinbo.finance

import com.gitlab.artismarti.tinbo.TiNBo
import com.gitlab.artismarti.tinbo.common.AbstractDataHolder
import com.gitlab.artismarti.tinbo.common.AbstractExecutor
import com.gitlab.artismarti.tinbo.common.AbstractPersister
import com.gitlab.artismarti.tinbo.common.Command
import com.gitlab.artismarti.tinbo.common.Data
import com.gitlab.artismarti.tinbo.common.DummyEntry
import com.gitlab.artismarti.tinbo.common.EditableCommands
import com.gitlab.artismarti.tinbo.common.Entry
import com.gitlab.artismarti.tinbo.common.Summarizable
import com.gitlab.artismarti.tinbo.config.CATEGORY_NAME_DEFAULT
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.orDefault
import com.gitlab.artismarti.tinbo.orThrow
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.spaceIfEmpty
import com.gitlab.artismarti.tinbo.toIntOrDefault
import com.gitlab.artismarti.tinbo.utils.dateTimeFormatter
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.math.RoundingMode
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

/**
 * @author artur
 */
@Component
class FinanceCommands @Autowired constructor(val financeExecutor: FinanceExecutor) :
		EditableCommands<FinanceEntry, FinanceData, DummyFinance>(financeExecutor), Command, Summarizable {

	override val id: String = "finance"
	private val SUCCESS_MESSAGE = "Successfully added a finance entry."

	@CliAvailabilityIndicator("test", "loadFinance")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isFinanceMode()
	}

	@CliCommand("loadFinance", help = "Loads/Creates an other data set. Finance data sets are stored under ~/tinbo/finance/*.")
	fun loadTasks(@CliOption(key = arrayOf("", "name"), mandatory = true,
			specifiedDefaultValue = Defaults.FINANCE_NAME,
			unspecifiedDefaultValue = Defaults.FINANCE_NAME) name: String) {
		executor.loadData(name)
	}

	@CliCommand("year", "yearSummary", help = "Sums up the year by providing expenditure per month.")
	fun yearSummary(@CliOption(key = arrayOf("last"), mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
					@CliOption(key = arrayOf(""), mandatory = false,
							specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		val now = LocalDate.now()
		if (year != -1 && (year < 2000 || year > now.year + 20)) {
			return "Entered date must be not too far in the past (> 2000) or in the future ( < now + 20)!"
		}

		val date = if (lastYear) now.minusYears(1) else if (year == -1) now else LocalDate.of(year, 1, 1)
		return financeExecutor.yearSummary(date)
	}

	@CliCommand("mean", help = "Provides the mean expenditure per month for current or specified year.")
	fun means(@CliOption(key = arrayOf("last"), mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
			  @CliOption(key = arrayOf(""), mandatory = false,
					  specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		val now = LocalDate.now()
		if (year != -1 && (year < 2000 || year > now.year + 20)) {
			return "Entered date must be not too far in the past (> 2000) or in the future ( < now + 20)!"
		}

		val date = if (lastYear) now.minusYears(1) else if (year == -1) now else LocalDate.of(year, 1, 1)
		return financeExecutor.yearSummaryMean(date)
	}

	@CliCommand("deviation", help = "Provides the expenditure deviation per month for current or specified year.")
	fun deviation(@CliOption(key = arrayOf("last"), mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
				  @CliOption(key = arrayOf(""), mandatory = false,
						  specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		val now = LocalDate.now()
		if (year != -1 && (year < 2000 || year > now.year + 20)) {
			return "Entered date must be not too far in the past (> 2000) or in the future ( < now + 20)!"
		}

		val date = if (lastYear) now.minusYears(1) else if (year == -1) now else LocalDate.of(year, 1, 1)
		return financeExecutor.yearSummaryDeviation(date)
	}

	override fun sum(categories: List<String>): String {
		return financeExecutor.sumCategories(categories)
	}

	override fun add(): String {
		val month = Month.of(console.readLine("Enter a month as number from 1-12 (empty if this month): ")
				.toIntOrDefault { LocalDate.now().month.value })
		val category = console.readLine("Enter a category: ").orValue(CATEGORY_NAME_DEFAULT)
		val message = console.readLine("Enter a message: ").orEmpty()
		val money = Money.of(currencyUnit, console.readLine("Enter a money value: ").orThrow().toDouble())
		val dateString = console.readLine("Enter a end time (yyyy-MM-dd HH:mm): ")
		val dateTime = if (dateString.isEmpty()) LocalDateTime.now()
		else LocalDateTime.parse(dateString, dateTimeFormatter)

		executor.addEntry(FinanceEntry(month, category, message, money, dateTime))
		return SUCCESS_MESSAGE
	}

}

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

	fun Sequence<FinanceEntry>.toSummaryStringList(): List<String> {
		return this.toSummaryStringList({ it.category }, {
			it.value.map { it.moneyValue }
					.reduce { money, money2 -> money.plus(money2) }
		})
	}

	fun <K> Sequence<FinanceEntry>.toSummaryStringList(
			keySelector: (FinanceEntry) -> K): List<String> {
		return this.toSummaryStringList(keySelector, {
			it.value.map { it.moneyValue }
					.reduce { money, money2 -> money.plus(money2) }
		})
	}

	inline fun <K, V> Sequence<FinanceEntry>.toSummaryStringList(
			keySelector: (FinanceEntry) -> K,
			valueTransformation: (Map.Entry<K, List<FinanceEntry>>) -> V): List<String> {
		return this.groupBy { keySelector.invoke(it) }
				.mapValues {
					valueTransformation.invoke(it)
				}
				.map { "${it.key};${it.value.toString()}" }
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
		printlnInfo("Deviation expenditure per month for year: ${date.year}")
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

@Component
class FinanceDataHolder @Autowired constructor(persister: FinancePersister) :
		AbstractDataHolder<FinanceEntry, FinanceData>(persister) {

	override val last_used_data: String
		get() = TiNBo.config.getKey(ConfigDefaults.FINANCE)
				.getOrElse(ConfigDefaults.LAST_USED, { Defaults.FINANCE_NAME })

	override fun newData(name: String, entriesInMemory: List<FinanceEntry>): FinanceData {
		return FinanceData(name, entriesInMemory)
	}

	override fun getEntriesFilteredBy(filter: String): List<FinanceEntry> {
		return getEntries().filter { it.category.equals(filter, ignoreCase = true) }
	}

	override fun changeCategory(oldName: String, newName: String) {
		val updatedEntries = getEntries().map {
			if (it.category.equals(oldName, ignoreCase = true)) {
				it.copy(category = newName)
			} else {
				it
			}
		}
		saveData(data.name, updatedEntries)
	}

}

@Component
class FinancePersister(FINANCE_PATH: Path = HomeFolder.getDirectory(ConfigDefaults.FINANCE)) :
		AbstractPersister<FinanceEntry, FinanceData>(FINANCE_PATH) {

	override fun restore(name: String): FinanceData {
		return load(name, FinanceData(name), FinanceEntry::class.java)
	}

}

class DummyFinance(val category: String?, val message: String?,
				   val month: Month?, val moneyValue: Money?,
				   val dateTime: LocalDateTime?) : DummyEntry()

class FinanceData(name: String = Defaults.NOTES_NAME,
				  entries: List<FinanceEntry> = listOf()) : Data<FinanceEntry>(name, entries) {
}

class FinanceEntry(val month: Month = Month.JANUARY,
				   val category: String = CATEGORY_NAME_DEFAULT,
				   val message: String = "",
				   val moneyValue: Money = Money.of(currencyUnit, 0.0),
				   val dateTime: LocalDateTime = LocalDateTime.now()) : Entry() {

	override fun compareTo(other: Entry): Int {
		if (other !is FinanceEntry) return 1
		return dateTime.compareTo(other.dateTime)
	}

	override fun toString(): String {
		return "$month;${category.spaceIfEmpty()};${message.spaceIfEmpty()};" +
				"${moneyValue.toString()};${dateTime.format(dateTimeFormatter)}"
	}

}

val currencyUnit: CurrencyUnit = CurrencyUnit.of(TiNBo.config
		.getKey(ConfigDefaults.DEFAULTS)[ConfigDefaults.CURRENCY].orDefault("EUR"))

fun FinanceEntry.copy(month: Month? = null,
					  category: String? = null,
					  message: String? = null,
					  moneyValue: Money? = null,
					  dateTime: LocalDateTime? = null): FinanceEntry {
	return FinanceEntry(month ?: this.month, category ?: this.category, message ?: this.message,
			moneyValue ?: this.moneyValue, dateTime ?: this.dateTime)
}