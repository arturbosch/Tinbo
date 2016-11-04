package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.config.TinboConfig
import io.gitlab.arturbosch.tinbo.model.AbstractPersister
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class NotePersister @Autowired constructor(config: TinboConfig) :
		AbstractPersister<NoteEntry, NoteData>(HomeFolder.getDirectory(ConfigDefaults.NOTES), config) {

	override fun restore(name: String): NoteData {
		return load(name, NoteData(name), NoteEntry::class.java)
	}

}
