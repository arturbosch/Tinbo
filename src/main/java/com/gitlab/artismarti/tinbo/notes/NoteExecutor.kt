package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.applyToString
import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.csv.CSVTablePrinter
import com.gitlab.artismarti.tinbo.plusElementAtBeginning
import com.gitlab.artismarti.tinbo.replaceAt
import com.gitlab.artismarti.tinbo.withIndexedColumn
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NoteExecutor(val noteDataHolder: NoteDataHolder = Injekt.get()) {

    private val csv = CSVTablePrinter()
    private var entriesInMemory: List<NoteEntry> = listOf()

    init {
        noteDataHolder.loadData(Default.TASKS_NAME)
    }

    fun addNote(noteEntry: NoteEntry) {
        noteDataHolder.persistEntry(noteEntry)
    }

    fun loadData(name: String) {
        noteDataHolder.loadData(name)
    }

    fun listData(): String {
        entriesInMemory = noteDataHolder.data.entries

        val entryTableData = entriesInMemory.applyToString()
                .withIndexedColumn()
                .plusElementAtBeginning("No.;Message")

        return csv.asTable(entryTableData).joinToString("\n")
    }

    fun editNote(index: Int, newMessage: String) {
        entriesInMemory = entriesInMemory.replaceAt(index, NoteEntry(newMessage))
    }

    fun deleteNotes(indices: Set<Int>) {
        entriesInMemory = entriesInMemory.filterIndexed { index, entry -> indices.contains(index).not() }
    }

    fun save(newName: String = ""): String {
        var name = noteDataHolder.data.name
        if (newName.isNotEmpty()) name = newName
        noteDataHolder.saveData(name, entriesInMemory)
        entriesInMemory = listOf()
        return "Successfully saved edited data"
    }

    fun indexExists(index: Int): Boolean {
        return index >= 0 && index < entriesInMemory.size
    }

}
