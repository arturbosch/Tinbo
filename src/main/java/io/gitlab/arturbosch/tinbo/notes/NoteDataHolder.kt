package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.TiNBo
import io.gitlab.arturbosch.tinbo.model.AbstractDataHolder
import io.gitlab.arturbosch.tinbo.config.ConfigDefaults
import io.gitlab.arturbosch.tinbo.config.Defaults
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class NoteDataHolder @Autowired constructor(persister: NotePersister) :
		AbstractDataHolder<NoteEntry, NoteData>(persister) {

	override val last_used_data: String
		get() = TiNBo.config.getKey(ConfigDefaults.NOTES)
				.getOrElse(ConfigDefaults.LAST_USED, { Defaults.NOTES_NAME })

	override fun newData(name: String, entriesInMemory: List<NoteEntry>): NoteData {
		return NoteData(name, entriesInMemory)
	}

	override fun getEntriesFilteredBy(filter: String): List<NoteEntry> {
		return getEntries()
	}

	override fun changeCategory(oldName: String, newName: String) {
		throw UnsupportedOperationException()
	}
}
