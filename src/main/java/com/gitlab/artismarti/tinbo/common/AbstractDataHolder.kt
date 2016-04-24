package com.gitlab.artismarti.tinbo.common

/**
 * @author artur
 */
abstract class AbstractDataHolder<E : Entry, D : Data<E>>(var data: D, val persister: AbstractPersister<E, D>) {

	fun loadData(name: String) {
		data = persister.restore(name)
	}

	fun persistEntry(entry: E) {
		data.addEntry(entry)
		persister.store(data)
	}

	fun saveData(name: String, entriesInMemory: List<E>) {
		data = newData(name, entriesInMemory)
		persister.store(data)
	}

	fun getEntries(): List<E> = data.entries.sorted()

	abstract fun newData(name: String, entriesInMemory: List<E>): D

	abstract fun getEntriesFilteredBy(filter: String): List<E>
}
