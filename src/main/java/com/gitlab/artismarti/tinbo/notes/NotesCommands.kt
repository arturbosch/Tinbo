package com.gitlab.artismarti.tinbo.notes

import com.gitlab.artismarti.tinbo.config.Default
import com.gitlab.artismarti.tinbo.config.ModeAdvisor
import com.gitlab.artismarti.tinbo.utils.DateTimeFormatters
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliAvailabilityIndicator
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.HashSet

/**
 * @author artur
 */
@Component
class NotesCommands(val executor: NotesExecutor = Injekt.get()) : CommandMarker {

    private val NEED_EDIT_MODE_TEXT = "Before adding or list notes/tasks exit edit mode with 'save' or 'cancel'."
    private val SUCCESS_MESSAGE = "Successfully added a note/task."
    private var isListMode: Boolean = false
    private var isEditMode: Boolean = false

    @CliAvailabilityIndicator("note", "task", "loadn", "listn", "editn")
    fun isAvailable(): Boolean {
        return ModeAdvisor.isNotesMode()
    }

    @CliCommand(value = "task", help = "Adds a new task.")
    fun newTask(@CliOption(key = arrayOf("message", "msg", "m"), mandatory = true, help = "Summary of the task.",
            specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
                @CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
                        specifiedDefaultValue = Default.MAIN_CATEGORY_NAME,
                        unspecifiedDefaultValue = Default.MAIN_CATEGORY_NAME) category: String,
                @CliOption(key = arrayOf("location", "loc", "l"), help = "Specify a location for this task.",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") location: String,
                @CliOption(key = arrayOf("description", "des", "d"), help = "Specify a description for this task.",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") description: String,
                @CliOption(key = arrayOf("start", "s"), help = "Specify a end time for this task. Format: yyyy-MM-dd HH:mm",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") startTime: String,
                @CliOption(key = arrayOf("end", "e"), help = "Specify a start time for this task. Format: yyyy-MM-dd HH:mm",
                        specifiedDefaultValue = "", unspecifiedDefaultValue = "") endTime: String): String {

        var result = SUCCESS_MESSAGE

        if (isEditMode) {
            result = NEED_EDIT_MODE_TEXT
        } else if (startTime.isEmpty() && endTime.isEmpty()) {
            executor.addNote(NoteEntry(message, description, location, category))
        } else if (startTime.isNotEmpty()) {
            try {
                val pair = DateTimeFormatters.parseDateTime(endTime, startTime)
                val formattedStartTime = pair.first
                var formattedEndTime = pair.second
                executor.addNote(NoteEntry(message, description, location, category, formattedStartTime, formattedEndTime))
            } catch(e: DateTimeParseException) {
                result = "Could not parse date, use format: yyyy-MM-dd HH:mm"
            }
        }

        return result
    }

    @CliCommand("loadNotes", "loadn", help = "Loads/Creates an other data set. Note data sets are stored under ~/tinbo/notes/*.")
    fun loadNotes(@CliOption(key = arrayOf("name", "n"), mandatory = true,
            specifiedDefaultValue = Default.NOTES_NAME,
            unspecifiedDefaultValue = Default.NOTES_NAME) name: String) {
        executor.loadData(name)
    }

    @CliCommand("note", "new", help = "Adds a new note.")
    fun newNote(@CliOption(key = arrayOf("message", "msg", "m"), mandatory = true, help = "Summary of the task.",
            specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
                @CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
                        specifiedDefaultValue = Default.MAIN_CATEGORY_NAME,
                        unspecifiedDefaultValue = Default.MAIN_CATEGORY_NAME) category: String): String {

        var result = SUCCESS_MESSAGE

        if (isEditMode) {
            result = NEED_EDIT_MODE_TEXT
        } else if (message.isEmpty()) {
            result = "You need to specify a message."
        } else {
            executor.addNote(NoteEntry(message, "", "", category))
        }

        return result
    }

    @CliCommand("listNotes", "listn", help = "Lists all notes and tasks.")
    fun listNotes(): String {
        if (isEditMode) {
            return NEED_EDIT_MODE_TEXT
        } else {
            isListMode = true
            return executor.listData()
        }
    }

    @CliCommand("saveNotes", "saven", help = "Saves current editing if list command was used.")
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

    @CliCommand("editNote", "editn", help = "Edits the note entry with given index")
    fun editNote(@CliOption(key = arrayOf("index", "i"), mandatory = true, help = "Index of the note to edit.") index: Int,
                 @CliOption(key = arrayOf("message", "msg", "m"), help = "Summary of the task.",
                         specifiedDefaultValue = "", unspecifiedDefaultValue = "") message: String,
                 @CliOption(key = arrayOf("category", "cat", "c"), help = "Category for the task",
                         specifiedDefaultValue = "", unspecifiedDefaultValue = "") category: String,
                 @CliOption(key = arrayOf("location", "loc", "l"), help = "Specify a location for this task.",
                         specifiedDefaultValue = "", unspecifiedDefaultValue = "") location: String,
                 @CliOption(key = arrayOf("description", "des", "d"), help = "Specify a description for this task.",
                         specifiedDefaultValue = "", unspecifiedDefaultValue = "") description: String,
                 @CliOption(key = arrayOf("start", "s"), help = "Specify a end time for this task. Format: yyyy-MM-dd HH:mm",
                         specifiedDefaultValue = "", unspecifiedDefaultValue = "") startTime: String,
                 @CliOption(key = arrayOf("end", "e"), help = "Specify a start time for this task. Format: yyyy-MM-dd HH:mm",
                         specifiedDefaultValue = "", unspecifiedDefaultValue = "") endTime: String): String {
        if (isListMode) {
            val i = index - 1
            if (executor.indexExists(i)) {
                isEditMode = true
                val pair = DateTimeFormatters.parseDateTimeOrDefault(endTime, startTime)
                executor.editNote(i, DummyNote(message, category, location, description, pair.first, pair.second))
                return "Successfully edited note."
            } else {
                return "This index doesn't exist"
            }
        } else {
            return "Before editing tasks you have to 'list' them to get indices to work on."
        }
    }

    @CliCommand("deleteNote", "deln", "removeNote", "rmn", help = "Deletes notes from storeage.")
    fun deleteNote(@CliOption(key = arrayOf("indices", "index", "i"), mandatory = true,
            help = "Indices pattern, allowed are numbers with space in between or intervals like 1-5 e.g. '1 2 3-5 6'.") indexPattern: String): String {

        if (isListMode) {
            try {
                val indices = parseIndices(indexPattern)
                isEditMode = true
                executor.deleteNotes(indices)
                return "Successfully deleted notes."
            } catch(e: IllegalArgumentException) {
                return "Could not parse the indices pattern. Use something like '1 2 3-5 6'."
            }
        } else {
            return "Before deleting tasks you have to 'list' them to get indices to work on."
        }
    }

    private fun parseIndices(indexPattern: String): Set<Int> {
        val result = HashSet<Int>()
        val indices = indexPattern.split(" ")
        val regex = Regex("[1-9][0-9]*")
        val regex2 = Regex("[1-9]+-[0-9]+")
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
