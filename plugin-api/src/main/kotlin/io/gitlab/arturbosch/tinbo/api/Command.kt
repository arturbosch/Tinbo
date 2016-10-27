package io.gitlab.arturbosch.tinbo.api

import org.springframework.shell.core.CommandMarker

/**
 * All command providers must implement this interface.
 * The id property is used to decide if this commands are printed in the help command.
 *
 * @author Artur Bosch
 */
interface Command : CommandMarker {
	val id: String
}
