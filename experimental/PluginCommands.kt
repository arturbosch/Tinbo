package com.gitlab.artismarti.tinbo.plugins

import com.gitlab.artismarti.tinbo.common.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.JLineShellComponent
import org.springframework.shell.core.MethodTarget
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class PluginCommands @Autowired constructor(val shell: JLineShellComponent) : Command {
	override val id: String = "start"

	@CliCommand("plugin")
	fun executePlugin(@CliOption(key = arrayOf("", "name"),
			help = "Name of the plugin to execute.") name: String): String {
		val commandMarker = shell.simpleParser.commandMarkers
				.find { it.javaClass.simpleName == name } ?: return "No plugin with this name found."

		for (method in commandMarker.javaClass.methods) {
			val cmd = method.getAnnotation(CliCommand::class.java)
			if (cmd != null) {
				println(cmd)
//				 We have a @CliCommand.
				for (value in cmd.value) {
					println(value)
//					val remainingBuffer = isMatch(buffer, value, strictMatching)
//					if (remainingBuffer != null) {
//						result.add(MethodTarget(method, command, remainingBuffer, value))
//					}
				}
			}
		}
		return "maybe"
	}
}