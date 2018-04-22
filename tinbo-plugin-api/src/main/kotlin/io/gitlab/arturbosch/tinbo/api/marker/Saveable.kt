package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author Artur Bosch
 */
interface Saveable {
	fun save(name: String): String
}
