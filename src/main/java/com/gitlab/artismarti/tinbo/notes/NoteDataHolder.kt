package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.TiNBo
import com.gitlab.artismarti.tinbo.common.AbstractDataHolder
import com.gitlab.artismarti.tinbo.config.ConfigDefaults
import com.gitlab.artismarti.tinbo.config.Defaults
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
