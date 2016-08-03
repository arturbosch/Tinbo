package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.common.AbstractPersister
import io.gitlab.arturbosch.tinbo.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.config.HomeFolder
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
