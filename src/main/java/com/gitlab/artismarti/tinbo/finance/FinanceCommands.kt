package com.gitlab.artismarti.tinbo.finance

import com.gitlab.artismarti.tinbo.common.Command
import com.gitlab.artismarti.tinbo.common.EditableCommands
import com.gitlab.artismarti.tinbo.common.Summarizable
import com.gitlab.artismarti.tinbo.config.CATEGORY_NAME_DEFAULT
import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.orDefaultMonth
import com.gitlab.artismarti.tinbo.orThrow
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.utils.dateTimeFormatter
import org.joda.money.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
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

	@CliAvailabilityIndicator("year", "mean", "deviation", "loadFinance")
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

		return withValidDate(year, lastYear) {
			financeExecutor.yearSummary(it)
		}
	}

	@CliCommand("mean", help = "Provides the mean expenditure per month for current or specified year.")
	fun means(@CliOption(key = arrayOf("last"), mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
			  @CliOption(key = arrayOf(""), mandatory = false,
					  specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		return withValidDate(year, lastYear) {
			financeExecutor.yearSummaryMean(it)
		}
	}

	@CliCommand("deviation", help = "Provides the expenditure deviation per month for current or specified year.")
	fun deviation(@CliOption(key = arrayOf("last"), mandatory = false,
			specifiedDefaultValue = "true", unspecifiedDefaultValue = "false") lastYear: Boolean,
				  @CliOption(key = arrayOf(""), mandatory = false,
						  specifiedDefaultValue = "-1", unspecifiedDefaultValue = "-1") year: Int): String {

		return withValidDate(year, lastYear) {
			financeExecutor.yearSummaryDeviation(it)
		}
	}

	fun withValidDate(year: Int, lastYear: Boolean, block: (LocalDate) -> String): String {
		val now = LocalDate.now()
		if (year != -1 && (year < 2000 || year > now.year + 20)) {
			return "Entered date must be not too far in the past (> 2000) or in the future ( < now + 20)!"
		}

		val date = if (lastYear) now.minusYears(1) else if (year == -1) now else LocalDate.of(year, 1, 1)
		return block.invoke(date)
	}

	override fun sum(categories: List<String>): String {
		return financeExecutor.sumCategories(categories)
	}

	override fun add(): String {
		val month = Month.of(console.readLine("Enter a month as number from 1-12 (empty if this month): ").orDefaultMonth())

		val category = console.readLine("Enter a category: ").orValue(CATEGORY_NAME_DEFAULT)
		val message = console.readLine("Enter a message: ").orEmpty()
		val money = Money.of(currencyUnit, console.readLine("Enter a money value: ").orThrow().toDouble())
		val dateString = console.readLine("Enter a end time (yyyy-MM-dd HH:mm): ")
		val dateTime = if (dateString.isEmpty()) LocalDateTime.now()
		else LocalDateTime.parse(dateString, dateTimeFormatter)

		executor.addEntry(FinanceEntry(month, category, message, money, dateTime))
		return SUCCESS_MESSAGE
	}

	override fun edit(index: Int): String {
		return withinListMode {
			val i = index - 1
			enterEditModeWithIndex(i) {
				val monthString = console.readLine("Enter a month as number from 1-12 (empty if this month) (leave empty if unchanged): ")
				val month = if (monthString.isNullOrEmpty()) null else Month.of(monthString.orDefaultMonth())
				val category = console.readLine("Enter a category (leave empty if unchanged): ")
				val message = console.readLine("Enter a message (leave empty if unchanged): ")
				val moneyString = console.readLine("Enter a money value (leave empty if unchanged): ")
				val money =  if (moneyString.isNullOrEmpty()) null else Money.of(currencyUnit, moneyString.toDouble())
				val dateString = console.readLine("Enter a end time (yyyy-MM-dd HH:mm) (leave empty if unchanged): ")
				val dateTime = if (dateString.isNullOrEmpty()) null else LocalDateTime.parse(dateString, dateTimeFormatter)
				executor.editEntry(i, DummyFinance(message, category, month, money, dateTime))
				"Successfully edited a finance entry."
			}
		}
	}
}
