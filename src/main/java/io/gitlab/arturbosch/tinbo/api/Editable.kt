package io.gitlab.arturbosch.tinbo.api

/**
 * @author artur
 */
interface Editable {
	fun add(): String
	fun list(categoryName: String): String
	fun load(name: String): String
	fun cancel(): String
	fun save(name: String): String
	fun delete(indexPattern: String): String
	fun changeCategory(oldName: String, newName: String): String
	fun data(): String
	fun edit(index: Int): String
}
