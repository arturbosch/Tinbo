package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.config.ModeListener
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.providers.PromptProvider
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
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

fun main(args: Array<String>) {
	SpringApplication.run(Tinbo::class.java, *args)
}
