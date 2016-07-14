package com.gitlab.artismarti.tinbo.common

import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class ClearCommand : Command {

	override val id: String = "share"

	@CliCommand(value = *arrayOf("clear"), help = "Clears the console")
	fun clear() {
		AnsiConsole.out().print(ansi().eraseScreen().cursor(0, 0))
	}

}
