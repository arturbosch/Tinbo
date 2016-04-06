package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.persistence.AbstractPersister
import java.nio.file.Path

/**
 * @author artur
 */
class NotePersister(NOTES_PATH: Path = HomeFolder.getDirectory("notes")) :
        AbstractPersister<NoteEntry, NoteData>(NOTES_PATH) {

    override fun restore(name: String): NoteData {
        return save(name, NoteData(name), NoteEntry::class.java)
    }

}
