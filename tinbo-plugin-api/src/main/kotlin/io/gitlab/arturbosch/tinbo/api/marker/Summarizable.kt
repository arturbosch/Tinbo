package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author artur
 */
interface Summarizable {
	fun sum(categories: List<String>): String
}
