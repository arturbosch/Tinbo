package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.common.AbstractExecutor
import com.gitlab.artismarti.tinbo.config.Default
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class NoteExecutor @Autowired constructor(val noteDataHolder: NoteDataHolder) :
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
