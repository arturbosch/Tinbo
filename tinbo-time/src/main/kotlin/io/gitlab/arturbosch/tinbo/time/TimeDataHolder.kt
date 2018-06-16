package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.api.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.api.config.Defaults
import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import io.gitlab.arturbosch.tinbo.api.model.AbstractDataHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TimeDataHolder @Autowired constructor(persister: TimePersister,
												 private val config: TinboConfig) :
		AbstractDataHolder<TimeEntry, TimeData>(persister) {

	override val lastUsedData: String
		get() = config.getKey(ConfigDefaults.TIMERS)
				.getOrElse(ConfigDefaults.LAST_USED) { Defaults.TIME_NAME }

	override fun newData(name: String, entriesInMemory: List<TimeEntry>): TimeData {
		return TimeData(name, entriesInMemory)
	}

	override fun getEntriesFilteredBy(filter: String): List<TimeEntry> {
		return getEntries().filter { it.category == filter }
	}

	override fun changeCategory(oldName: String, newName: String) {
		val updatedEntries = getEntries().map {
			if (it.category.equals(oldName, ignoreCase = true)) {
				TimeEntry(newName, it.message, it.hours, it.minutes, it.seconds, it.date)
			} else {
				it
			}
		}
		saveData(data.name, updatedEntries)
	}
}
