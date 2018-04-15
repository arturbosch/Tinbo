package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author artur
 */
interface Editable : Listable, Addable {
	fun load(name: String): String
	fun cancel(): String
	fun save(name: String): String
	fun delete(indexPattern: String): String
	fun changeCategory(oldName: String, newName: String): String
	fun data(): String
	fun edit(index: Int): String
}
