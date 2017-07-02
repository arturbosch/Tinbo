package io.gitlab.arturbosch.tinbo.common

import io.gitlab.arturbosch.tinbo.api.Command
import org.springframework.shell.core.ExitShellRequest
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.stereotype.Component

/**
 * @author artur
 */
@Component
class ExitCommand : Command {

	override val id: String = "share"

	@CliCommand("exit", "quit", help = "Exits the shell")
	fun quit(): ExitShellRequest {
		return ExitShellRequest.NORMAL_EXIT
	}

}
