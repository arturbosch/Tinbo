package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.Entry
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NotesDataHolder(var data: NotesData = Injekt.get(),
                      val persister: NotesPersister = Injekt.get()) {

    fun loadData(name: String) {
        data = persister.restore(name) as NotesData
    }

    fun persistEntry(entry: NoteEntry) {
        data.addEntry(entry)
        persister.store(data)
    }

    fun saveData(name: String, entriesInMemory: List<Entry>) {
        data = NotesData(name, entriesInMemory)
        persister.store(data)
    }
}
