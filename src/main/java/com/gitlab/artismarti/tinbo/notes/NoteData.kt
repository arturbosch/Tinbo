package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.persistence.Data

/**
 * @author artur
 */
class NoteData(name: String, entries: List<NoteEntry> = listOf()) : Data(name, entries)
