package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author Artur Bosch
 */
interface Deletable {
	fun delete(indexPattern: String): String
}
