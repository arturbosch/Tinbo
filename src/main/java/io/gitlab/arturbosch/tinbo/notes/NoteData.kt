package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.model.Data
import io.gitlab.arturbosch.tinbo.config.Defaults
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
open class NoteData(name: String = Defaults.NOTES_NAME,
					entries: List<NoteEntry> = listOf()) : Data<NoteEntry>(name, entries)
