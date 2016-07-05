package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.config.Mode
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.shell.core.JLineShellComponent
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.shell.support.util.StringUtils
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
			buffer: String?) {

		if (StringUtils.isEmpty(buffer)) {
			val shell = ctx.getBean("shell", JLineShellComponent::class.java)
			val parser = shell.simpleParser
			val commands: List<Command> = parser.commandMarkers.asSequence()
					.filter { it is Command }
					.map { it as Command }
					.toList()

			val currentModeCommands = when (ModeAdvisor.getMode()) {
				Mode.START -> commands.filter { it.id == "start" || it.id == "help" }
				Mode.TIMER -> commands.filter { it.id == "time" || it.id == "edit" || it.id == "share" }
				Mode.NOTES -> commands.filter { it.id == "note" || it.id == "edit" || it.id == "share" }
				Mode.TASKS -> commands.filter { it.id == "task" || it.id == "edit" || it.id == "share" }
				else -> emptyList()
			}

			println(commands)
			println(currentModeCommands)
//			parser
//			parser.obtainHelp("ls add")
		}
		println("its meeee")
	}

	override fun setApplicationContext(applicationContext: ApplicationContext) {
		ctx = applicationContext
	}
}
