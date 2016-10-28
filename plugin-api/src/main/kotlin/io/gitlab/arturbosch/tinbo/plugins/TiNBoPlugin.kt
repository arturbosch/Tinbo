package io.gitlab.arturbosch.tinbo.plugins

import io.gitlab.arturbosch.tinbo.api.Command

/**
 * All Tinbo plugins must implement this interface.
 * This interface pre defines the id used for all plugins.
 *
 * Declaring your implementations of this interface into a
 * META-INF/services/io.gitlab.arturbosch.tinbo.plugins.TiNBoPlugin
 * file is needed to get your plugins loaded at startup.
 *
 * @author Artur Bosch
 */
interface TiNBoPlugin : Command {
	override val id: String
		get() = "plugins"

	fun context() = ContextAware.context

	object ContextAware {
		var context: SpringContext? = null
	}
}