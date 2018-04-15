package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.api.config.TinboMode

/**
 * @author Artur Bosch
 */
object FinanceMode : TinboMode {
	override val id: String = "finance"
	override val helpIds: Array<String> = arrayOf(id, "edit", "share", "mode", "sum")
	override val editAllowed: Boolean = true
	override val isSummarizable: Boolean = true
}
