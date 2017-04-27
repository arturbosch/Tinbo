package io.gitlab.arturbosch.tinbo.providers

import io.gitlab.arturbosch.tinbo.config.EditablePromptProvider
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
open class PromptProvider : EditablePromptProvider {

	override var promptText = "tinbo"

	override fun getProviderName(): String {
		return "TinboPromptProvider"
	}

	override fun getPrompt(): String {
		return "$promptText>"
	}

}
