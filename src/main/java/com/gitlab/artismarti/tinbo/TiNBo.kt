package com.gitlab.artismarti.tinbo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.shell.CommandLine
import org.springframework.shell.core.JLineShellComponent
import org.springframework.shell.support.logging.HandlerUtils
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import java.util.logging.Logger

/**
 * Full spring shell demo using spring boot for auto-configuration,
 * a custom bootstrap class to work properly with spring boot and
 * written with kotlin language.
 *
 * @author artur
 */
@SpringBootApplication
open class TiNBo {

    companion object : InjektMain() {

        override fun InjektRegistrar.registerInjectables() {
            
        }

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
    }

}
