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

    fun addNote(entry: E) {
        dataHolder.persistEntry(entry)
    }

    fun loadData(name: String) {
        dataHolder.loadData(name)
    }

    fun listData(): String {
        entriesInMemory = dataHolder.data.entries

        val entryTableData = entriesInMemory.applyToString()
                .withIndexedColumn()
                .plusElementAtBeginning(TABLE_HEADER)

        return csv.asTable(entryTableData).joinToString(NEW_LINE)
    }

    fun editNote(index: Int, dummy: T) {
        entriesInMemory = entriesInMemory.replaceAt(index, newEntry(index, dummy))
    }

    protected abstract fun newEntry(index: Int, dummy: T): E

    fun deleteNotes(indices: Set<Int>) {
        entriesInMemory = entriesInMemory.filterIndexed { index, entry -> indices.contains(index).not() }
    }

    fun save(newName: String = ""): String {
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

