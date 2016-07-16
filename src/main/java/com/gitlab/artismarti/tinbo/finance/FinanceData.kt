package com.gitlab.artismarti.tinbo.finance

import com.gitlab.artismarti.tinbo.TiNBo
import com.gitlab.artismarti.tinbo.common.AbstractDataHolder
import com.gitlab.artismarti.tinbo.common.AbstractPersister
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.config.HomeFolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
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
