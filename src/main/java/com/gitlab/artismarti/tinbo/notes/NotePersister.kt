package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.common.AbstractPersister
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
@Component
class NotePersister(NOTES_PATH: Path = HomeFolder.getDirectory("notes")) :
		AbstractPersister<NoteEntry, NoteData>(NOTES_PATH) {

	override fun restore(name: String): NoteData {
		return save(name, NoteData(name), NoteEntry::class.java)
	}

}
