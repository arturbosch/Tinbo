package io.gitlab.arturbosch.tinbo.api

import org.jline.reader.LineReader
import org.jline.terminal.Terminal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

/**
 * @author Artur Bosch
 */
@Component
class TinboTerminal @Autowired @Lazy constructor(private val reader: LineReader,
												 private val writer: Terminal) {

	@JvmOverloads
	fun readLine(prompt: String? = null,
				 rightPrompt: String? = null,
				 mask: Char? = null,
				 buffer: String? = null): String {
		return reader.readLine(prompt, rightPrompt, mask, buffer) ?: ""
	}

	fun write(content: String) {
		writer.writer().println(content)
		writer.writer().flush()
	}
}
