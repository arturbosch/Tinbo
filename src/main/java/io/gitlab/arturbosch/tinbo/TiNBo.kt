package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.config.ModeListener
import io.gitlab.arturbosch.tinbo.config.ModeManager
import io.gitlab.arturbosch.tinbo.config.TinboMode
import io.gitlab.arturbosch.tinbo.providers.PromptProvider
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.shell.support.logging.HandlerUtils
import java.util.logging.Logger

/**
 * @author artur
 */
@SpringBootApplication
open class TiNBo {

	companion object {

		@JvmStatic fun main(args: Array<String>) {
			val ctx = SpringApplication.run(TiNBo::class.java)
			try {
				val bootStrap = io.gitlab.arturbosch.tinbo.BootShim(args, ctx)
				bootStrap.run()
			} catch (e: RuntimeException) {
				throw e
			} finally {
				HandlerUtils.flushAllHandlers(Logger.getLogger(""))
			}
		}

	}

	@Bean
	fun register(prompt: PromptProvider): CommandLineRunner {
		return CommandLineRunner {
			ModeManager.register(object : ModeListener {
				override fun change(mode: TinboMode) {
					prompt.promptText = mode.id
				}
			})
		}
	}

}
