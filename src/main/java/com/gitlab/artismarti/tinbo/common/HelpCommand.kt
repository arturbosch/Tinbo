package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.config.Mode
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.shell.core.JLineShellComponent
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class HelpCommand : Command, ApplicationContextAware {

	override val id: String = "share"

	private lateinit var ctx: ApplicationContext

	@CliCommand(value = "help", help = "List all commands usage")
	fun obtainHelp(
			@CliOption(key = arrayOf("", "command"), optionContext = "disable-string-converter availableCommands", help = "Command name to provide help for")
			buffer: String?): String {

		val shell = ctx.getBean("shell", JLineShellComponent::class.java)
		val parser = shell.simpleParser

		val commands: List<Command> = parser.commandMarkers.asSequence()
				.filter { it is Command }
				.map { it as Command }
				.toList()

		val currentModeCommands = when (ModeAdvisor.getMode()) {
			Mode.START -> commands.filter { it.id == "start" || it.id == "help" || it.id == "share" }
			Mode.TIMER -> commands.filter { it.id == "time" || it.id == "edit" || it.id == "share" || it.id == "mode" }
			Mode.NOTES -> commands.filter { it.id == "note" || it.id == "edit" || it.id == "share" || it.id == "mode" }
			Mode.TASKS -> commands.filter { it.id == "task" || it.id == "edit" || it.id == "share" || it.id == "mode" }
			Mode.FINANCE -> commands.filter { it.id == "finance" || it.id == "edit" || it.id == "share" || it.id == "mode" }
			else -> emptyList()
		}

		return HelpParser(currentModeCommands).obtainHelp(buffer)
	}

	override fun setApplicationContext(applicationContext: ApplicationContext) {
		ctx = applicationContext
	}
}
