package com.gitlab.artismarti.tinbo.tasks

import com.gitlab.artismarti.tinbo.applyToString
import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.csv.CSVTablePrinter
import com.gitlab.artismarti.tinbo.orValue
import com.gitlab.artismarti.tinbo.plusElementAtBeginning
import com.gitlab.artismarti.tinbo.replaceAt
import com.gitlab.artismarti.tinbo.withIndexedColumn
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * @author artur
 */
class TaskExecutor(val taskDataHolder: TaskDataHolder = Injekt.get()) {

    private val csv = CSVTablePrinter()
    private var entriesInMemory: List<TaskEntry> = listOf()

    init {
        taskDataHolder.loadData(Default.TASKS_NAME)
    }

    fun addNote(taskEntry: TaskEntry) {
        taskDataHolder.persistEntry(taskEntry)
    }

    fun loadData(name: String) {
        taskDataHolder.loadData(name)
    }

    fun listData(): String {
        entriesInMemory = taskDataHolder.data.entries

        val entryTableData = entriesInMemory.applyToString()
                .withIndexedColumn()
                .plusElementAtBeginning("No.;Category;Message;Location;Start;End;Description")

        return csv.asTable(entryTableData).joinToString("\n")
    }

    fun editNote(index: Int, dummy: DummyTask) {
        val editedNote = createEditedNote(index, dummy)
        entriesInMemory = entriesInMemory.replaceAt(index, editedNote)
    }

    private fun createEditedNote(index: Int, dummy: DummyTask): TaskEntry {
        val realNote = entriesInMemory[index]
        return TaskEntry(dummy.message.orValue(realNote.message),
                dummy.description.orValue(realNote.description),
                dummy.location.orValue(realNote.location),
                dummy.category.orValue(realNote.category),
                dummy.startTime.orValue(realNote.startTime),
                dummy.endTime.orValue(realNote.endTime))
    }

    fun deleteNotes(indices: Set<Int>) {
        entriesInMemory = entriesInMemory.filterIndexed { index, entry -> indices.contains(index).not() }
    }

    fun save(newName: String = ""): String {
        var name = taskDataHolder.data.name
        if (newName.isNotEmpty()) name = newName
        taskDataHolder.saveData(name, entriesInMemory)
        entriesInMemory = listOf()
        return "Successfully saved edited data"
    }

    fun indexExists(index: Int): Boolean {
        return index >= 0 && index < entriesInMemory.size
    }

}
