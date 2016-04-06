package com.gitlab.artismarti.tinbo

import com.gitlab.artismarti.tinbo.notes.NoteData
import com.gitlab.artismarti.tinbo.notes.NoteDataHolder
import com.gitlab.artismarti.tinbo.notes.NoteExecutor
import com.gitlab.artismarti.tinbo.notes.NotePersister
import com.gitlab.artismarti.tinbo.tasks.TaskData
import com.gitlab.artismarti.tinbo.tasks.TaskDataHolder
import com.gitlab.artismarti.tinbo.tasks.TaskExecutor
import com.gitlab.artismarti.tinbo.tasks.TaskPersister
import com.gitlab.artismarti.tinbo.timer.TimeData
import com.gitlab.artismarti.tinbo.timer.TimeDataHolder
import com.gitlab.artismarti.tinbo.timer.TimeExecutor
import com.gitlab.artismarti.tinbo.timer.TimePersister
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
            addSingleton(fullType(), TimeData())
            addSingleton(fullType(), TimePersister())
            addSingleton(fullType(), TimeDataHolder())
            addSingleton(fullType(), TimeExecutor())
            addSingleton(fullType(), TaskData())
            addSingleton(fullType(), TaskPersister())
            addSingleton(fullType(), TaskDataHolder())
            addSingleton(fullType(), TaskExecutor())
            addSingleton(fullType(), NoteData())
            addSingleton(fullType(), NotePersister())
            addSingleton(fullType(), NoteDataHolder())
            addSingleton(fullType(), NoteExecutor())
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
