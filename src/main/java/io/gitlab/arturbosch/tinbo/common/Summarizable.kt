package io.gitlab.arturbosch.tinbo.common

/**
 * @author artur
 */
interface Summarizable {
	fun sum(categories: List<String>): String
}