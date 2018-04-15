package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.api.config.TinboConfig
import io.gitlab.arturbosch.tinbo.api.model.AbstractExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class NoteExecutor @Autowired constructor(noteDataHolder: NoteDataHolder,
											   tinboConfig: TinboConfig) :
		AbstractExecutor<NoteEntry, NoteData, DummyNote>(noteDataHolder, tinboConfig) {

	override val TABLE_HEADER: String
		get() = "No.;Message"

	override fun newEntry(index: Int, dummy: DummyNote): NoteEntry {
		val entry = entriesInMemory[index]
		return entry.copy(dummy.message)
	}

}

