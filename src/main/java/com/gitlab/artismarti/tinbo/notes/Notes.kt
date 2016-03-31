package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.Data

/**
 * @author artur
 */
class Notes(name: String, entries: List<Note>) : Data(name, entries) {
}
