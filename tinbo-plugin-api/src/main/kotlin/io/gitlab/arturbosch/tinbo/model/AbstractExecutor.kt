package io.gitlab.arturbosch.tinbo.model

import io.gitlab.arturbosch.tinbo.applyToString
import io.gitlab.arturbosch.tinbo.config.TinboConfig
import io.gitlab.arturbosch.tinbo.plusElementAtBeginning
import io.gitlab.arturbosch.tinbo.replaceAt
import io.gitlab.arturbosch.tinbo.withIndexedColumn

/**
 * @author Artur Bosch
 */
abstract class AbstractExecutor<E : Entry, D : Data<E>, in T : DummyEntry>(
		private val dataHolder: AbstractDataHolder<E, D>,
		private val tinboConfig: TinboConfig) : CSVAwareExecutor() {

	protected var entriesInMemory: List<E> = listOf()

	private val NEW_LINE = "\n"

	fun addEntry(entry: E) {
		dataHolder.persistEntry(entry)
		cancel()
	}

	fun loadData(name: String) {
		dataHolder.loadData(name)
	}

	fun listData(all: Boolean): String {
		entriesInMemory = dataHolder.getEntries()
		return listDataInternal(all)
	}

	fun listInMemoryEntries(all: Boolean): String {
		return listDataInternal(all)
	}

	private fun listDataInternal(all: Boolean): String {

		var entryTableData = entriesInMemory
				.applyToString()
				.withIndexedColumn()

		if (!all) {
			val amount = tinboConfig.getListAmount()
			entryTableData = entryTableData.takeLast(amount)
		}

		entryTableData = entryTableData.plusElementAtBeginning(TABLE_HEADER)

		return csv.asTable(entryTableData).joinToString(NEW_LINE)
	}

	fun listDataFilteredBy(filter: String, all: Boolean): String {
		entriesInMemory = dataHolder.getEntriesFilteredBy(filter)
		return listDataInternal(all)
	}

	fun editEntry(index: Int, dummy: T) {
		entriesInMemory = entriesInMemory.replaceAt(index, newEntry(index, dummy))
	}

	protected abstract fun newEntry(index: Int, dummy: T): E

	fun deleteEntries(indices: Set<Int>) {
		entriesInMemory = when {
			isSpecialCaseToDeleteLast(indices) -> entriesInMemory.dropLast(1)
			else -> entriesInMemory.filterIndexed { index, _ ->
				indices.contains(index).not()
			}
		}
	}

	private fun isSpecialCaseToDeleteLast(indices: Set<Int>) = indices.size == 1 && indices.first() == -1

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

	fun getAllDataNames(): List<String> {
		return dataHolder.getDataNames()
	}

}

