package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.DataHolder
import com.gitlab.artismarti.tinbo.persistence.Entry
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NoteDataHolder(var data: NoteData = Injekt.get(), val persister: NotePersister = Injekt.get()) : DataHolder {
    
    override fun loadData(name: String) {
        data = persister.restore(name) as NoteData
    }

    override fun persistEntry(entry: Entry) {
        data.addEntry(entry)
        persister.store(data)
    }

    override fun saveData(name: String, entriesInMemory: List<Entry>) {
        data = NoteData(name, entriesInMemory)
        persister.store(data)
    }

}
