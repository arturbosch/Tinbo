package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.model.Entry
import io.gitlab.arturbosch.tinbo.spaceIfEmpty
import io.gitlab.arturbosch.tinbo.orSpace

/**
 * @author artur
 */
class NoteEntry(val message: String = "") : Entry() {

	override fun compareTo(other: Entry): Int {
		return 1
	}

	override fun toString(): String {
		return message.spaceIfEmpty()
	}

	fun copy(message: String?): NoteEntry {
		return NoteEntry(message ?: this.message)
	}
}
