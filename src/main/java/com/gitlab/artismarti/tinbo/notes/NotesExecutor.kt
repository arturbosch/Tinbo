package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.applyToString
import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.csv.CSVTablePrinter
import com.gitlab.artismarti.tinbo.persistence.Entry
import com.gitlab.artismarti.tinbo.plusElementAtBeginning
import com.gitlab.artismarti.tinbo.withIndexedColumn
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class NotesExecutor(val notesDataHolder: NotesDataHolder = Injekt.get()) {

    private val csv = CSVTablePrinter()
    private var entriesInMemory: List<Entry> = listOf()

    init {
        notesDataHolder.loadData(Default.NOTES_NAME)
    }

    fun addNote(noteEntry: NoteEntry) {
        notesDataHolder.persistEntry(noteEntry)
    }

    fun loadData(name: String) {
        notesDataHolder.loadData(name)
    }

    fun listData(): String {
        entriesInMemory = notesDataHolder.data.entries

        val entryTableData = entriesInMemory.applyToString()
                .withIndexedColumn()
                .plusElementAtBeginning("No.;Category;Message;Location;Start;End;Description")

        return csv.asTable(entryTableData).joinToString("\n")
    }

}
