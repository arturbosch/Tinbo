package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.common.AbstractDataHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class TaskDataHolder @Autowired constructor(data: TaskData, persister: TaskPersister) :
		AbstractDataHolder<TaskEntry, TaskData>(data, persister) {

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
