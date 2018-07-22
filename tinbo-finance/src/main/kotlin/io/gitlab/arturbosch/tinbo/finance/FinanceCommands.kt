package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.TinboTerminal
import io.gitlab.arturbosch.tinbo.api.commands.EditableCommands
import io.gitlab.arturbosch.tinbo.api.config.Defaults
import io.gitlab.arturbosch.tinbo.api.config.ModeManager
import io.gitlab.arturbosch.tinbo.api.marker.Summarizable
import io.gitlab.arturbosch.tinbo.api.nullIfEmpty
import io.gitlab.arturbosch.tinbo.api.orDefaultMonth
import io.gitlab.arturbosch.tinbo.api.orThrow
import io.gitlab.arturbosch.tinbo.api.orValue
import io.gitlab.arturbosch.tinbo.api.utils.dateFormatter
import io.gitlab.arturbosch.tinbo.api.utils.dateTimeFormatter
import org.joda.money.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeParseException

/**
 * @author artur
 */
@Component
class FinanceCommands @Autowired constructor(private val financeExecutor: FinanceExecutor,
											 private val configProvider: ConfigProvider,
											 terminal: TinboTerminal) :
		EditableCommands<FinanceEntry, FinanceData, DummyFinance>(financeExecutor, terminal), Summarizable {

	override val id: String = FinanceMode.id
	private val successMessage = "Successfully added a finance entry."

	@CliAvailabilityIndicator("year", "mean", "deviation", "loadFinance")
	fun isAvailable(): Boolean {
		return ModeManager.isCurrentMode(FinanceMode)
	}

	@CliCommand("loadFinance", help = "Loads/Creates an other data set. Finance data sets are stored under ~/tinbo/finance/*.")
	fun loadTasks(@CliOption(key = ["", "name"], mandatory = true,
			specifiedDefaultValue = Defaults.FINANCE_NAME,
			unspecifiedDefaultValue = Defaults.FINANCE_NAME) name: String) {
		executor.loadData(name)
	}

	@CliCommand("year", "yearSummary", help = "Sums up the year by providing expenditure per month.")
	fun yearSummary(@CliOption(key = ["last"], mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
					@CliOption(key = [""], mandatory = false,
							specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		return withValidDate(year, lastYear) {
			financeExecutor.yearSummary(it)
		}
	}

	@CliCommand("mean", help = "Provides the mean expenditure per month for current or specified year.")
	fun means(@CliOption(key = ["last"], mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
			  @CliOption(key = [""], mandatory = false,
					  specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		return withValidDate(year, lastYear) {
			financeExecutor.yearSummaryMean(it)
		}
	}

	@CliCommand("deviation", help = "Provides the expenditure deviation per month for current or specified year.")
	fun deviation(@CliOption(key = ["last"], mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
				  @CliOption(key = [""], mandatory = false,
						  specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		return withValidDate(year, lastYear) {
			financeExecutor.yearSummaryDeviation(it)
		}
	}

	private fun withValidDate(year: Int, lastYear: Boolean, block: (LocalDate) -> String): String {
		val now = LocalDate.now()
		if (year != -1 && (year < 2000 || year > now.year + 20)) {
			return "Entered date must be not too far in the past (> 2000) or in the future ( < now + 20)!"
		}

		val date = if (lastYear) now.minusYears(1) else if (year == -1) now else LocalDate.of(year, 1, 1)
		return block.invoke(date)
	}

	override fun sum(categories: Set<String>, categoryFilters: Set<String>): String =
			financeExecutor.sumCategories(categories, categoryFilters)

	override fun add(): String {
		return whileNotInEditMode {
			val month = Month.of(console.readLine("Enter a month as number from 1-12 (empty if this month): ").orDefaultMonth())
			val category = console.readLine("Enter a category: ").orValue(configProvider.categoryName)
			val message = console.readLine("Enter a message: ")
			val money = Money.of(configProvider.currencyUnit, console.readLine("Enter a money value: ").orThrow().toDouble())
			val dateString = console.readLine("Enter a end time (yyyy-MM-dd HH:mm): ")
			val dateTime = parseDateTime(dateString)

			executor.addEntry(FinanceEntry(month, category, message, money, dateTime))
			successMessage
		}
	}

	private fun parseDateTime(dateString: String): LocalDateTime {
		return if (dateString.isEmpty()) {
			LocalDateTime.now()
		} else {
			try {
				LocalDateTime.parse(dateString, dateTimeFormatter)
			} catch (ex: DateTimeParseException) {
				LocalDate.parse(dateString, dateFormatter).atTime(0, 0)
			}
		}
	}

	override fun edit(index: Int): String {
		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val monthString = console.readLine("Enter a month as number from 1-12 (empty if this month) (leave empty if unchanged): ")
				val month = if (monthString.isEmpty()) null else Month.of(monthString.orDefaultMonth())
				val category = console.readLine("Enter a category (leave empty if unchanged): ").nullIfEmpty()
				val message = console.readLine("Enter a message (leave empty if unchanged): ").nullIfEmpty()
				val moneyString = console.readLine("Enter a money value (leave empty if unchanged): ")
				val money = if (moneyString.isEmpty()) null else Money.of(configProvider.currencyUnit, moneyString.toDouble())
				val dateString = console.readLine("Enter a end time (yyyy-MM-dd HH:mm) (leave empty if unchanged): ")
				val dateTime = parseDateTime(dateString)
				executor.editEntry(i, DummyFinance(category, message, month, money, dateTime))
				"Successfully edited a finance entry."
			}
		}
	}
}
