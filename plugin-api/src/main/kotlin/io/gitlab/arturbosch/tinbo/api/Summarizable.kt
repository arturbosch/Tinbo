package io.gitlab.arturbosch.tinbo.api

/**
 * @author artur
 */
interface Summarizable {
	fun sum(categories: List<String>): String
}