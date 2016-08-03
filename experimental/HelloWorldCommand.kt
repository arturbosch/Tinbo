package com.gitlab.artismarti.tinbo.plugins

import com.gitlab.artismarti.tinbo.common.Command
import org.springframework.shell.core.annotation.CliCommand

/**
 * @author artur
 */
class HelloWorldCommand : Command {

	override val id: String = "start"

	@CliCommand("hello")
	fun hello(): String {
		return "Hello World!"
	}
}