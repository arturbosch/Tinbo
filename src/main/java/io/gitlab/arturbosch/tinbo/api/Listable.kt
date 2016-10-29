package io.gitlab.arturbosch.tinbo.api

/**
 * @author Artur Bosch
 */
interface Listable {
	fun list(categoryName: String): String
}