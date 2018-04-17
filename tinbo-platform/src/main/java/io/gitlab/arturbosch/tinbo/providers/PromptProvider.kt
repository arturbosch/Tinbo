package io.gitlab.arturbosch.tinbo.providers

import com.google.common.eventbus.Subscribe
import io.gitlab.arturbosch.tinbo.PromptChangeEvent
import io.gitlab.arturbosch.tinbo.api.config.EditablePromptProvider
import io.gitlab.arturbosch.tinbo.api.registerForEventBus
import org.jline.utils.AttributedString
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
@Order(org.springframework.core.Ordered.HIGHEST_PRECEDENCE)
class PromptProvider : EditablePromptProvider {

	init {
		registerForEventBus()
	}

	@Subscribe
	fun handlePromptChange(event: PromptChangeEvent) {
		promptText = event.newName
	}

	override var promptText = "tinbo"

	override fun getPrompt(): AttributedString {
		return AttributedString("$promptText>")
	}
}
