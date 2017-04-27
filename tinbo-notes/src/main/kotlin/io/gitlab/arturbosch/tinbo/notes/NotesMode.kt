package io.gitlab.arturbosch.tinbo.notes

import io.gitlab.arturbosch.tinbo.config.TinboMode

/**
 * @author Artur Bosch
 */
object NotesMode : TinboMode {
	override val id: String = "note"
	override val helpIds: Array<String> = arrayOf(id, "edit", "share", "mode")
	override val editAllowed: Boolean = true
	override val isSummarizable: Boolean = false
}