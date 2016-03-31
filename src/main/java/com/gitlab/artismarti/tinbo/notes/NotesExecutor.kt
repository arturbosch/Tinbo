package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.Default
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NotesExecutor(val notesDataHolder: NotesDataHolder = Injekt.get()) {

    init {
        notesDataHolder.loadData(Default.NOTES_NAME)
    }

}
