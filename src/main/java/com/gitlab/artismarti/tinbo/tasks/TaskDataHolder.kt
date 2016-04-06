package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.persistence.AbstractDataHolder
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TaskDataHolder(data: TaskData = Injekt.get(), persister: TaskPersister = Injekt.get()) :
        AbstractDataHolder<TaskEntry, TaskData>(data, persister) {

    override fun newData(name: String, entriesInMemory: List<TaskEntry>): TaskData {
        return TaskData(name, entriesInMemory)
    }

    override fun getEntriesFilteredBy(filter: String): List<TaskEntry> {
        return data.entries.filter { it.category == filter }
    }
}
