package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.common.AbstractPersister
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.HomeFolder
import org.springframework.stereotype.Component
import java.nio.file.Path

/**
 * @author artur
 */
@Component
open class NotePersister(NOTES_PATH: Path = HomeFolder.getDirectory(ConfigDefaults.NOTES)) :
		AbstractPersister<NoteEntry, NoteData>(NOTES_PATH) {

	override fun restore(name: String): NoteData {
		return load(name, NoteData(name), NoteEntry::class.java)
	}

}
