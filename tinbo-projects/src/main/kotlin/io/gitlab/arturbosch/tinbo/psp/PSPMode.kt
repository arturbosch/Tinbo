package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.config.TinboMode

/**
 * @author Artur Bosch
 */
object PSPMode : TinboMode {
	override val id: String = "psp"
	override val helpIds: Array<String> = arrayOf(id, "projects", "share", "mode")
	override val editAllowed: Boolean = true
	override val isSummarizable: Boolean = false
}