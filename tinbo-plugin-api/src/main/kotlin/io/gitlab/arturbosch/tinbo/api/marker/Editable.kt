package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author artur
 */
interface Editable : Listable, Addable, Loadable,
		Datable, Cancelable, Deletable, Saveable {
	fun changeCategory(oldName: String, newName: String): String
	fun edit(index: Int): String
}
