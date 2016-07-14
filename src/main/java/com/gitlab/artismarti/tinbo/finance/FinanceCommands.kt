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
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.notes.NoteEntry
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.spaceIfEmpty
import com.gitlab.artismarti.tinbo.utils.printlnInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
@Component
class FinanceCommands @Autowired constructor(executor: FinanceExecutor) :
		EditableCommands<FinanceEntry, FinanceData, DummyFinance>(executor), Command {

	@CliAvailabilityIndicator("test")
	fun isAvailable(): Boolean {
		return ModeAdvisor.isFinanceMode()
	}

	@CliCommand("test", help = "...")
	fun test(@CliOption(key = arrayOf("", "test"), mandatory = true,
			specifiedDefaultValue = "", unspecifiedDefaultValue = "") name: String) {
		printlnInfo(name)
	}

	override fun add(): String {
		val category = console.readLine("Enter a category: ").orValue(Defaults.MAIN_CATEGORY_NAME)
		val message = console.readLine("Enter a message: ")
		executor.addEntry(FinanceEntry(category, message))
		return SUCCESS_MESSAGE
	}

	private val SUCCESS_MESSAGE = "Successfully added a finance entry."
	override val id: String = "finance"
}

@Component
class FinanceExecutor @Autowired constructor(financeDataHolder: FinanceDataHolder) :
		AbstractExecutor<FinanceEntry, FinanceData, DummyFinance>(financeDataHolder) {

	override val TABLE_HEADER: String
		get() = "No.;Category;Notice"

	override fun newEntry(index: Int, dummy: DummyFinance): FinanceEntry {
		val entry = entriesInMemory[index]
		return entry.copy(dummy.category, dummy.message)
	}

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
		return getEntries().filter { it.category == filter }
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

class DummyFinance(val category: String, val message: String) : DummyEntry()

class FinanceData(name: String = Defaults.NOTES_NAME,
				  entries: List<FinanceEntry> = listOf()) : Data<FinanceEntry>(name, entries) {
}

class FinanceEntry(val category: String = "", val message: String = "") : Entry() {

	override fun compareTo(other: Entry): Int {
		if (other !is NoteEntry) return 1
		return message.compareTo(other.message)
	}

	override fun toString(): String {
		return "${category.spaceIfEmpty()};${message.spaceIfEmpty()}"
	}

}

fun FinanceEntry.copy(category: String? = null, message: String? = null): FinanceEntry {
	return FinanceEntry(category ?: this.category, message ?: this.message)
}