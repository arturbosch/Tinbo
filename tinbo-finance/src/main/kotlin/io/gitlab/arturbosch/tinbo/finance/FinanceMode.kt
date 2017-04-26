package io.gitlab.arturbosch.tinbo.finance

import io.gitlab.arturbosch.tinbo.config.TinboMode

/**
 * @author Artur Bosch
 */
object FinanceMode : TinboMode {
	override val id: String = "finance"
	override val editAllowed: Boolean = true
	override val isSummarizable: Boolean = true
}