package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author Artur Bosch
 */
interface Categorizable {
	fun categories(): String
	fun changeCategory(oldName: String, newName: String): String
}
