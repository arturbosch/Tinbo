package com.gitlab.artismarti.tinbo.persistence

/**
 * @author artur
 */
abstract class Data<E : Entry>(var name: String,
							   var entries: List<E>) {

	fun addEntry(entry: E) {
		entries = entries.plus(entry)
	}

	override fun toString(): String {
		return "Data(name='$name', entries=$entries)"
	}

}
