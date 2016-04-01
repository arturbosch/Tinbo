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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * @author artur
 */
@Component
class NotesCommands(val executor: NotesExecutor = Injekt.get()) : CommandMarker {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

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
                val pair = parseDateTime(endTime, startTime)
                val formattedStartTime = pair.first
                var formattedEndTime = pair.second
                executor.addNote(NoteEntry(message, description, location, category, formattedStartTime, formattedEndTime))
            } catch(e: DateTimeParseException) {
                result = "Could not parse date, use format: yyyy-MM-dd HH:mm"
            }
        }

        return result
    }

    private fun parseDateTimeOrDefault(endTime: String, startTime: String): Pair<LocalDateTime?, LocalDateTime?> {
        var formattedStartTime: LocalDateTime? = null
        var formattedEndTime = formattedStartTime
        try {
            formattedStartTime = LocalDateTime.parse(startTime, formatter)
        } catch(ignored: DateTimeParseException) {
        }
        if (endTime.isNotEmpty()) {
            try {
                formattedEndTime = LocalDateTime.parse(endTime, formatter)
            } catch(ignored: DateTimeParseException) {
            }
        }
        return Pair(formattedStartTime, formattedEndTime)
    }

    private fun parseDateTime(endTime: String, startTime: String): Pair<LocalDateTime, LocalDateTime> {
        val formattedStartTime = LocalDateTime.parse(startTime, formatter)
        var formattedEndTime = formattedStartTime
        if (endTime.isNotEmpty()) formattedEndTime = LocalDateTime.parse(endTime, formatter)
        return Pair(formattedStartTime, formattedEndTime)
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
        if(isListMode && isEditMode) {
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
            isEditMode = true
            val i = index - 1
            if (executor.indexExists(i)) {
                val pair = parseDateTimeOrDefault(endTime, startTime)
                executor.editNote(i, DummyNote(message, category, location, description, pair.first, pair.second))
                return "Successfully edited note."
            } else {
                return "This index doesn't exist`"
            }
        } else {
            return "Before editing notes/tasks you have to 'list' them to get indices to work on."
        }
    }
}
