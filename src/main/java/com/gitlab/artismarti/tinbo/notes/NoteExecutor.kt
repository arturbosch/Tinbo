package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.persistence.AbstractExecutor
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NoteExecutor(val noteDataHolder: NoteDataHolder = Injekt.get()) :
        AbstractExecutor<NoteEntry, NoteData, DummyNote>(noteDataHolder) {

    override val TABLE_HEADER: String
        get() = "No.;Message"

    init {
        noteDataHolder.loadData(Default.TASKS_NAME)
    }

    override fun newEntry(index: Int, dummy: DummyNote): NoteEntry {
        return NoteEntry(dummy.message)
    }

}
