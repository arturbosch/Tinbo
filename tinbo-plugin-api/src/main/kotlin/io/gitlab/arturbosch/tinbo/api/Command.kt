package io.gitlab.arturbosch.tinbo.api

import org.springframework.shell.core.CommandMarker

/**
 * All command providers must implement this interface.
 *
 * @author Artur Bosch
 */
interface Command : CommandMarker {
	/**
	 * The id is used to decide which commands are printed when using 'help'. This depends on your entered 'mode',
	 * so basically the id must be equals to one of the 'helpId's' of a mode.
	 */
	val id: String
}
