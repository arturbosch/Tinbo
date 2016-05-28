package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.Defaults
import com.gitlab.artismarti.tinbo.common.Data
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class NoteData(name: String = Defaults.NOTES_NAME,
                    entries: List<NoteEntry> = listOf()) : Data<NoteEntry>(name, entries)
