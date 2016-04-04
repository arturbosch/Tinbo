package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.DataHolder
import com.gitlab.artismarti.tinbo.persistence.Entry
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NoteDataHolder(val data: NoteData = Injekt.get(), val persister: NotePersister = Injekt.get()) : DataHolder {
    
    override fun loadData(name: String) {
        throw UnsupportedOperationException()
    }

    override fun persistEntry(entry: Entry) {
        throw UnsupportedOperationException()
    }

    override fun saveData(name: String, entriesInMemory: List<Entry>) {
        throw UnsupportedOperationException()
    }

}