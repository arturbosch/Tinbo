package com.gitlab.artismarti.tinbo

import jline.console.ConsoleReader
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.shell.CommandLine
import org.springframework.shell.core.JLineShellComponent
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
				val bootStrap = BootShim(args, ctx)
				bootStrap.run()
			} catch (e: RuntimeException) {
				throw e
			} finally {
				HandlerUtils.flushAllHandlers(Logger.getLogger(""))
			}
		}

		@Bean
		fun shell(): JLineShellComponent {
			return JLineShellComponent()
		}

		@Bean
		fun commandLine(): CommandLine {
			return CommandLine(null, 3000, null)
		}

		@Bean
		fun console(): ConsoleReader {
			return ConsoleReader()
		}
	}

}
