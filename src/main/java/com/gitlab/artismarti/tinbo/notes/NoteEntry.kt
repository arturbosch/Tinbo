package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.common.Entry

/**
 * @author artur
 */
class NoteEntry(val message: String = "") : Entry() {

	override fun compareTo(other: Entry): Int {
		return 1
	}

	override fun toString(): String {
		return "$message"
	}

}
