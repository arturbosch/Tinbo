package com.gitlab.artismarti.tinbo.common

import com.gitlab.artismarti.tinbo.applyToString
import com.gitlab.artismarti.tinbo.csv.CSVTablePrinter
import com.gitlab.artismarti.tinbo.plusElementAtBeginning
import com.gitlab.artismarti.tinbo.replaceAt
import com.gitlab.artismarti.tinbo.utils.printlnInfo
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

	fun addEntry(entry: E) {
		dataHolder.persistEntry(entry)
		cancel()
	}

	fun loadData(name: String) {
		dataHolder.loadData(name)
	}

	fun listData(): String {
		entriesInMemory = dataHolder.getEntries()
		return listDataInternal()
	}

	fun listInMemoryEntries(): String {
		return listDataInternal()
	}

	private fun listDataInternal(): String {

		val entryTableData = entriesInMemory
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
		if (entriesInMemory.isEmpty()) printlnInfo("")
	}

	fun saveEntries(newName: String = "") {
		var name = dataHolder.data.name
		if (newName.isNotEmpty()) name = newName
		dataHolder.saveData(name, entriesInMemory)
		cancel()
	}

	fun indexExists(index: Int): Boolean {
		return index >= 0 && index < entriesInMemory.size
	}

	/**
	 * Used to set entries in memory to empty list. This has the effect that list commands
	 * load whole new set of data from disk. Cancel is used after save and new entry.
	 */
	fun cancel() {
		entriesInMemory = listOf()
	}

	fun changeCategory(oldName: String, newName: String) {
		dataHolder.changeCategory(oldName, newName)
	}

}

