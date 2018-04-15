package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author Artur Bosch
 */
interface Listable {
	fun list(categoryName: String, all: Boolean): String
}
