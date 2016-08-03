package io.gitlab.arturbosch.tinbo

import io.gitlab.arturbosch.tinbo.config.HomeFolder
import io.gitlab.arturbosch.tinbo.config.TinboConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.shell.support.logging.HandlerUtils
import java.util.logging.Logger

/**
 * @author artur
 */
@SpringBootApplication
open class TiNBo {

	companion object {

		val config: TinboConfig = TinboConfig.load(HomeFolder.getOrCreateDefaultConfig())

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

}
