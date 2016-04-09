package com.gitlab.artismarti.tinbo.persistence

import com.gitlab.artismarti.tinbo.applyToString
import com.gitlab.artismarti.tinbo.csv.CSVTablePrinter
import com.gitlab.artismarti.tinbo.plusElementAtBeginning
import com.gitlab.artismarti.tinbo.replaceAt
import com.gitlab.artismarti.tinbo.withIndexedColumn

/**
 * @author artur
 */
abstract class AbstractExecutor<E : Entry, D : Data<E>, T : DummyEntry>(
		private val dataHolder: AbstractDataHolder<E, D>) {

	protected val csv = CSVTablePrinter()
	protected var entriesInMemory: List<E> = listOf()

	protected abstract val TABLE_HEADER: String

	private val NEW_LINE = "\n"
	private val SUCCESS_SAVE = "Successfully saved edited data"

	fun addEntry(entry: E) {
		dataHolder.persistEntry(entry)
	}

	fun loadData(name: String) {
		dataHolder.loadData(name)
	}

	fun listData(): String {
		entriesInMemory = dataHolder.getEntries()
		return listDataInternal()
	}

	private fun listDataInternal(): String {

		val entryTableData = entriesInMemory.sorted()
				.applyToString()
				.withIndexedColumn()
				.plusElementAtBeginning(TABLE_HEADER)

		return csv.asTable(entryTableData).joinToString(NEW_LINE)
	}

	fun listDataFilteredBy(filter: String): String {
		entriesInMemory = dataHolder.getEntriesFilteredBy(filter)
		return listDataInternal()
	}

	fun editEntry(index: Int, dummy: T) {
		entriesInMemory = entriesInMemory.replaceAt(index, newEntry(index, dummy))
	}

	protected abstract fun newEntry(index: Int, dummy: T): E

	fun deleteEntries(indices: Set<Int>) {
		entriesInMemory = entriesInMemory.filterIndexed {
			index, entry ->
			indices.contains(index).not()
		}
	}

	fun saveEntries(newName: String = ""): String {
		var name = dataHolder.data.name
		if (newName.isNotEmpty()) name = newName
		dataHolder.saveData(name, entriesInMemory)
		entriesInMemory = listOf()
		return SUCCESS_SAVE
	}

	fun indexExists(index: Int): Boolean {
		return index >= 0 && index < entriesInMemory.size
	}

}

