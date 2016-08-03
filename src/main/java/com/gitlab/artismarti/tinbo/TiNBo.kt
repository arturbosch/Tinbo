package com.gitlab.artismarti.tinbo

import com.gitlab.artismarti.tinbo.config.HomeFolder
import com.gitlab.artismarti.tinbo.config.TinboConfig
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
				val bootStrap = BootShim(args, ctx)
				bootStrap.run()
			} catch (e: RuntimeException) {
				throw e
			} finally {
				HandlerUtils.flushAllHandlers(Logger.getLogger(""))
			}
		}

	}

}
