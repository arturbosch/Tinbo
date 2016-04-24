package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.common.AbstractDataHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class NoteDataHolder @Autowired constructor(data: NoteData, persister: NotePersister) :
		AbstractDataHolder<NoteEntry, NoteData>(data, persister) {

	override fun newData(name: String, entriesInMemory: List<NoteEntry>): NoteData {
		return NoteData(name, entriesInMemory)
	}

	override fun getEntriesFilteredBy(filter: String): List<NoteEntry> {
		return getEntries()
	}
}
