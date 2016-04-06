package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.Entry

/**
 * @author artur
 */
class NoteEntry(val message: String = "") : Entry() {

    override fun compareTo(other: Entry): Int {
        if (other !is NoteEntry) return 1
        return message.compareTo(other.message)
    }

    override fun toString(): String {
        return "$message"
    }

}
