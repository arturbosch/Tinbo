package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.common.Entry
import com.gitlab.artismarti.tinbo.spaceIfEmpty

/**
 * @author artur
 */
class NoteEntry(val message: String = "") : Entry() {

	override fun compareTo(other: Entry): Int {
		return 1
	}

	override fun toString(): String {
		return "${message.spaceIfEmpty()}"
	}

	fun copy(message: String?): NoteEntry {
		return NoteEntry(message ?: this.message)
	}
}
