package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import io.gitlab.arturbosch.tinbo.config.Mode
import io.gitlab.arturbosch.tinbo.config.ModeAdvisor
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
			@CliOption(key = arrayOf("", "command"), optionContext = "disable-string-converter availableCommands",
					help = "Command name to provide help for")
			buffer: String?): String {

		val shell = ctx.getBean("shell", JLineShellComponent::class.java)
		val parser = shell.simpleParser

		val commands: List<Command> = parser.commandMarkers.asSequence()
				.filter { it is Command }
				.map { it as Command }
				.toList()

		val currentModeCommands = when (ModeAdvisor.getMode()) {
			Mode.START -> commands.filter {
				when (it.id) {
					"start", "help", "share" -> true
					else -> false
				}
			}
			Mode.TIMER -> commands.filter {
				when (it.id) {
					"time", "edit", "share", "mode", "sum" -> true
					else -> false
				}
			}
			Mode.NOTES -> commands.filter {
				when (it.id) {
					"note", "edit", "share", "mode" -> true
					else -> false
				}
			}
			Mode.TASKS -> commands.filter {
				when (it.id) {
					"task", "edit", "share", "mode" -> true
					else -> false
				}
			}
			Mode.FINANCE -> commands.filter {
				when (it.id) {
					"finance", "edit", "share", "mode", "sum" -> true
					else -> false
				}
			}
			else -> emptyList()
		}

		return io.gitlab.arturbosch.tinbo.common.HelpParser(currentModeCommands).obtainHelp(buffer)
	}

	override fun setApplicationContext(applicationContext: ApplicationContext) {
		ctx = applicationContext
	}
}
