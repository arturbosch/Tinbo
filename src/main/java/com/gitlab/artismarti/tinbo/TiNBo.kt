package com.gitlab.artismarti.tinbo

import com.gitlab.artismarti.tinbo.timer.TimerData
import com.gitlab.artismarti.tinbo.timer.TimerDataHolder
import com.gitlab.artismarti.tinbo.timer.TimerExecutor
import com.gitlab.artismarti.tinbo.timer.TimerPersister
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.shell.CommandLine
import org.springframework.shell.core.JLineShellComponent
import org.springframework.shell.support.logging.HandlerUtils
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.fullType
import java.util.logging.Logger

/**
 * @author artur
 */
@SpringBootApplication
open class TiNBo {

    companion object : InjektMain() {

        override fun InjektRegistrar.registerInjectables() {
            addSingleton(fullType(), TimerData())
            addSingleton(fullType(), TimerPersister())
            addSingleton(fullType(), TimerDataHolder())
            addSingleton(fullType(), TimerExecutor())
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
