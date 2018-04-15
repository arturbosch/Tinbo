package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.api.model.Entry
import io.gitlab.arturbosch.tinbo.api.spaceIfEmpty

/**
 * @author artur
 */
class NoteEntry(private val message: String = "") : Entry() {

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
