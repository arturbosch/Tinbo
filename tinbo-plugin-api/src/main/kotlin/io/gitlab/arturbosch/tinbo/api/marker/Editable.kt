package io.gitlab.arturbosch.tinbo.api.marker

/**
 * @author artur
 */
interface Editable : Listable, Addable, Loadable,
		Datable, Cancelable, Deletable, Saveable, Categorizable {
	fun edit(index: Int): String
}
