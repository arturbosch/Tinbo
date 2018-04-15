package io.gitlab.arturbosch.tinbo.config

import org.springframework.shell.jline.PromptProvider

/**
 * @author Artur Bosch
 */
interface EditablePromptProvider : PromptProvider {
	var promptText: String
}
