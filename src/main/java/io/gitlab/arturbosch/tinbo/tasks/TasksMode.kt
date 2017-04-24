package io.gitlab.arturbosch.tinbo.tasks

import io.gitlab.arturbosch.tinbo.config.TinboMode

/**
 * @author Artur Bosch
 */
object TasksMode : TinboMode {
	override val id: String = "tasks"
	override val editAllowed: Boolean = true
	override val isSummarizable: Boolean = false
}