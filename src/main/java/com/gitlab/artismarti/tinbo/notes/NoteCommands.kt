package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.HashSet

/**
 * @author artur
 */
@Component
class NoteCommands(val executor: NoteExecutor = Injekt.get()) : CommandMarker {

    private val NEED_EDIT_MODE_TEXT = "Before adding or list tasks exit edit mode with 'save' or 'cancel'."
    private val SUCCESS_MESSAGE = "Successfully added a task."

    private var isListMode: Boolean = false
    private var isEditMode: Boolean = false

    @CliAvailabilityIndicator("note", "loadNotes", "editNotes", "listNotes", "deleteNote", "saveNotes", "cancelNotes")
    fun isAvailable(): Boolean {
        return ModeAdvisor.isNotesMode()
    }

    @CliCommand(value = "note", help = "Adds a new note.")
    fun newNote(@CliOption(key = arrayOf("message", "msg", "m"), mandatory = true, help = "Summary of the task.",
            specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String): String {

        var result = SUCCESS_MESSAGE

        if (isEditMode) {
            result = NEED_EDIT_MODE_TEXT
        } else if (message.isEmpty()) {
            result = "You need to specify a message."
        } else {
            executor.addNote(NoteEntry(message))
        }

        return result
    }

    @CliCommand("loadNotes", help = "Loads/Creates an other data set. Note data sets are stored under ~/tinbo/notes/*.")
    fun loadNotes(@CliOption(key = arrayOf("name", "n"), mandatory = true,
            specifiedDefaultValue = Default.TASKS_NAME,
            unspecifiedDefaultValue = Default.TASKS_NAME) name: String) {
        executor.loadData(name)
    }

    @CliCommand("listNotes", help = "Lists all notes.")
    fun listNotes(): String {
        if (isEditMode) {
            return NEED_EDIT_MODE_TEXT
        } else {
            isListMode = true
            return executor.listData()
        }
    }

    @CliCommand("cancelNotes", help = "Cancels edit mode.")
    fun cancelNoteEditing(): String {
        if (isEditMode) {
            isEditMode = false
            isListMode = false
            return "Cancelled edit mode."
        } else {
            return "You need to be in edit mode to use cancel."
        }
    }

    @CliCommand("saveNotes", help = "Saves current editing if list command was used.")
    fun saveNotes(@CliOption(key = arrayOf("name", "n"), help = "Saves notes under a new data set (also a new filename).",
            specifiedDefaultValue = "", unspecifiedDefaultValue = "") name: String): String {
        if (isListMode && isEditMode) {
            isListMode = false
            isEditMode = false
            return executor.save(name)
        } else {
            return "You need to be in edit mode to use save."
        }
    }

    @CliCommand("editNote", "editNotes", help = "Edits the note entry(/entries) with given index")
    fun editNote(@CliOption(key = arrayOf("index", "i"), mandatory = true, help = "Index of the task to edit.") index: Int,
                 @CliOption(key = arrayOf("message", "msg", "m"), help = "Summary of the task.",
                         specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String): String {
        if (isListMode) {
            val i = index - 1
            if (executor.indexExists(i)) {
                isEditMode = true
                executor.editNote(i, DummyNote(message))
                return SUCCESS_MESSAGE
            } else {
                return "This index doesn't exist"
            }
        } else {
            return "Before editing tasks you have to 'list' them to get indices to work on."
        }
    }

    @CliCommand("deleteNote", "deleteNotes", "removeNote", "removeNotes", help = "Deletes notes from storage.")
    fun deleteNote(@CliOption(key = arrayOf("indices", "index", "i"), mandatory = true,
            help = "Indices pattern, allowed are numbers with space in between or intervals like 1-5 e.g. '1 2 3-5 6'.") indexPattern: String): String {

        if (isListMode) {
            try {
                val indices = parseIndices(indexPattern)
                isEditMode = true
                executor.deleteNotes(indices)
                return "Successfully deleted task(s)."
            } catch(e: IllegalArgumentException) {
                return "Could not parse the indices pattern. Use something like '1 2 3-5 6'."
            }
        } else {
            return "Before deleting notes you have to 'list' them to get indices to work on."
        }
    }

    private fun parseIndices(indexPattern: String): Set<Int> {
        val result = HashSet<Int>()
        val indices = indexPattern.split(" ")
        val regex = Regex("[1-9][0-9]*")
        val regex2 = Regex("[1-9]+-[1-9]+")
        for (index in indices) {
            if (regex.matches(index)) {
                result.add(index.toInt() - 1)
            } else if (regex2.matches(index)) {
                val interval = index.split("-")
                if (interval.size == 2) {
                    val (i1, i2) = Pair(interval[0].toInt(), interval[1].toInt())
                    IntRange(i1 - 1, i2 - 1)
                            .forEach { result.add(it) }
                } else {
                    throw IllegalAccessException()
                }
            } else {
                throw IllegalArgumentException()
            }
        }
        return result
    }
}
