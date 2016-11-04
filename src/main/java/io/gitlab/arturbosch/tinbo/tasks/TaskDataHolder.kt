package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.config.Defaults
import io.gitlab.arturbosch.tinbo.config.TinboConfig
import io.gitlab.arturbosch.tinbo.model.AbstractDataHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TaskDataHolder @Autowired constructor(persister: TaskPersister,
												 val config: TinboConfig) :
		AbstractDataHolder<TaskEntry, TaskData>(persister) {

	override val last_used_data: String
		get() = config.getKey(ConfigDefaults.TASKS)
				.getOrElse(ConfigDefaults.LAST_USED, { Defaults.TASKS_NAME })


	override fun newData(name: String, entriesInMemory: List<TaskEntry>): TaskData {
		return TaskData(name, entriesInMemory)
	}

	override fun getEntriesFilteredBy(filter: String): List<TaskEntry> {
		return getEntries().filter { it.category == filter }
	}

	override fun changeCategory(oldName: String, newName: String) {
		val updatedEntries = getEntries().map {
			if (it.category.equals(oldName, ignoreCase = true)) {
				TaskEntry(it.message, it.description, it.location, newName, it.startTime, it.endTime)
			} else {
				it
			}
		}
		saveData(data.name, updatedEntries)
	}
}
