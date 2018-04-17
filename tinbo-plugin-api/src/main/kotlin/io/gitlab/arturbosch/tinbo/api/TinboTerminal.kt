package io.gitlab.arturbosch.tinbo.api

import io.gitlab.arturbosch.tinbo.api.config.EditablePromptProvider
import org.jline.reader.LineReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TinboTerminal @Autowired @Lazy constructor(
		private val reader: LineReader,
		private val promptProvider: EditablePromptProvider) {

	@JvmOverloads
	fun readLine(prompt: String? = null,
				 rightPrompt: String? = null,
				 mask: Char? = null,
				 buffer: String? = null): String {
		return reader.readLine(prompt, rightPrompt, mask, buffer) ?: ""
	}

	fun write(content: String) {
		reader.terminal.writer().println(content)
		reader.terminal.writer().flush()
	}

	fun changePrompt(prompt: String) {
		promptProvider.promptText = prompt
	}
}
