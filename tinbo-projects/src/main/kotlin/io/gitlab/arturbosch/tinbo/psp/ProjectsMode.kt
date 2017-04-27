package io.gitlab.arturbosch.tinbo.psp

import io.gitlab.arturbosch.tinbo.config.TinboMode

/**
 * @author Artur Bosch
 */
object ProjectsMode : TinboMode {
	override val id: String = "projects"
	override val editAllowed: Boolean = false
	override val isSummarizable: Boolean = false
}