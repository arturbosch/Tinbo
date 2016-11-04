package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.config.Defaults
import io.gitlab.arturbosch.tinbo.model.Data
import io.gitlab.arturbosch.tinbo.model.DummyEntry
import io.gitlab.arturbosch.tinbo.model.Entry
import io.gitlab.arturbosch.tinbo.spaceIfEmpty
import io.gitlab.arturbosch.tinbo.utils.dateTimeFormatter
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.time.LocalDateTime
import java.time.Month

/**
 * @author artur
 */

class DummyFinance(val category: String?, val message: String?,
				   val month: Month?, val moneyValue: Money?,
				   val dateTime: LocalDateTime?) : DummyEntry()

class FinanceData(name: String = Defaults.NOTES_NAME,
				  entries: List<FinanceEntry> = listOf()) : Data<FinanceEntry>(name, entries)

class FinanceEntry(val month: Month = Month.JANUARY,
				   val category: String = "Main",
				   val message: String = "",
				   val moneyValue: Money = Money.of(CurrencyUnit.of("EUR"), 0.0),
				   val dateTime: LocalDateTime = LocalDateTime.now()) : Entry() {

	override fun compareTo(other: Entry): Int {
		if (other !is FinanceEntry) return 1
		return dateTime.compareTo(other.dateTime)
	}

	override fun toString(): String {
		return "$month;${category.spaceIfEmpty()};${message.spaceIfEmpty()};" +
				"$moneyValue;${dateTime.format(dateTimeFormatter)}"
	}

}

fun FinanceEntry.copy(month: Month? = null,
					  category: String? = null,
					  message: String? = null,
					  moneyValue: Money? = null,
					  dateTime: LocalDateTime? = null): FinanceEntry {
	return FinanceEntry(month ?: this.month, category ?: this.category, message ?: this.message,
			moneyValue ?: this.moneyValue, dateTime ?: this.dateTime)
}