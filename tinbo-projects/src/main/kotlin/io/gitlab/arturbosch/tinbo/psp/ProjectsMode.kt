package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.config.TinboMode

/**
 * @author Artur Bosch
 */
object ProjectsMode : TinboMode {
	override val id: String = "projects"
	override val helpIds: Array<String> = arrayOf(PSPMode.id, "share", "mode")
	override val editAllowed: Boolean = false
	override val isSummarizable: Boolean = false
}