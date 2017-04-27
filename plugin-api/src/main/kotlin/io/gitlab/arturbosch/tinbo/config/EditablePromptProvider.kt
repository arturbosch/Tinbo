package io.gitlab.arturbosch.tinbo.config

import org.springframework.shell.plugin.PromptProvider

/**
 * @author Artur Bosch
 */
interface EditablePromptProvider : PromptProvider {
	var promptText: String
}