package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.persistence.Entry
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TaskDataHolder(var data: TaskData = Injekt.get(),
                     val persister: TaskPersister = Injekt.get()) {

    fun loadData(name: String) {
        data = persister.restore(name)
    }

    fun persistEntry(entry: TaskEntry) {
        data.addEntry(entry)
        persister.store(data)
    }

    fun saveData(name: String, entriesInMemory: List<Entry>) {
        data = TaskData(name, entriesInMemory)
        persister.store(data)
    }
}
