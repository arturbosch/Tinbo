package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.config.ModeListener
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.providers.PromptProvider
import org.jline.reader.LineReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.shell.result.DefaultResultHandler
import org.springframework.stereotype.Component
import java.util.ServiceLoader

/**
 * @author artur
 */
@SpringBootApplication
open class Tinbo {

	@Bean
	open fun registerModeListeners(prompt: PromptProvider): CommandLineRunner {
		return CommandLineRunner {
			ServiceLoader.load(ModeListener::class.java).forEach { ModeManager.register(it) }
			ModeManager.register(object : ModeListener {
				override fun change(mode: TinboMode) {
					prompt.promptText = mode.id
				}
			})
		}
	}
}

@Component
class TinboTerminal @Autowired constructor(private val reader: LineReader,
										   private val writer: DefaultResultHandler) {

	fun readLine(prompt: String? = null,
				 rightPrompt: String? = null,
				 mask: Char? = null,
				 buffer: String? = null): String {
		return reader.readLine(prompt, rightPrompt, mask, buffer) ?: ""
	}

	fun write(content: String) {
		writer.handleResult(content)
	}
}

fun main(args: Array<String>) {
	SpringApplication.run(Tinbo::class.java, *args)
}
