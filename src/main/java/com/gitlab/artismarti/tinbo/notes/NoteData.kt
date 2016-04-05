package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.persistence.Data
import com.gitlab.artismarti.tinbo.persistence.Entry

/**
 * @author artur
 */
class NoteData(name: String = Default.NOTES_NAME, entries: List<Entry> = listOf()) : Data(name, entries)
