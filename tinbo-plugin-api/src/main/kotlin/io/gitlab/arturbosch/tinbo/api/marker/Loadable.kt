package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author Artur Bosch
 */
interface Loadable {
	fun load(name: String): String
}
