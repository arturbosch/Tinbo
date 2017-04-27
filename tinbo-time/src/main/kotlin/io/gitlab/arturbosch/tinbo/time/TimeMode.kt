package io.gitlab.arturbosch.tinbo.time

import io.gitlab.arturbosch.tinbo.config.TinboMode

/**
 * @author Artur Bosch
 */
object TimeMode : TinboMode {
	override val id: String = "time"
	override val helpIds: Array<String> = arrayOf(id, "edit", "share", "mode", "sum")
	override val editAllowed: Boolean = true
	override val isSummarizable: Boolean = true
}