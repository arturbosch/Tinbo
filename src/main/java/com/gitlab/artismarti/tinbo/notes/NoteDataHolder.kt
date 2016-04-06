package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.AbstractDataHolder
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NoteDataHolder(data: NoteData = Injekt.get(), persister: NotePersister = Injekt.get()) :
        AbstractDataHolder<NoteEntry, NoteData>(data, persister) {

    override fun newData(name: String, entriesInMemory: List<NoteEntry>): NoteData {
        return NoteData(name, entriesInMemory)
    }
}
