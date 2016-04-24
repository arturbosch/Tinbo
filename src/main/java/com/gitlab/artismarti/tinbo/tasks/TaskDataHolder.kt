package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.common.AbstractDataHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class TaskDataHolder @Autowired constructor(data: TaskData, persister: TaskPersister) :
		AbstractDataHolder<TaskEntry, TaskData>(data, persister) {

	override fun newData(name: String, entriesInMemory: List<TaskEntry>): TaskData {
		return TaskData(name, entriesInMemory)
	}

	override fun getEntriesFilteredBy(filter: String): List<TaskEntry> {
		return getEntries().filter { it.category == filter }
	}
}
