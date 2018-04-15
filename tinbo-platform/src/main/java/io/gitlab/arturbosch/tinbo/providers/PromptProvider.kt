package io.gitlab.arturbosch.tinbo.providers

import io.gitlab.arturbosch.tinbo.api.config.EditablePromptProvider
import org.jline.utils.AttributedString
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
class PromptProvider : EditablePromptProvider {

	override var promptText = "tinbo"

	override fun getPrompt(): AttributedString {
		return AttributedString("$promptText>")
	}
}
