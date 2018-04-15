package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.api.config.TinboMode

/**
 * @author Artur Bosch
 */
object ProjectsMode : TinboMode {
	override val id: String = "projects"
	override val helpIds: Array<String> = arrayOf(id, "share", "mode")
	override val editAllowed: Boolean = true
	override val isSummarizable: Boolean = false
}
