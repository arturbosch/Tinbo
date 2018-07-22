package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author artur
 */
interface Summarizable {
	fun sum(categories: Set<String>, categoryFilters: Set<String>): String
}
