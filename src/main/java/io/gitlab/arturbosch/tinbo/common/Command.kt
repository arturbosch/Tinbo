package io.gitlab.arturbosch.tinbo.common

import org.springframework.shell.core.CommandMarker

/**
 * @author artur
 */
interface Command : CommandMarker {
	val id: String
}
